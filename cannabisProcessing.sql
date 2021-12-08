/*
 * final.sql
 * 
 *  authors: Logan Hsia, Pouya Zakeri, Austin "Slade" Getz, Clare Garski
 *
 */

-- Setting user to admin

DROP TABLE IF EXISTS strain CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE IF EXISTS type_xref CASCADE;
DROP TABLE IF EXISTS effect CASCADE;
DROP TABLE IF EXISTS effect_xref CASCADE;
DROP TABLE IF EXISTS flavor CASCADE;
DROP TABLE IF EXISTS flavor_xref CASCADE;


-- Table Creation

CREATE TABLE strain (
  id SERIAL,
  Strain VARCHAR(50),
  Rating TEXT,
  Description TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE type (
  id SERIAL,
  Type TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE type_xref (
  id SERIAL,
  Type_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Type_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Type_id) REFERENCES type(id)
);

CREATE TABLE effect (
  id SERIAL,
  Effect TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE effect_xref (
  id SERIAL,
  Effect_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Effect_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Effect_id) REFERENCES effect(id)
);

CREATE TABLE flavor (
  id SERIAL,
  Flavor TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE flavor_xref (
  id SERIAL,
  Flavor_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Flavor_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Flavor_id) REFERENCES flavor(id)
);

-- Copying CSV files to SQL Database
\copy strain FROM './strain_data.csv' DELIMITER ',' CSV
\copy type FROM './type_data.csv' DELIMITER ',' CSV
\copy type_xref FROM './type_xref.csv' DELIMITER ',' CSV
\copy effect FROM './effect_data.csv' DELIMITER ',' CSV
\copy effect_xref FROM './effect_xref.csv' DELIMITER ',' CSV
\copy flavor FROM './flavor_data.csv' DELIMITER ',' CSV
\copy flavor_xref FROM './flavor_xref.csv' DELIMITER ',' CSV

