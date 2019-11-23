package hello.elan.jpa.auth.dto;


import hello.elan.jpa.auth.enums.ActiveType;

import javax.validation.constraints.NotNull;

/**
 * @author ElanZh
 * @date 2019/6/17 15:43
 * @Package com.kejiang.ift.bffm.biz.auth.dto
 * @Description 修改角色资源DTO
 */
@lombok.Data
public class ModRoleResourceDto {
    /** 角色ID */
    @NotNull(message = "角色ID不得为空")
    private Integer roleId;
    /** 资源ID */
    @NotNull(message = "资源ID不得为空")
    private Integer resourceId;
    /** 操作类型 */
    @NotNull(message = "操作类型不得为空")
    private ActiveType activeType;
}
