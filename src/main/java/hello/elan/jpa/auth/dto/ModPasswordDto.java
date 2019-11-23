package hello.elan.jpa.auth.dto;

import javax.validation.constraints.NotBlank;

/**
 * @author ElanZh
 * @date 2019年-07月-18号 下午2:59
 */
@lombok.Data
@lombok.NoArgsConstructor
public class ModPasswordDto {
    /** 原密码 */
    @NotBlank
    private String originalPassword;
    /** 新密码 */
    @NotBlank
    private String newPassword;
    /** 确认秘密 */
    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String key;
    @NotBlank
    private String code;
}
