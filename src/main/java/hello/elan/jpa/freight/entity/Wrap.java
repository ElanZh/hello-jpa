package hello.elan.jpa.freight.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "T_WRAP")
@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Wrap {
    /**
     * ID
     */
    @Id
    @Column(length = 11)
    @GeneratedValue(generator = "SQ_WRAP")
    private Integer id;

    /**
     * 包裹名
     */
    @Column(length = 20)
    private String name;

    /**
     * 卸货地点联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 装车地点
     */
    @JsonIgnore
    @ManyToOne(targetEntity = WaybillAddress.class)
    @JoinColumn(name = "loadAddressId")
    private WaybillAddress loadAddress;

    /**
     * 卸货地点
     */
    @JsonIgnore
    @ManyToOne(targetEntity = WaybillAddress.class)
    @JoinColumn(name = "unloadAddressId")
    private WaybillAddress unloadAddress;

    @JsonIgnore
    @ManyToOne(targetEntity = Waybill.class)
    @JoinColumn(name = "waybillId")
    private Waybill waybill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Wrap wrap = (Wrap) o;

        return new EqualsBuilder()
                .append(id, wrap.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
