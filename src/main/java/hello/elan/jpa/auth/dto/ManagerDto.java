package hello.elan.jpa.auth.dto;


import hello.elan.jpa.auth.entity.Manager;

/**
 * @author ElanZh
 * @date 2019/5/9 19:52
 * @Package com.kejiang.ift.bffm.biz.auth.dto
 * @Description
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
public class ManagerDto extends Manager {
    private Integer pageSize;
    private Integer pageNum;
    private Integer roleId;
}
