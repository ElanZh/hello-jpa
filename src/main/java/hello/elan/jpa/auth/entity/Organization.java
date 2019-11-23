package hello.elan.jpa.auth.entity;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ElanZh
 * @date 2019/6/20 15:48
 * @Package com.kejiang.ift.bffm.biz.auth.entity
 * @Description 组织结构
 */
@Entity
@Table(name="T_ORGANIZATION")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
@EntityListeners(AuditingEntityListener.class)
public class Organization {

    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_ORGANIZATION")
    private Integer id;

    /**组织名称*/
    @Column
    private String name;

    /**组织编码*/
    @Column
    private String code;

    /**父组织*/
    @ManyToOne
    @JoinColumn(name="pid")
    private Organization parent;

    /**子组织*/
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="pid")
    private Set<Organization> children = new HashSet<>();

    /**
     * 拥有的用户
     */
    @OneToMany(mappedBy="organization",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<Manager> users = new HashSet<>();


    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime updateAt;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createAt;
}
