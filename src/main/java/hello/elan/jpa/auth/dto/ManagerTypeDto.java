package hello.elan.jpa.auth.dto;


import hello.elan.jpa.auth.entity.Manager;

/**
 * @author ElanZh
 * @date 2019年-07月-10号 下午3:54
 * @Description 管理员类型信息
 */
@lombok.Data
@lombok.NoArgsConstructor
public class ManagerTypeDto {
    private Manager.ManagerType type;
    private String code;
}
