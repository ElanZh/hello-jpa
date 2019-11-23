package hello.elan.jpa.auth.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于前端获取下级按钮
 * @author ElanZh
 * @date 2019/5/11 11:53
 * @Package com.kejiang.ift.bffm.biz.auth.dto
 * @Description
 */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ButtonDto {
    private Integer id;
    private String name;
    private List<String> subjectRoles = new ArrayList<>();
}