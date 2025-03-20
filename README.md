<h1>RetailerService</h1>
<hr><p>This project is a Spring Boot-based application that calculates and manages customer reward points based on transaction history. Customers earn points based on their purchases, and the service allows you to calculate reward points for each customer on a monthly basis or in total.</p><h2>Features</h2>
<hr><ul>
<li>Calculate reward points for each transaction based on the amount spent: 2 points for every dollar spent over $100. 1 point for every dollar spent between $50 and $100.</li>
</ul><ul>
<li>Calculate monthly reward points for a given customer based on their transactions.</li>
</ul><ul>
<li>Calculate total reward points for a customer based on all their transactions.</li>
</ul><h2>Setup</h2>
<hr><p>Prerequisites
To run this project, you need:</p>
<p>Java 8 or later,
Maven (for building the project),
An IDE like IntelliJ IDEA or Eclipse (optional),
A database (H2 is used in this project by default, but you can configure it for other databases like MySQL or PostgreSQL)</p><h5>Steps</h5><ul>
<li>https://github.com/barsasatpathy/RetailerService.git</li>
</ul><ul>
<li>import project in your IDE</li>
</ul><ul>
<li>mvn clean install</li>
</ul><ul>
<li>run project in your IDE</li>
</ul><ul>
<li>Tomcat started on port(s): 8080 (http) with context path ''</li>
</ul><h2>Usage</h2>
<hr><p>API enpoints</p><h5>Code Examples</h5><ul>
<li>POST /retail/transaction : This endpoint is used to record a single transaction and calculate the associated reward points for a customer. It takes customerId, transactionAmount, transactionDate as parameter and return 201 created or 500 internal server error.</li>
</ul><p><code>Request: {   "customerId": "cust001",   "transactionAmount": 120.0,   "transactionDate": "2025-03-15" }</code></p><ul>
<li>POST /retail/transactions : This endpoint allows you to process multiple transactions for different customers at once and calculate the corresponding reward points. It takes list of (customerId, transactionAmount, transactionDate) as transaction parameter and return 201 created or 500 internal server error.</li>
</ul><p><code>Request:[     {         "customerId": "cust002",         "transactionAmount": 40,         "transactionDate": "2025-01-15"     },     {         "customerId": "cust002",         "transactionAmount": 102, "transactionDate": "2025-05-15"     } ]</code></p><ul>
<li>GET /retail/rewards/{customerId}: This endpoint retrieves the list of reward points for a specific customer. It takes customerId as path param and return 200 0r 400.</li>
</ul><p><code>Response : [   {     "customerId": "cust001",     "monthYear": "2025-03",     "rewardPoints": 90   },   {     "customerId": "cust001",     "monthYear": "2025-04",     "rewardPoints": 100   } ]</code></p><ul>
<li>GET /retail/rewards/all: This endpoint retrieves the list of reward points for all customers. It returns 200 0r 400.</li>
</ul><p><code>Response: [   {     "customerId": "cust001",     "monthYear": "2025-03",     "rewardPoints": 90   },   {     "customerId": "cust002",     "monthYear": "2025-03",     "rewardPoints": 110   } ]</code></p><ul>
<li>GET /retail/rewards/total/{customerId}: This endpoint retrieves the total reward points accumulated by a specific customer across all transactions and months.It takes customerId as path param and return total reward point as integer.</li>
</ul><p><code>190</code></p>