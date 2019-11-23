package hello.elan.jpa.auth;

import hello.elan.jpa.auth.dto.ModRoleResourceDto;
import hello.elan.jpa.auth.dto.RoleDto;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * @author ElanZh
 * @date 2019/5/7 16:58
 * @Package com.hello.ocean.jpa.biz.auth
 * @Description
 */
@RestController
@RequestMapping("api/role")
public class RoleCtrl {
    private final AuthService authService;

    public RoleCtrl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 获取所有角色的ID和name
     */
    @GetMapping("getAllRoleIdAndName")
    public List<Role> getAllRoleIdAndName(){
        return authService.getAllRoleIdAndName();
    }

    /**
     * 分页查询
     */
    @PostMapping("getPageList")
    public Page<Role> getPageList(@RequestBody RoleDto req){
        return authService.getRolePage(req);
    }

    /**
     * 修改角色
     */
    @PostMapping("mod")
    public Boolean mod(@RequestBody @Valid Role req){
        return authService.modRole(req);
    }

    /**
     * 增加角色
     */
    @PostMapping("add")
    public Boolean add(@RequestBody @Valid Role req){
        return authService.addRole(req);
    }

    /**
     * 删除角色
     */
    @GetMapping("del")
    public Boolean del(int roleId){
        return authService.delRole(roleId);
    }

    /**
     * 修改指定角色拥有的资源，返回修改后角色拥有的资源
     */
    @PostMapping("modResource")
    public Set<Resource> modResource(@Valid @RequestBody ModRoleResourceDto req){
        return authService.modRoleResource(req);
    }

    /**
     * 查询指定角色所隶属的manager
     */
    @GetMapping("getSubjectManager")
    public Role getSubjectManager(int roleId){
        return authService.getRoleSubjectManager(roleId);
    }

    /**
     * 修改角色所隶属的manager
     */
    @PostMapping("modSubjectManager")
    public Boolean modSubjectManager(@RequestBody Role req){
        if (req.getId() == null){
            throw new RuntimeException("请指定要编辑的角色");
        }
        return authService.modRoleSubjectManager(req);
    }

    /**
     * 检查用户名是否可用
     * @param roleId 角色ID
     * @param roleName 角色名
     * @return
     */
    @GetMapping("checkName")
    public Boolean checkName(Integer roleId, @Valid @NotBlank(message = "角色名不能为空") String roleName){
        return authService.checkRoleName(roleId, roleName);
    }


}
