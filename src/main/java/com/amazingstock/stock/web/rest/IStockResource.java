package com.amazingstock.stock.web.rest;

/**
 * Used this interface for moving Swagger annotation into this and spare main controller.
 */

import com.amazingstock.stock.service.dto.StockDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;

public interface IStockResource {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created with body the new stockDTO.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The stock has already an ID.", content = @Content)})
    @Operation(summary = "Create a new stock")
    ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO,
                                                @Parameter(description = "Lang for changing message language. lang[en/nl]")
                                                @RequestParam(required = false, defaultValue = "en") String lang) throws URISyntaxException;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The stock updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The stockDTO is not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "The stockDTO couldn't be updated", content = @Content)})
    @Operation(summary = "Updates an existing stock.")
    ResponseEntity<StockDTO> updateStock(
            @Parameter(description = "Id of stock")
            @PathVariable(value = "id", required = false) Long id,
            @Valid @RequestBody StockDTO stockDTO,
            @Parameter(description = "Using X-Request-Id for avoid repeating request by user. Client must change it in evey request. example: X-Request-Id: ABCD")
            @RequestHeader("X-Request-Id") String requestId,
            @Parameter(description = "Lang for changing message language. lang[en/nl]")
            @RequestParam(required = false, defaultValue = "en") String lang
    ) throws URISyntaxException;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The stock updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The stockDTO is not valid", content = @Content),
            @ApiResponse(responseCode = "404", description = "The stockDTO is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The stockDTO couldn't be updated", content = @Content)})
    @Operation(summary = "Partial updates given fields of an existing stock, field will ignore if it is null")
    ResponseEntity<StockDTO> partialUpdateStock(
            @Parameter(description = "Id of stock")
            @PathVariable(value = "id", required = false) Long id,
            @NotNull @RequestBody StockDTO stockDTO,
            @Parameter(description = "Using X-Request-Id for avoid repeating request by user. Client must change it in evey request. example: X-Request-Id: ABCD")
            @RequestHeader("X-Request-Id") String requestId,
            @Parameter(description = "Lang for changing message language. lang[en/nl]")
            @RequestParam(required = false, defaultValue = "en") String lang
    ) throws URISyntaxException;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the list of stocks in body", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))}),})
    @Operation(summary = "Get All the stocks.")
    ResponseEntity<List<StockDTO>> getAllStocks(@org.springdoc.api.annotations.ParameterObject Pageable pageable,
                                                @Parameter(description = "Lang for changing message language. lang[en/nl]")
                                                @RequestParam(required = false, defaultValue = "en") String lang);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the list of stocks in body", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))}),
            @ApiResponse(responseCode = "404", description = "The stock is not found", content = @Content)})
    @Operation(summary = "Get the stocks.")
    ResponseEntity<StockDTO> getStock(@PathVariable Long id,
                                      @Parameter(description = "Lang for changing message language. lang[en/nl]")
                                      @RequestParam(required = false, defaultValue = "en") String lang);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Content not fount", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StockDTO.class))})})
    @Operation(summary = "Delete the id stock.")
    ResponseEntity<Void> deleteStock(@PathVariable Long id,
                                     @Parameter(description = "Lang for changing message language. lang[en/nl]")
                                     @RequestParam(required = false, defaultValue = "en") String lang);

}
