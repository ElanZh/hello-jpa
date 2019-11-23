package hello.elan.jpa.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.elan.jpa.auth.enums.Effect;
import hello.elan.jpa.auth.enums.Gender;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_MANAGER")
@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Manager {

    /**
     * 所属组织
     */
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name="organizationId")
    private Organization organization;
    @Transient
    private Integer organizationId;

    /**
     * 拥有角色
     */
    @ManyToMany //(fetch = FetchType.EAGER)
    @JoinTable(name = "MT_MANAGER_ROLE", joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    /**
     * ID
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_MANAGER")
    private Integer id;

    /**
     * 账号类型
     */
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ManagerType type;


    /**
     * 账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 新密码
     */
    @Transient
    private String newPassword;
    /**
     * 确认密码
     */
    @Transient
    private String confirmPassword;

    /**
     * MD5盐
     */
    @JsonIgnore
    private String md5Salt;

    /**
     * 性别，MALE:男，FEMALE:女
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private Effect status;

    /**
     * 找回密码状态；true:找回状态。默认false
     */
    private boolean passwordStatus;

    /**
     * 复制入参的属性，
     * <br/>不含MD5、密码、roles、密码找回状态、更新/创建+时间/人
     *
     * @param res 源
     */
    public void copy(Manager res) {
        this.id = res.id;
        this.username = res.username;
        this.nickName = res.nickName;
        this.gender = res.gender;
        this.email = res.email;
        this.status = res.status;
        this.type = res.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manager manager = (Manager) o;
        return Objects.equals(id, manager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum ManagerType {
        /** 账号类型：平台，调度 */
        PLATFORM, DISPATCH
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", md5Salt='" + md5Salt + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", passwordStatus=" + passwordStatus +
                ", updateAt=" + updateAt +
                ", createAt=" + createAt +
                '}';
    }

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updateAt;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createAt;
}
