package hello.elan.jpa.auth;

import hello.elan.jpa.auth.entity.Manager;
import hello.elan.jpa.auth.entity.Resource;
import hello.elan.jpa.auth.entity.Role;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author ElanZh
 * @date 2019/5/7 16:58
 * @Package com.hello.ocean.jpa.biz.auth.repo
 * @Description
 */
@RestController
@RequestMapping("api/resource")
public class ResourceCtrl {
    private final AuthService authService;

    public ResourceCtrl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 获取页面左侧菜单树
     */
    @GetMapping("getMenu")
    public Resource getTreeMenu() {
        return authService.getTreeMenu();
    }

    /**
     * 查询所有菜单
     */
    @GetMapping("getList")
    public Resource getlist() {
        return authService.getResource();
    }

    /* 可能没用，因为getList接口已经返回了全量数据
    @GetMapping("get")
    public <Resource> get(){
        return null;}
     */

    /**
     * 增加一个菜单
     */
    @PostMapping("add")
    public Resource add(@RequestBody @Valid Resource req) {
        return authService.addResource(req);
    }

    /**
     * 修改一个菜单
     */
    @PostMapping("mod")
    public Boolean mod(@RequestBody @Valid Resource req) {
        return authService.modResource(req);
    }

    /**
     * 删除一个菜单
     */
    @GetMapping("del")
    public Boolean del(@Valid @NotNull Integer resourceId) {
        return authService.delResource(resourceId);
    }

    /**
     * 查询该菜单分配给了哪些角色
     * @param resourceId 菜单ID
     * @return
     * @author ElanZh
     * @date 2019/5/11 17:18
     */
    @GetMapping("getSubjectRoles")
    public List<Role> getSubjectRoles(int resourceId){
        return authService.getResourceSubjectRoles(resourceId);
    }

    /**
     * 查询该菜单隶属哪些账号
     * @param resourceId 菜单ID
     * @return
     * @author ElanZh
     * @date 2019/5/11 17:18
     */
    @GetMapping("getSubjectManagers")
    public Set<Manager> getSubjectManagers(int resourceId){
        return authService.getResourceSubjectManagers(resourceId);
    }

}
