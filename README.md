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
</ul>