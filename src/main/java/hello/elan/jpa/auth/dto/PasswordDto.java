package hello.elan.jpa.auth.dto;

import javax.validation.constraints.NotBlank;

/**
 * @author ElanZh
 * @date 2019年-07月-08号 上午11:10
 * @Description 修改密码
 */
@lombok.Data
@lombok.NoArgsConstructor
public class PasswordDto {
    /** 令牌 */
    private String token;
    /** 新密码 */
    private String newPassword;
    /** 确认密码 */
    private String confirmPassword;

    @NotBlank
    private String key;
    @NotBlank
    private String code;
}
