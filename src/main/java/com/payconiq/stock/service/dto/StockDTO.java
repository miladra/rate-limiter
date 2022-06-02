package com.payconiq.stock.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payconiq.stock.domain.Stock} entity.
 */

@Getter  @Setter
public class StockDTO implements Serializable {

  private Long id;


  @NotNull
  private String name;

  @NotNull
  private Double currentPrice;

  private Instant lastUpdate;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StockDTO)) {
      return false;
    }

    StockDTO stockDTO = (StockDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, stockDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "StockDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", currentPrice=" + getCurrentPrice() +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
  }
}
