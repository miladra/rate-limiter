package com.payconiq.stock.utility;


/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 * Extracted from JHipster
 */


import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

public final class SpringLiquibaseUtil {
    private SpringLiquibaseUtil() {
    }

    public static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(liquibaseDataSource);
            return liquibase;
        } else {
            SpringLiquibase liquibase = new DataSourceClosingSpringLiquibase();
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
            return liquibase;
        }
    }

    public static AsyncSpringLiquibase createAsyncSpringLiquibase(Environment env, Executor executor, DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            liquibase.setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
        } else {
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
        }

        return liquibase;
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        } else {
            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
        }
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        Objects.requireNonNull(liquibaseProperties);
        Supplier var10000 = liquibaseProperties::getUrl;
        Objects.requireNonNull(dataSourceProperties);
        String url = getProperty(var10000, dataSourceProperties::determineUrl);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getUser;
        Objects.requireNonNull(dataSourceProperties);
        String user = getProperty(var10000, dataSourceProperties::determineUsername);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getPassword;
        Objects.requireNonNull(dataSourceProperties);
        String password = getProperty(var10000, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        return (String)Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
    }
}

