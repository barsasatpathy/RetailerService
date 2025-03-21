# RetailerService
<hr><p>This project is a Spring Boot-based application that calculates and manages customer reward points based on transaction history. Customers earn points based on their purchases, and the service allows you to calculate reward points for each customer on a monthly basis or in total.</p>

## Features
<hr><ul>
<li>Calculate reward points for each transaction based on the amount spent: 2 points for every dollar spent over $100. 1 point for every dollar spent between $50 and $100.</li>
</ul><ul>
<li>Calculate monthly reward points for a given customer based on their transactions.</li>
</ul><ul>
<li>Calculate total reward points for a customer based on all their transactions.</li>
</ul>

##  Project Structure
```
retail-service/
│── src/main/java/com/project/assignment/retailer
│   ├── controller/
│   │   ├── RewardController.java
│   ├── dto/
│   │   ├── CustomerDto.java
│   ├── entity/
│   │   ├── Transaction.java
│   ├── repository/
│   │   ├── TransactionRepository.java
│   ├── service/
│   │   ├── RewardService.java
│   ├── RetailerApplication
│── src/main/resources/
│   ├── application.properties
│── src/main/java/com/project/assignment/retailer
│   ├── controller/
│   │   ├── RewardControllerTest.java
│   ├── service/
│   │   ├── RewardServiceTest.java
│   ├── RetailerApplicationTests
│   ├── RewardControllerIntegrationTest
│── pom.xml
│── README.md

```

## Setup
### Prerequisites To run this project
- Java 8 or later
- Maven (for building the project), 
- An IDE like IntelliJ IDEA or Eclipse (optional), 
- A database (H2 is used in this project by default, but you can configure it for other databases like MySQL or PostgreSQL in application.properties file)

### Steps
<ul>
<li>https://github.com/barsasatpathy/RetailerService.git</li>
</ul><ul>
<li>import project in your IDE</li>
</ul><ul>
<li>mvn clean install</li>
</ul><ul>
<li>run project in your IDE</li>
</ul><ul>
<li>Tomcat started on port(s): 8080 (http) with context path ''</li>
</ul>

## Usages
### API enpoints

- **GET /retail/customer/{customerId}:**  
  This endpoint retrieves reward points for a specific customer. It takes `customerId` as a path parameter and returns `200 OK` or `404 Not Found` if not found.
  this api fetch all transactions of given customerId, calculate reward points for last 3 months from current month and even calculate total reward points.

#### Response:
```json
{
  "customerId": "cust001",
  "pointsPerMonth": {
    "2025-01": 0,
    "2025-03": 250,
    "2025-02": 54
  },
  "totalPoints": 304
}
```