package hello.elan.jpa.prod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 张一然
 * @date 2020/5/27 16:32
 */
@Entity
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "SQ_PRODUCT")
    @Column(length = 11)
    private Integer id;

    private String name;
}
