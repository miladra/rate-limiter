package com.payconiq.stock.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.payconiq.stock.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDTO.class);
        StockDTO stockDTO1 = new StockDTO();
        stockDTO1.setId(1L);
        StockDTO stockDTO2 = new StockDTO();
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
        stockDTO2.setId(stockDTO1.getId());
        assertThat(stockDTO1).isEqualTo(stockDTO2);
        stockDTO2.setId(2L);
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
        stockDTO1.setId(null);
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
    }
}
