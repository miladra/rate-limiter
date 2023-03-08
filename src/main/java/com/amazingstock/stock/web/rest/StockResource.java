package com.amazingstock.stock.web.rest;

import com.amazingstock.stock.domain.Stock;
import com.amazingstock.stock.service.StockService;
import com.amazingstock.stock.service.dto.StockDTO;
import com.amazingstock.stock.utility.ResponseUtil;
import com.amazingstock.stock.aop.RateController;
import com.amazingstock.stock.repository.StockRepository;
import com.amazingstock.stock.utility.HeaderUtil;
import com.amazingstock.stock.utility.PaginationUtil;
import com.amazingstock.stock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link Stock}.
 */
@RestController
@RequestMapping("/api")
public class StockResource implements IStockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    @Value("${spring.application.name}")
    private String applicationName;

    private final StockService stockService;

    private final MessageSource messages;

    public StockResource(StockService stockService, StockRepository stockRepository, MessageSource messages) {
        this.stockService = stockService;
        this.messages = messages;
    }

    /**
     * {@code POST  /stocks} : Create a new stock.
     *
     * @param stockDTO the stockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockDTO, or with status {@code 400 (Bad Request)} if the stock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v1/stocks")
    public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO,
                                                @RequestParam(required = false, defaultValue = "en") String lang) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stockDTO);

        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.alreadyhaveid", null, new Locale(lang))), ENTITY_NAME, "id exists");
        }

        Optional<StockDTO> stock = stockService.findByName(stockDTO.getName());
        if (stock.isPresent()) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.alreadyhaveid", null, new Locale(lang))), stock.get().getName(), "stock exists");
        }


        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity
                .created(new URI("/api/stocks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /stocks/:id} : Updates an existing stock.
     *
     * @param id       the id of the stockDTO to save.
     * @param stockDTO the stockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockDTO,
     * or with status {@code 400 (Bad Request)} if the stockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @RateController(rateLimit = 1, parameterName = "X-Request-Id", httpStatusResponse = HttpStatus.OK)
    @PutMapping("/v1/stocks/{id}")
    public ResponseEntity<StockDTO> updateStock(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody StockDTO stockDTO,
            @RequestHeader("X-Request-Id") String requestId,
            @RequestParam(required = false, defaultValue = "en") String lang
    ) throws URISyntaxException {
        log.debug("REST request to update Stock : {}, {}", id, stockDTO);
        isValidStockDTO(id, stockDTO, lang);

        StockDTO result = stockService.update(stockDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /stocks/:id} : Partial updates given fields of an existing stock, field will ignore if it is null
     *
     * @param id       the id of the stockDTO to save.
     * @param stockDTO the stockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockDTO,
     * or with status {@code 400 (Bad Request)} if the stockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @RateController(rateLimit = 1, parameterName = "X-Request-Id", httpStatusResponse = HttpStatus.OK)
    @PatchMapping(value = "/v1/stocks/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<StockDTO> partialUpdateStock(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody StockDTO stockDTO,
            @RequestHeader("X-Request-Id") String requestId,
            @RequestParam(required = false, defaultValue = "en") String lang
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stock partially : {}, {}", id, stockDTO);
        isValidStockDTO(id, stockDTO, lang);

        Optional<StockDTO> result = stockService.partialUpdate(stockDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDTO.getId().toString())
        );
    }


    /**
     * {@code GET  /stocks} : get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stocks in body.
     */
    @GetMapping("/v1/stocks")
    public ResponseEntity<List<StockDTO>> getAllStocks(@org.springdoc.api.annotations.ParameterObject Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "en") String lang) {
        log.debug("REST request to get a page of Stocks");
        Page<StockDTO> page = stockService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stocks/:id} : get the "id" stock.
     *
     * @param id the id of the stockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v1/stocks/{id}")
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id,
                                             @RequestParam(required = false, defaultValue = "en") String lang) {
        log.debug("REST request to get Stock : {}", id);
        Optional<StockDTO> stockDTO = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDTO);
    }

    /**
     * {@code DELETE  /stocks/:id} : delete the "id" stock.
     *
     * @param id the id of the stockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v1/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id,
                                            @RequestParam(required = false, defaultValue = "en") String lang) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }

    void isValidStockDTO(Long id, StockDTO stockDTO, String lang) {
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.alreadyhaveid", null, new Locale(lang))), StockResource.ENTITY_NAME, "idnull");
        }

        if (!Objects.equals(id, stockDTO.getId())) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.alreadyhaveid", null, new Locale(lang))), StockResource.ENTITY_NAME, "idinvalid");
        }

        Optional<StockDTO> stock = stockService.findOne(id);

        if (!stock.isPresent()) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.notfound", null, new Locale(lang))), StockResource.ENTITY_NAME, "idnotfound");
        }


        if (stock.isPresent() && (stock.get().getLastUpdate().equals(stockDTO.getLastUpdate()) || stock.get().getLastUpdate().isAfter(stockDTO.getLastUpdate()))) {
            throw new BadRequestAlertException(String.format(messages.getMessage("stock.alreadyupdated", null, new Locale(lang))), stock.get().getName(), "stock exists");
        }

    }
}