Query Builder:

A Spring Boot-based service that dynamically connects to MySQL or PostgreSQL databases to generate SQL queries and retrieve table data.

Setup Instructions:

1.Clone the Repository

git clone https://github.com/ramprasadps-tts/tm-query-builder-service

Open it in Spring Tool Suite (STS) or your preferred IDE.

2. Configure application.properties
   
Update DataBase settings (MySQL) in:

src/main/resources/application.properties

4. MySQL Setup
   
    • Start local MySQL
   
    • Create a database named querybuilder
   
CREATE DATABASE querybuilder;
   
    • Create the following table:

 CREATE TABLE db_connection (
  connection_id VARCHAR(255) PRIMARY KEY,
  
  connection_host VARCHAR(255),
  
  connection_db VARCHAR(255),
  
  connection_port VARCHAR(10),
  
  connection_driver VARCHAR(20),
  
  connection_user VARCHAR(100),
  
  connection_password VARCHAR(100),
  
  connection_schemaname VARCHAR(25)
);
 

4.API Endpoints

i).Create Database Connection

POST /querybuilder/database/connection

ii).Fetch Table and Column Details

POST /querybuilder/columndetails/fetchColumnDetails

iii). Fetch Table Data
   
POST /querybuilder/data/fetchResultData

iv). Fetch SQL Query
   
POST /querybuilder/query/fetchQuery

