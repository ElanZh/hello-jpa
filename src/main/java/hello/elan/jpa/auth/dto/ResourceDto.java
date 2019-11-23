package hello.elan.jpa.auth.dto;


import hello.elan.jpa.auth.entity.Resource;

import javax.validation.constraints.NotNull;

/**
 * @author ElanZh
 * @date 2019/5/11 16:23
 * @Package com.kejiang.ift.bffm.biz.auth.dto
 * @Description
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResourceDto extends Resource {
    @NotNull
    private Integer parentId;

    public ResourceDto(Integer id, String name, Integer parentId) {
        super.setId(id);
        super.setName(name);
        this.parentId = parentId;
    }
}
