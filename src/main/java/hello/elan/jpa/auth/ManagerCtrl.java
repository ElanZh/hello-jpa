package hello.elan.jpa.auth;

import hello.elan.jpa.auth.dto.ManagerDto;
import hello.elan.jpa.auth.dto.ModPasswordDto;
import hello.elan.jpa.auth.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author ElanZh
 * @date 2019/5/7 16:58
 * @Package com.hello.ocean.jpa.biz.auth
 * @Description
 */
@RestController
@RequestMapping("api/manager")
public class ManagerCtrl {
    private final AuthService authService;

    public ManagerCtrl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 分页查询manager
     */
    @PostMapping("getPageList")
    public Page<Manager> getPageList(@RequestBody ManagerDto req){
        return authService.findAllByCondition(req);
    }

    /**
     * 增加manager
     */
    @PostMapping("add")
    public Boolean add(@RequestBody Manager req){
        return authService.addManager(req);
    }

    /**
     * 编辑manager
     */
    @PostMapping("mod")
    public Boolean mod(@RequestBody Manager req){
        return authService.modManager(req);
    }
    /**
     * 删除manager
     */
    @GetMapping("del")
    public Boolean del(int managerId){
        return authService.delManager(managerId);
    }

    /**
     * 修改一个manager所拥有的角色
     */
    @PostMapping("modRole")
    public Boolean modRole(@RequestBody ManagerDto req){
        if (req.getId() == null){
            throw new RuntimeException("入参错误");
        }
        return authService.modManagerRole(req);
    }


    /**
     * 检查用户名是否可用
     * @author ElanZh
     * @date 2019/6/14 15:51
     */
    @GetMapping("checkUserName")
    public Boolean checkUserName(@Valid @NotBlank(message = "用户名不得为空") String username) {
        return authService.checkManagerUserName(username);
    }


}
