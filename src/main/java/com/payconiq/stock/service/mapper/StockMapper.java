package com.payconiq.stock.service.mapper;

import com.payconiq.stock.domain.Stock;
import com.payconiq.stock.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {}
