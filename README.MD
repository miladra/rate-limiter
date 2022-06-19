# Stock Api
   Java-based backend application that implements Below RESTFUL APIs for Stock Management.

## API spec
   - GET /api/stocks (get a list of stocks)
   - POST /api/stocks (create a stock)
   - GET /api/stocks/1 (get one stock from the list)
   - PATCH /api/stocks/1 (update the price of a single stock)
   - DELETE/api/stocks/1 (delete a single stock)

## Stack 
   * Spring Boot
   * Java 11
   * Spring Framework
   * Apache-maven-3.8.5
   * postgres:14.2
   * Swagger
   * Docker and docker-compose
   * Lombok
   * AspectJ

## Testing
   * Junit, Spring Boot, H2database, Mockito
  
# Thought of Process


   - I assumed creating two stock with same name is not allowed.
   - I added PUT method to update the stock, and PATCH method to partially update the stock.
   - I thought that multi-client updates would be possible. When a client retries a request due to network issues, the retried message may be written twice, causing other updates to be lost. That's why I assumed:
      - First: the request's update time is longer than the last update time in the database.
      - Second: a request must be executed exactly once (Idempotent). In order to do this I provide the X-Request-Id header for updating price due to implementing idempotency.
        - I Used a Caffeine as a cache for storing X-Request-Id to handle idempotency. X-Request-Id-X-RateLimit-Remaining , X-Request-Id-X-RateLimit-Limit added to the response header.
   - I implemented soft delete to history tracking or audit and also related to reference integrity.
   - The application supports 2 language (nl, en) as a query param. ?lang=en OR lang=nl
   - Add JMX Stock-XManagement for managing Application with JMX, like clear Caffeine cache.

### initial data

   - Liquebase will create the initial list of stocks in the dev profile
   
### Build and run the application standalone

   - For opening in ide please install suitable Lombok plugin for your IDE.

   - In order to run tha application you can use local database or you can run postgres by below command
    
       ```
       docker-compose -f docker/postgresql.yml up
       
       ```
    
   - Clone the repository

        ```
        git clone <repository url>
        
        ```
        
   - Build by MAVEN
    
        ```
         mvn clean install
         
        ```
   OR
   - Run by spring boot
     
        ```
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
           
        ```   

### Run app with docker-compose

   - In root directory
   
   ```
   mvn clean install

   docker-compose -f app.yml up
   ```

## Postman:

   - You can find Postman file in postman folder in root of project, For each API call
   
## Code quality

   - Code quality can be analyzed using SonarQube
   - I specific Docker Compose configuration for Sonar
    
   ```
   docker-compose -f docker/sonar.yml up -d
    
   ```
   - SonarQube default login and assword is "admin"
   - You need to get token from: in right top of sonarQube pannel -> User(Administrator) > My Account > Security > Generate Tokens
   - Then you can run below command by maven
    
   ```
   mvn sonar:sonar -Dsonar.host.url=http://localhost:9001 -Dsonar.login='GeneratedToken'
    
   ```
  
## API documentation

   http://localhost:8090/swagger-ui/index.html

## Development Strategy

   - I have followed the Git Flow that is called Release branches , I borrowed it from [link](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow#Release%20branches)

   ![Git Flow](https://wac-cdn.atlassian.com/dam/jcr:8f00f1a4-ef2d-498a-a2c6-8020bb97902f/03%20Release%20branches.svg)

   - Using a dedicated branch to prepare releases makes it possible for one team to polish the current release while another team continues working on features for the next release
   - **I have left open the Pull Requests from branches for simplicity**
### Future improvements
   * Upgrade to Java 16
   * Add Integration tests with continerized database, Architecture tests, End to END tests
   * Add a security layer such as JWT
   * Consider concurrency topics. adding versioning to handle optimistic locking
   * Create deployment pipelines to build and deploy the artifact into different environments
   * Manage server-side error page,
   * Enable or Disable Swager between dev or prod profile

###### By: Milad Ranjbari
