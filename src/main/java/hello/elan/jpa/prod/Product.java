package hello.elan.jpa.prod;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author 张一然
 * @date 2020/5/27 16:32
 */
@Entity
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class Product {
    @Id
    @GeneratedValue(generator = "SQ_PRODUCT")
    @Column(length = 11)
    private Integer id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updateAt;

    @CreatedBy
    @Column(updatable = false)
    private Integer createBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer updateBy;

}
