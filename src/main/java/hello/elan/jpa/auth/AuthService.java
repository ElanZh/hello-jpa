package hello.elan.jpa.auth;

import hello.elan.jpa.auth.dto.ManagerDto;
import hello.elan.jpa.auth.dto.ModPasswordDto;
import hello.elan.jpa.auth.dto.ModRoleResourceDto;
import hello.elan.jpa.auth.dto.RoleDto;
import hello.elan.jpa.auth.entity.Manager;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import hello.elan.jpa.auth.enums.Effect;
import hello.elan.jpa.auth.enums.NodeType;
import hello.elan.jpa.auth.repo.ManagerRepo;
import hello.elan.jpa.auth.repo.ResourceRepo;
import hello.elan.jpa.auth.repo.RoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ElanZh
 * @date 2019/5/7 15:26
 * @Package com.hello.ocean.jpa.biz.auth
 * @Description
 */
@Service(value = "AuthService")
@Slf4j
public class AuthService {
    private final ManagerRepo managerRepo;
    private final ResourceRepo resourceRepo;
    private final RoleRepo roleRepo;

    public AuthService(ManagerRepo managerRepo, ResourceRepo resourceRepo, RoleRepo roleRepo) {
        this.managerRepo = managerRepo;
        this.resourceRepo = resourceRepo;
        this.roleRepo = roleRepo;
    }

    /**
     * 通过用户名查找manager
     *
     * @param username 用户名
     * @return 管理员对象
     * @author ElanZh
     * @date 2019/5/9 14:30
     */
    public Manager findByUsername(String username) {
        return managerRepo.findByUsername(username);
    }

    /**
     * 查找用户拥有的角色
     *
     * @param username
     * @return 用户拥有的角色
     * @author ElanZh
     * @date 2019/5/9 14:30
     */
    public Set<Role> findRolesByAccount(String username) {
        return managerRepo.findByUsername(username).getRoles();
    }

    /**
     * 查询用户拥有的资源
     *
     * @param username 用户名
     * @return 用户拥有的资源
     * @author ElanZh
     * @date 2019/5/9 14:30
     */
    public Set<Resource> findResourceByAccount(String username) {
        Set<Role> roles = findRolesByAccount(username);
        return resourceRepo.findByRolesIn(roles);
    }

    /**
     * 树形菜单结构体
     * 先从redis中获取，redis中没有查询数据库
     *
     * @author ElanZh
     * @date 2019/5/9 18:58
     */
    Resource getTreeMenu() {
        return generateTreeMenu(Arrays.asList(resourceRepo.findRoot()));
    }

    /**
     * 将root菜单及其所有子菜单改造成前端需要的结构体
     *
     * @param parents root菜单
     * @return 前端要用的结构体
     */
    private Resource generateTreeMenu(List<Resource> parents) {
        DataBuilder.buildMenu(parents);
        return parents.get(0);
    }

    /**
     * 获取数据库中的所有菜单
     */
    public Resource getResource() {
        Resource root = resourceRepo.findRoot();
        DataBuilder.traverseTree(root, node -> {
            // 设置已配给角色
            node.setSubjectRoles(node.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
            // 设置pid
            if (node.getParent() != null) {
                node.setPId(node.getParent().getId());
            }
            // 按sort字段排序
            node.sortChildren();
        });
        return root;
    }

    /**
     * 条件分页查询用户
     *
     * @author ElanZh
     * @date 2019/5/10 15:04
     */
    Page<Manager> findAllByCondition(ManagerDto req) {
        Page<Manager> result = findPageByCondition(req);
        // 不返回角色下的资源
        result.getContent().forEach(manager -> manager.getRoles().forEach(role -> role.setResources(null)));
        return result;
    }

    private Page<Manager> findPageByCondition(ManagerDto req) {
        return managerRepo.findAll(((root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotBlank(req.getUsername())) {
                predicate.add(cb.equal(root.get("username"), req.getUsername()));
            }
            if (req.getStatus() != null) {
                predicate.add(cb.equal(root.get("status").as(Effect.class), req.getStatus()));
            }
            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        }), PageRequest.of(req.getPageNum() - 1, req.getPageSize()));
    }

    /**
     * 增加一个菜单
     *
     * @author ElanZh
     * @date 2019/5/11 16:50
     */
    Resource addResource(Resource req) {
        req.setChildren(null);
        req.setParent(new Resource(req.getPId()));
        // 新增菜单不能与其他兄弟菜单排序值重复
        if (resourceRepo.countParentAndSort(req) > 0) {
            throw new RuntimeException("同一菜单下的一级子菜单，排序值不能重复！");
        }
        // 保存菜单信息
        req = resourceRepo.saveAndFlush(req);
        log.info("新增菜单：ID" + req.getId() + ",PID" + req.getPId());
        // 保存菜单关联角色关系
        if (!CollectionUtils.isEmpty(req.getSubjectRoleIds())) {
            Set<Role> roleList = new HashSet<>(req.getSubjectRoleIds().size());
            req.getSubjectRoleIds().forEach(x -> roleList.add(new Role(x)));
            req.setRoles(roleList);
            req = resourceRepo.saveAndFlush(req);
            log.info("新增菜单" + req.getId() + "增加关联角色关系：" + req.getSubjectRoleIds());
        }
        return req;
    }

    /**
     * 修改一个菜单
     *
     * @author ElanZh
     * @date 2019/5/11 17:00
     */
    boolean modResource(Resource req) {
        Optional<Resource> existResource = resourceRepo.findById(req.getId());
        existResource.ifPresent(exist -> {
            // 修改的菜单排序值不能与其他兄弟菜单的排序值重复
            if (!req.getSort().equals(exist.getSort())) {
                Resource selectParam = new Resource(req.getId());
                selectParam.setSort(req.getSort());
                selectParam.setParent(new Resource(exist.getParent().getId()));
                if (resourceRepo.countParentAndSortAndNotIncludeSelf(selectParam) > 0) {
                    throw new RuntimeException("修改目标和其兄弟菜单的排序值不能重复！");
                }
            }
            exist.copy(req);
            if (CollectionUtils.isEmpty(req.getSubjectRoleIds())) {
                exist.setRoles(null);
            } else {
                Set<Role> roleList = new HashSet<>(req.getSubjectRoleIds().size());
                req.getSubjectRoleIds().forEach(x -> roleList.add(new Role(x)));
                exist.setRoles(roleList);
            }
            resourceRepo.save(exist);
        });
        return true;
    }

    /**
     * 删除一个菜单，如果有子菜单则不允许删除
     *
     * @author ElanZh
     * @date 2019/5/11 17:11
     */
    boolean delResource(int resourceId) {
        Optional<Resource> isExist = resourceRepo.findById(resourceId);
        isExist.ifPresent(existEntity -> {
            if (!CollectionUtils.isEmpty(existEntity.getChildren())) {
                throw new RuntimeException("先删除子级！");
            }
            resourceRepo.deleteById(resourceId);
        });
        return true;
    }

    /**
     * 查询目标菜单所分配的角色
     *
     * @author ElanZh
     * @date 2019/5/12 19:02
     */
    List<Role> getResourceSubjectRoles(int resourceId) {
        List<Role> result = new ArrayList<>();
        resourceRepo.findById(resourceId).ifPresent(
                resource -> result.addAll(
                        resource.getRoles().stream().peek(item -> {
                            item.setManagers(null);
                            item.setResources(null);
                        }).collect(Collectors.toList())
                )
        );
        return result;
    }

    /**
     * 查询所有生效角色的id和roleName
     * 一般用于页面下拉框
     *
     * @author ElanZh
     * @date 2019/5/12 16:40
     */
    List<Role> getAllRoleIdAndName() {
        return roleRepo.findIdAndRoleNameAllEffective();
    }

    /**
     * 条件查询查询RoleList，带分页+创建时间倒序
     *
     * @author ElanZh
     * @date 2019/5/12 16:57
     */
    Page<Role> getRolePage(RoleDto req) {
        Page<Role> result = roleRepo.findAll(
                ((root, query, cb) -> {
                    List<Predicate> predicate = new ArrayList<>();
                    if (StringUtils.isNotBlank(req.getRoleName())) {
                        predicate.add(cb.like(root.get("roleName"), "%" + req.getRoleName() + "%"));
                    }
                    return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
                }),
                PageRequest.of(
                        req.getPageNum() - 1,
                        req.getPageSize(),
                        Sort.Direction.DESC,
                        "audit.createdAt"
                )
        );
        result.getContent().forEach(role -> role.getResources().forEach(resource -> resource.setChildren(null)));
        return result;
    }

    /**
     * 编辑角色
     *
     * @author ElanZh
     * @date 2019/5/12 18:56
     */
    boolean modRole(Role req) {
        Optional<Role> existRole = roleRepo.findById(req.getId());
        existRole.ifPresent(exist -> {
            exist.copy(req);
            roleRepo.save(exist);
        });
        return true;
    }

    /**
     * 新增角色
     *
     * @author ElanZh
     * @date 2019/5/12 18:56
     */
    boolean addRole(Role req) {
        req.setId(null);
        // 默认有效
        req.setStatus(Effect.EFFECTIVE);
        return roleRepo.saveAndFlush(req).getId() != null;
    }

    /**
     * 修改一个角色拥有的资源
     *
     * @param req
     * @return
     * @author ElanZh
     * @date 2019/5/12 19:00
     */
    Set<Resource> modRoleResource(@Valid ModRoleResourceDto req) {
        Role existRole = roleRepo.findById(req.getRoleId()).orElseThrow(() -> new RuntimeException("要修改的角色不存在！"));
        Optional<Resource> existResource = resourceRepo.findById(req.getResourceId());
        existResource.ifPresent(resource -> {
            // 操作目标是一个按钮则直接改
            if (resource.getType() == NodeType.LEAF) {
                switch (req.getActiveType()) {
                    case ADD:
                        resource.getRoles().add(existRole);
                        break;
                    case DEL:
                        resource.getRoles().remove(existRole);
                        break;
                    default:
                        return;
                }
            }
            // 操作目标是一个菜单，则遍历其各个子级
            else {
                switch (req.getActiveType()) {
                    case ADD:
                        DataBuilder.traverseTree(resource, item -> item.getRoles().add(existRole));
                        break;
                    case DEL:
                        DataBuilder.traverseTree(resource, item -> item.getRoles().remove(existRole));
                        break;
                    default:
                        return;
                }
            }
            resourceRepo.save(resource);
        });
        Set<Resource> result = roleRepo.findById(req.getRoleId()).orElse(new Role()).getResources();
        result.forEach(x -> x.setChildren(null));
        return result;
    }

    /**
     * 查询一个角色隶属于哪些manager
     *
     * @param roleId 角色ID
     * @return managerList
     */
    Role getRoleSubjectManager(int roleId) {
        Role result = new Role();
        result.setId(roleId);
        Optional<Role> role = roleRepo.findById(roleId);
        role.ifPresent(exist -> result.setManagers(exist.getManagers()));
        return result;
    }

    /**
     * 修改指定角色隶属的manager
     */
    boolean modRoleSubjectManager(Role req) {
        Optional<Role> existRole = roleRepo.findById(req.getId());
        existRole.ifPresent(exist -> {
            exist.setManagers(req.getManagers());
            roleRepo.save(exist);
        });
        return true;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    boolean delRole(int roleId) {
        Optional<Role> existRole = roleRepo.findById(roleId);
        existRole.ifPresent(roleRepo::delete);
        return true;
    }

    /**
     * 删除manager
     *
     * @param managerId ID
     * @return boolean
     */
    boolean delManager(int managerId) {
        managerRepo.deleteById(managerId);
        return true;
    }

    /**
     * 增加一个账号
     *
     * @param req
     * @return
     */
    boolean addManager(Manager req) {
        // 密码确认
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new RuntimeException("密码输入不一致！");
        }
        req.setPassword(req.getNewPassword());
        generatePassword(req);
        return managerRepo.saveAndFlush(req).getId() != null;
    }

    /**
     * 入参将原明文密码setPassword，方法内setMd5和password，可以直接入库
     *
     * @author ElanZh
     * @date 2019/7/9 下午1:29
     */
    public static void generatePassword(Manager req) {
        // 生成salt
        String salt = RandomStringUtils.randomNumeric(6);
        req.setMd5Salt(salt);
        // 生成加密密码
        req.setPassword(DigestUtils.md5DigestAsHex((req.getPassword() + salt).getBytes()));
    }

    /**
     * 编辑一个账号
     *
     * @param req
     * @return
     */
    boolean modManager(Manager req) {
        // 查询已有账号
        Optional<Manager> existManager = managerRepo.findById(req.getId());
        existManager.ifPresent(exist -> {
            exist.copy(req);
            managerRepo.save(exist);
        });
        return true;
    }

    /**
     * 修改一个账号所拥有的角色
     *
     * @param req
     * @return
     */
    boolean modManagerRole(ManagerDto req) {
        Optional<Manager> existManager = managerRepo.findById(req.getId());
        existManager.ifPresent(exist -> {
            exist.setRoles(req.getRoles());
            managerRepo.save(exist);
        });
        return true;
    }

    /**
     * 检查用户名是否可用
     *
     * @param username 用户名
     * @return true=可用， false=不可用
     */
    boolean checkManagerUserName(String username) {
        return managerRepo.findByUsername(username) == null;
    }

    /**
     * 检查角色名是否可用
     *
     * @param roleId
     * @param roleName 角色名
     * @return true=可用， false=不可用
     */
    boolean checkRoleName(Integer roleId, String roleName) {
        if (roleId == null) {
            return roleRepo.countByRoleName(roleName) == 0;
        } else {
            return roleRepo.countByRoleNameAndAndIdIsNot(roleName, roleId) == 0;
        }
    }

    /**
     * 查询菜单隶属的账号
     *
     * @return
     */
    Set<Manager> getResourceSubjectManagers(int resourceId) {
        Optional<Resource> resource = resourceRepo.findById(resourceId);
        HashSet<Manager> result = new HashSet<>();
        resource.ifPresent(x -> x.getRoles().forEach(role -> result.addAll(role.getManagers())));
        result.forEach(x -> x.setRoles(null));
        return result;
    }


    /**
     * 修改自己的密码
     *
     * @param req
     * @param managerId
     * @return java.lang.String
     * @author ElanZh
     * @date 2019/7/18 下午3:15
     */
    public boolean modOwnPassword(ModPasswordDto req, int managerId) {
        Manager manager = managerRepo.findById(managerId).orElseThrow(() -> new RuntimeException("账号不存在"));
        // 原密码错误
        if (!manager.getPassword().equals(DigestUtils.md5DigestAsHex((req.getOriginalPassword() + manager.getMd5Salt()).getBytes()))) {
            throw new RuntimeException("密码错误");
        }
        // 输入不一致
        if (!req.getConfirmPassword().equals(req.getNewPassword())) {
            throw new RuntimeException("输入不一致");
        }
        // 新密码和原密码相同
        if (req.getOriginalPassword().equals(req.getNewPassword())) {
            return true;
        }
        manager.setPassword(req.getNewPassword());
        generatePassword(manager);
        managerRepo.save(manager);
        log.debug("用户" + manager.getUsername() + "修改自己密码为:" + req.getNewPassword() + ":md5->" + manager.getPassword() + ":salt->" + manager.getMd5Salt());
        return true;
    }
}
