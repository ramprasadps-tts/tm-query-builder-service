USE querybuilder;
CREATE TABLE IF NOT EXISTS db_connection (
  connection_id VARCHAR(255) PRIMARY KEY,
  connection_host VARCHAR(255),
  connection_db VARCHAR(255),
  connection_port VARCHAR(10),
  connection_driver VARCHAR(20),
  connection_user VARCHAR(100),
  connection_password VARCHAR(100),
  connection_schemaname VARCHAR(25)
);
