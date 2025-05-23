Query Builder:

A Spring Boot-based service that dynamically connects to MySQL or PostgreSQL databases to generate SQL queries and retrieve table data.

Setup Instructions:

1.Clone the Repository

git clone https://github.com/ramprasadps-tts/tm-query-builder-service

Open it in Spring Tool Suite (STS) or your preferred IDE.

2. Configure application.properties
Update DataBase settings (MySQL) in:
src/main/resources/application.properties

3. MySQL Setup
    • Start local MySQL
   
    • Create a database named querybuilder
   
    • Create the following table:
   
Table: db_connection

Columns:
	connection_id	varchar(255) PK
 
	connection_host	varchar(255)
 
	connection_db	varchar(255)
 
	connection_port	varchar(10)
 
	connection_driver	varchar(20)
 
	connection_user	varchar(100)
 
	connection_password	varchar(100)
 
	connection_schemaname	varchar(25)
 

API Endpoints

1.Create Database Connection

POST /querybuilder/database/connection

2.Fetch Table and Column Details

POST /querybuilder/columndetails/fetchColumnDetails

3. Fetch Table Data
4. 
POST /querybuilder/data/fetchResultData

5. Fetch SQL Query
6. 
POST /querybuilder/query/fetchQuery

