package com.amazingstock.stock.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

/**
 * The Stock Entity
 * For requirements, like data history tracking or audit and also related to reference integrity.
 * I use not permanently delete data from the database, instead of physically deleting it from a table in the database
 *
 * By using the @Where annotation, we can't get the deleted product data in case we still want the deleted data to be accessible. An example of this is a user with administrator-level that has full access and can view the data that has been “deleted”.
 * To implement this, we shouldn't use the @Where annotation but two different annotations, @FilterDef, and @Filter. With these annotations we can dynamically add conditions as needed:
 */

@Entity
@Table(name = "stock")
@Getter @Setter
@SQLDelete(sql = "UPDATE stock SET deleted = TRUE WHERE id=?")
@Where(clause = "deleted = false")
public class Stock extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @NotNull
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;


    public Stock name(String name) {
        this.setName(name);
        return this;
    }

    public Stock currentPrice(BigDecimal currentPrice) {
        this.setCurrentPrice(currentPrice);
        return this;
    }
    public Stock lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }
    public Stock deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Stock) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", currentPrice=" + getCurrentPrice() +
                ", lastUpdate='" + getLastUpdate() + "'" +
                "}";
    }
}
