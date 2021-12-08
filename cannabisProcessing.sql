/*
 * final.sql
 * 
 *  authors: Logan Hsia, Pouya Zakeri, Austin "Slade" Getz, Clare Garski
 *
 */

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
  Rating DECIMAL(1,1),
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
COPY strains(id, Strain, Rating, Description)
FROM '.\strain_data.csv'
DELIMITER ','
CSV HEADER;

COPY type(id, Type)
FROM '.\type_data.csv'
DELIMITER ','
CSV HEADER;

COPY type_xref(id, Type_id, Strain_id)
FROM '.\type_xref.csv'
DELIMITER ','
CSV HEADER;

COPY effect(id, Effect)
FROM '.\effect_data.csv'
DELIMITER ','
CSV HEADER;

COPY effect_xref(id, Effect_id, Strain_id)
FROM '.\effect_xref.csv'
DELIMITER ','
CSV HEADER;

COPY flavor(id, Flavor)
FROM '.\flavor_data.csv'
DELIMITER ','
CSV HEADER;

COPY flavor_xref(id, Flavor_id, Strain_id)
FROM '.\flavor_xref.csv'
DELIMITER ','
CSV HEADER;
