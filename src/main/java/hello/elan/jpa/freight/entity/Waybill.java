package hello.elan.jpa.freight.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "T_WAYBILL")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Waybill {
    /**
     * ID
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_WAYBILL")
    private Integer id;

    /**
     * 运单号
     */
    @Column(length = 30)
    private String waybillNum;

    /**
     * 关联包裹
     */
    @OneToMany(cascade = CascadeType.REFRESH, targetEntity = Wrap.class)
    @JoinColumn(name = "waybillId")
    private Set<Wrap> wrapSet = new HashSet<>();

    /**
     * 关联运单地址
     */
    @OneToMany(mappedBy = "waybill",targetEntity = WaybillAddress.class)
    private List<WaybillAddress> waybillAddressSet = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Waybill waybill = (Waybill) o;

        return new EqualsBuilder()
                .append(id, waybill.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
