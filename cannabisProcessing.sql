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
DROP TABLE IF EXISTS effects CASCADE;
DROP TABLE IF EXISTS effect_xref CASCADE;
DROP TABLE IF EXISTS flavors CASCADE;
DROP TABLE IF EXISTS flavor_xref CASCADE;


-- Table Creation

CREATE TABLE strain (
  id SERIAL,
  Strain TEXT ,
  Rating DECIMAL(2, 1),
  Description TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE type (
  id SERIAL,
  Type TEXT ,
  PRIMARY KEY (id)
);


CREATE TABLE type_xref (
  Type_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Type_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Type_id) REFERENCES type(id)
);

CREATE TABLE effects (
  id SERIAL,
  Effect TEXT ,
  PRIMARY KEY (id)
);



CREATE TABLE effect_xref (
  Effect_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Effect_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Effect_id) REFERENCES effects(id)
);

CREATE TABLE flavors (
  id SERIAL,
  Flavor TEXT ,
  PRIMARY KEY (id)
);

CREATE TABLE flavor_xref (
  Flavor_id INTEGER,
  Strain_id INTEGER,
  PRIMARY KEY (Flavor_id, Strain_id),
  FOREIGN KEY (Strain_id) REFERENCES strain(id),
  FOREIGN KEY (Flavor_id) REFERENCES flavors(id)
);

-- Copying CSV files to SQL Database
\copy strain FROM './strain_data.csv' DELIMITER ',' CSV
\copy type FROM './type_data.csv' DELIMITER ',' CSV
\copy type_xref FROM './type_xref.csv' DELIMITER ',' CSV
\copy effects FROM './effect_data.csv' DELIMITER ',' CSV
\copy effect_xref FROM './effect_xref.csv' DELIMITER ',' CSV
\copy flavors FROM './flavor_data.csv' DELIMITER ',' CSV
\copy flavor_xref FROM './flavor_xref.csv' DELIMITER ',' CSV


