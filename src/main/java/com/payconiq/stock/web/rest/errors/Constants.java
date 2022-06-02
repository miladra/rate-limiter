package com.payconiq.stock.web.rest.errors;

/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 */
public interface Constants {
    String SPRING_PROFILE_DEVELOPMENT = "dev";
    String SPRING_PROFILE_TEST = "test";
    String SPRING_PROFILE_E2E = "e2e";
    String SPRING_PROFILE_PRODUCTION = "prod";
    String SPRING_PROFILE_CLOUD = "cloud";
    String SPRING_PROFILE_HEROKU = "heroku";
    String SPRING_PROFILE_AWS_ECS = "aws-ecs";
    String SPRING_PROFILE_AZURE = "azure";
    String SPRING_PROFILE_API_DOCS = "api-docs";
    String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";
    String SPRING_PROFILE_K8S = "k8s";
}
