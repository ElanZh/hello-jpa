package hello.elan.jpa.freight.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "T_WAYBILL_ADDRESS")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class WaybillAddress {
    /**
     * ID
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_WAYBILL_ADDRESS")
    private Integer id;

    /**
     * 地址明细
     */
    private String addressDetail;

    /**
     * 配送顺序
     */
    @Column(length = 8)
    private Integer sort;

    /**
     * 关联运单
     */
    @JsonIgnore
    @ManyToOne(targetEntity = Waybill.class)
    @JoinColumn(name = "waybillId")
    private Waybill waybill;

    /**
     * 卸货包裹
     */
    @OneToMany(cascade = CascadeType.REFRESH,targetEntity = Wrap.class)
    @JoinColumn(name = "unloadAddressId")
    private Set<Wrap> wrapSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WaybillAddress that = (WaybillAddress) o;

        return new EqualsBuilder()
                .append(addressDetail, that.addressDetail)
                .append(waybill.getId(), that.waybill.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(addressDetail)
                .append(waybill.getId())
                .toHashCode();
    }
}
