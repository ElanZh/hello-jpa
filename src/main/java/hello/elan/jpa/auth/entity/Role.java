package hello.elan.jpa.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.elan.jpa.auth.enums.Effect;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_ROLE")
@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Role {

    /**
     * 被分配给的用户
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Manager> managers = new HashSet<>();

    /**
     * 可以访问的资源
     */
    @ManyToMany(mappedBy = "roles")
    private Set<Resource> resources = new HashSet<>();

    /**
     * 角色ID，主键
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_ROLE")
    private Integer id;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名不得为空")
    @Column(unique = true)
    private String roleName;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private Effect status;

    /**
     * 备注
     */
    @Lob
    private String remark;

    /**
     * 排序值，用于页面展示顺序，越小越靠前，默认为0
     */
    private Integer sort = 0;

    /**
     * @param id ID
     * @param roleName 角色名
     */
    public Role(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Role(Integer id) {
        this.id = id;
    }


    /**
     * 复制属性
     * <br/>不包含：managers，resources，创建/更新+时间/人
     * @param res
     */
    public void copy(Role res) {
        this.id = res.id;
        this.roleName = res.roleName;
        this.remark = res.remark;
        this.status = res.status;
        this.sort = res.sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", sort=" + sort +
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