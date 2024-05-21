## README

## Start up

- mvn spring-boot:run
- Run src/main/resources/data-h2.sql for data initialization

## Note

Endpoints are authenticated using Basic Auth

- There are two testing accounts, test1 and test2. Both with password test1234

GET /accounts
Get all accounts with balance for particular user.
Example
curl --location 'http://localhost:8080/accounts' \
--header 'Authorization: Basic dGVzdDI6dGVzdDEyMzQ=' \
--header 'Cookie: JSESSIONID=934F5EC7B33DE7AB8F981D4E0913BA9A;
XSRF-TOKEN=18ef4872-2807-4058-bae9-f328a2ce3e1c'

POST /account/{accountNumber}/transfer
Create transfer request to transfer money from one account to another
curl --location 'http://localhost:8080/accounts' \
--header 'Authorization: Basic dGVzdDI6dGVzdDEyMzQ=' \
--header 'Cookie: JSESSIONID=934F5EC7B33DE7AB8F981D4E0913BA9A;
XSRF-TOKEN=18ef4872-2807-4058-bae9-f328a2ce3e1c'

## Future Improvement

- Switch to other authentication / authorization approach, e.g. JWT
- Adopt some architecture like hexagonal to better split classes for better testability,
  maintainability
- Create test fixture to facilitate code reuse
- Use BCryptEncoder
