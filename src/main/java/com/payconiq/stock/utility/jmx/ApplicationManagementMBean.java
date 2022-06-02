package com.payconiq.stock.utility.jmx;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 */

@Component
@ManagedResource(objectName = "stock-xmanamgement:name=stock-XManagement", description = "Stock Management Bean")
@Slf4j
public class ApplicationManagementMBean {

    private final Cache<String, Integer> cache;

    public ApplicationManagementMBean(Cache<String, Integer> cache) {
        this.cache = cache;
    }

    @ManagedOperation(description = "#Clearing Caffeine Cache")
    public void clearCaffeineCache() {
        cache.invalidateAll();
        log.info("#Caffeine Cache Cleared.");
    }
}
