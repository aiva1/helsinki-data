CREATE TABLE reports
(
  id serial NOT NULL,
  fips INT,
  admin2 CHARACTER(50),
  province_state CHARACTER(50),
  country_region CHARACTER(50),
  last_update TIMESTAMP,
  lat CHARACTER(50),
  long CHARACTER(50),
  confirmed INT,
  deaths INT,
  recovered INT,
  active INT,
  combined_key CHARACTER(100),
  CONSTRAINT reports_pkey PRIMARY KEY (id)
);

--- Note: Use PgAdmin to data from csv file to the table. Below code cannot will work only if server has access to file.
COPY reports
FROM 'resources/covid/04-04-2020.csv' DELIMITER ',' CSV HEADER;
