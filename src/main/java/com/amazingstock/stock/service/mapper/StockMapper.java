package com.amazingstock.stock.service.mapper;

import com.amazingstock.stock.service.dto.StockDTO;
import com.amazingstock.stock.domain.Stock;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {}
