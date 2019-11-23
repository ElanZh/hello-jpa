package hello.elan.jpa.auth.dto;


import hello.elan.jpa.auth.entity.Role;

/**
 * @author ElanZh
 * @date 2019/5/12 16:50
 * @Package com.kejiang.ift.bffm.biz.auth.dto
 * @Description
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.EqualsAndHashCode(callSuper = true)
public class RoleDto extends Role {
    private Integer pageSize;
    private Integer pageNum;
}
