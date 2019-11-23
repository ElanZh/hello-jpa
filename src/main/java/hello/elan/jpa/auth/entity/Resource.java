package hello.elan.jpa.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.elan.jpa.auth.dto.ButtonDto;
import hello.elan.jpa.auth.dto.TreeAble;
import hello.elan.jpa.auth.enums.NodeType;
import hello.elan.jpa.auth.enums.OpenStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "T_RESOURCE", uniqueConstraints = @UniqueConstraint(columnNames = {"pId", "sort"}))
@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Resource implements TreeAble<Resource>, Comparable<Resource> {

    /**
     * 上级资源
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pId")
    private Resource parent;

    /**
     * 下级资源
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pId")
    private Set<Resource> children = new HashSet<>();

    /**
     * 哪些角色可以访问
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "MT_ROLE_RESOURCE", inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    /**
     * 主键ID
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_RESOURCE")
    private Integer id;

    /**
     * 类型：NODE-菜单；LEAF-按钮；
     */
    @NotNull(message = "菜单类型不得为空")
    @Enumerated(EnumType.STRING)
    private NodeType type;

    /**
     * 名称，对应vue里的title
     */
    private String name;

    /**
     * 权限地址
     */
    private String authorityUrl;

    /**
     * 状态 OPEN-开启，CLOSE-关闭
     */
    @NotNull(message = "菜单状态不得为空")
    private OpenStatus status;

    /**
     * 排序
     */
    @NotNull(message = "排序值必填，否则查询时排序出错")
    private Integer sort;

    /**
     * 是否删除
     */
    private boolean isDelete;

    /**
     * 前端页面需要该字段以区分下级按钮和菜单
     */
    @Transient
    private List<ButtonDto> buttons;

    /**
     * 页面渲染左侧导航栏时需要该字段判断用户拥有的菜单
     */
    @Transient
    private List<String> subjectRoles;
    @Transient
    private List<Integer> subjectRoleIds;

    public Resource(Integer id) {
        this.id = id;
    }


    /**
     * JSON字段为 pid
     */
    @Transient
    private Integer pId;

    /**
     * 复制属性
     * <br/>不包含：父子级，创建/更新+时间/人
     *
     * @param res
     */
    public void copy(Resource res) {
        this.id = res.id;
        this.type = res.type;
        this.name = res.name;
        this.authorityUrl = res.authorityUrl;
        this.status = res.status;
        this.sort = res.sort;
        this.isDelete = res.isDelete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(@NotNull Resource o) {
        return sort - o.getSort();
    }

    public void sortChildren() {
        children = new TreeSet<>(children);
    }


    @Override
    public String toString() {
        return "Resource{" +
                "children=" + children +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", authorityUrl='" + authorityUrl + '\'' +
                ", status=" + status +
                ", sort=" + sort +
                ", isDelete=" + isDelete +
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