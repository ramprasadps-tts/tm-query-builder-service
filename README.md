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

Mysql
body:
{
  "connectionHost": "localhost",
  "connectionPort": 3306,
  "connectionDriver": "mysql",
  "databaseUser": "root",
  "databasePassword": "password",
  "databaseName": "querybuilder"
}

Postgres:
body:
{
"connectionHost": "localhost",
"connectionPort": 5432,
"connectionDriver": "postgresql",
"databaseUser": "postgres",
"databasePassword": "postgres",
"databaseName": "Query",
"schemaName":"public"
}

response:
{
  "message": "Connected Database",
  "responseData": "<connection-id>",
  "isSuccess": true
}

2.Fetch Table and Column Details
POST /querybuilder/columndetails/fetchColumnDetails
body:
{
  "connectionId": "<connection-id>"
}

response:
{
"message": "Table Details of the Schema",
"responseData": {
"Persons": {
"column": {
"PersonID": "int",
"Name": "varchar"
},
"primarykey": null,
"foreignKeys": null
},
"db_connection": {
"column": {
"connection_host": "varchar",
"connection_user": "varchar",
"connection_password": "varchar",
"connection_id": "varchar",
"connection_db": "varchar",
"connection_schemaname": "varchar",
"connection_driver": "varchar",
"connection_port": "varchar"
},
"primarykey": "connection_id",
"foreignKeys": null
}
},
"isSuccess": true
}

3. Fetch Table Data
POST /querybuilder/data/fetchResultData
body:
{
  "connectionId": "<connection-id>",
  "requestData": {
    "tableName": "Persons",
    "columnNames": ["PersonID", "Name"],
    "limit": 10,
    "pageNo": 1
  }
}

response:
{
"message": "Data for the Request",
"responseData": {
"filterResponse": [
{
"PersonID": 1,
"Name": "ravi"
},
{
"rowCount": 1
}
]
},
"isSuccess": true
}

4. Fetch SQL Query
POST /querybuilder/query/fetchQuery
body:
{
"connectionId": "<connection-id>",
"requestData": {
"tableName": "Persons",
"columnNames": ["PersonID", "Name"],
"orderBy": [
{
"orderColumnName": "Name",
"orderType": "ASC"
}
],
"limit": 10,
"pageNo": 1
}
}

response:
{
"message": "Data for the Request",
"responseData": {
"query": "SELECT PersonID,Name FROM querybuilder.Persons ORDER BY Name ASC LIMIT 10 OFFSET 0"
},
"isSuccess": true
}

