package hello.elan.jpa.config;

import hello.elan.jpa.auth.entity.Manager;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import hello.elan.jpa.auth.enums.Effect;
import hello.elan.jpa.auth.enums.Gender;
import hello.elan.jpa.auth.enums.NodeType;
import hello.elan.jpa.auth.enums.OpenStatus;
import hello.elan.jpa.auth.repo.ManagerRepo;
import hello.elan.jpa.auth.repo.ResourceRepo;
import hello.elan.jpa.auth.repo.RoleRepo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ElanZh
 * @date 2019/5/8 13:26
 * @Description springboot的开机配置
 */
@Component
public class InitRunner implements ApplicationRunner {

    private final ResourceRepo resourceRepo;
    private final RoleRepo roleRepo;
    private final ManagerRepo managerRepo;

    public InitRunner(ResourceRepo resourceRepo, RoleRepo roleRepo, ManagerRepo managerRepo) {
        this.resourceRepo = resourceRepo;
        this.roleRepo = roleRepo;
        this.managerRepo = managerRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 开机检查 基本菜单和权限
        checkAuth();
    }

    /**
     * 检查 菜单，角色，管理员
     *
     * @author ElanZh
     * @date 2019/5/8 15:48
     */
    private void checkAuth() {
        // 检查菜单
        if (resourceRepo.count() == 0) {

            Resource root = Resource.builder().type(NodeType.NODE).status(OpenStatus.OPEN).name("root").sort(0).build();
            root = resourceRepo.saveAndFlush(root);
            root.setParent(root);
            Resource sysSetting = Resource.builder().parent(root).type(NodeType.NODE).status(OpenStatus.OPEN).name("系统设置").sort(99).build();
            sysSetting = resourceRepo.saveAndFlush(sysSetting);
            Resource authMenu = Resource.builder().parent(sysSetting).type(NodeType.NODE).status(OpenStatus.OPEN).name("权限管理").sort(1).build();
            authMenu = resourceRepo.saveAndFlush(authMenu);
            Resource managerMenu = Resource.builder().parent(authMenu).type(NodeType.NODE).status(OpenStatus.OPEN).name("账号管理").sort(1).build();
            managerMenu = resourceRepo.saveAndFlush(managerMenu);
            Resource roleMenu = Resource.builder().parent(authMenu).type(NodeType.NODE).status(OpenStatus.OPEN).name("角色管理").sort(2).build();
            roleMenu = resourceRepo.saveAndFlush(roleMenu);
            Resource resourceMenu = Resource.builder().parent(authMenu).type(NodeType.NODE).status(OpenStatus.OPEN).name("菜单管理").sort(3).build();
            resourceMenu = resourceRepo.saveAndFlush(resourceMenu);
            Resource hideMenu = Resource.builder().parent(root).type(NodeType.NODE).status(OpenStatus.OPEN).name("隐藏菜单").sort(100).build();
            hideMenu = resourceRepo.saveAndFlush(hideMenu);

            List<Resource> rs = new ArrayList<>(255);

            rs.add(Resource.builder().parent(managerMenu).name("分页查询").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/getPageList").sort(1).build());
            rs.add(Resource.builder().parent(managerMenu).name("编辑").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/mod").sort(2).build());
            rs.add(Resource.builder().parent(managerMenu).name("添加").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/add").sort(3).build());
            rs.add(Resource.builder().parent(managerMenu).name("删除").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/del").sort(4).build());
            rs.add(Resource.builder().parent(managerMenu).name("修改拥有的角色").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/modRole").sort(5).build());
            rs.add(Resource.builder().parent(managerMenu).name("发送修改密码邮件").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/sendMailForUpdatePassword").sort(6).build());
            rs.add(Resource.builder().parent(managerMenu).name("校验用户名").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/checkUserName").sort(7).build());

            rs.add(Resource.builder().parent(roleMenu).name("获取指定角色隶属的人员").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/getSubjectManager").sort(1).build());
            rs.add(Resource.builder().parent(roleMenu).name("修改指定角色隶属的人员").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/modSubjectManager").sort(2).build());
            rs.add(Resource.builder().parent(roleMenu).name("分页查询").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/getPageList").sort(3).build());
            rs.add(Resource.builder().parent(roleMenu).name("添加").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/add").sort(4).build());
            rs.add(Resource.builder().parent(roleMenu).name("编辑").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/mod").sort(5).build());
            rs.add(Resource.builder().parent(roleMenu).name("删除").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/del").sort(6).build());
            rs.add(Resource.builder().parent(roleMenu).name("查询所有激活角色的ID和Name").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/getAllRoleIdAndName").sort(7).build());
            rs.add(Resource.builder().parent(roleMenu).name("修改角色所拥有的资源").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/modResource").sort(8).build());
            rs.add(Resource.builder().parent(roleMenu).name("检查角色名是否可用").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/role/checkName").sort(9).build());

            rs.add(Resource.builder().parent(resourceMenu).name("添加").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/add").sort(1).build());
            rs.add(Resource.builder().parent(resourceMenu).name("编辑").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/mod").sort(2).build());
            rs.add(Resource.builder().parent(resourceMenu).name("删除").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/del").sort(3).build());
            rs.add(Resource.builder().parent(resourceMenu).name("查询全部").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/getList").sort(4).build());
            rs.add(Resource.builder().parent(resourceMenu).name("所隶属的角色").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/getSubjectRoles").sort(5).build());

            rs.add(Resource.builder().parent(hideMenu).name("获取用户信息").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/login/manager").sort(1).build());
            rs.add(Resource.builder().parent(hideMenu).name("获取左边菜单栏导航").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/resource/getMenu").sort(2).build());
            rs.add(Resource.builder().parent(hideMenu).name("上传文件").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/file/upload").sort(3).build());
            rs.add(Resource.builder().parent(hideMenu).name("获取文件链接前缀").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/file/getUrl").sort(4).build());
            rs.add(Resource.builder().parent(hideMenu).name("修改密码").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/api/manager/updatePassword").sort(5).build());
            rs.add(Resource.builder().parent(hideMenu).name("找回密码").type(NodeType.LEAF).status(OpenStatus.OPEN).authorityUrl("/backPassWord/getLogin").sort(6).build());
            rs.add(Resource.builder().parent(hideMenu).name("主页").type(NodeType.NODE).status(OpenStatus.OPEN).authorityUrl("/api/store/homePage/index").sort(7).build());
            rs.add(Resource.builder().parent(hideMenu).name("登出").type(NodeType.NODE).status(OpenStatus.OPEN).authorityUrl("/api/login/logout").sort(8).build());

            resourceRepo.saveAll(rs);
        }

        // 检查角色
        Role role = roleRepo.findByRoleName("admin");
        if (role == null) {
            role = Role.builder().roleName("admin").status(Effect.EFFECTIVE).remark("admin角色").sort(0).build();
            role = roleRepo.saveAndFlush(role);
        }
        List<Resource> allRs = resourceRepo.findAll();
        for (Resource resource : allRs) {
            resource.getRoles().add(role);
        }
        resourceRepo.saveAll(allRs);

        // 检查管理员
        if (managerRepo.countByUsername("admin") == 0) {
            List<Role> allRole = roleRepo.findAll();
            Manager manager = Manager.builder().roles(new HashSet<>(allRole)).username("admin").type(Manager.ManagerType.PLATFORM).nickName("超级管理员").password("CB4E7F6F1BF84E0777A8C23958FB1960").md5Salt("695175").gender(Gender.MALE).email("yakun.sun@sunyard.com").status(Effect.EFFECTIVE).build();
            managerRepo.save(manager);
        }

    }
}
