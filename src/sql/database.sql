DROP INDEX mapa_index;
DROP INDEX fotografie_index;

DROP TABLE fotografie;
DROP TABLE incident;
DROP TABLE stanice;
DROP TABLE linka;
DROP TABLE obvod;
DROP TABLE mapa;

DROP PROCEDURE rotate;

--==========================================
-- mapa (obvody, linky, stanice)
--==========================================
DROP SEQUENCE "MAPA_SEQ";
CREATE SEQUENCE  "MAPA_SEQ"   INCREMENT BY 1 START WITH 1;

CREATE TABLE mapa (
    id NUMBER NOT null,
    geometrie SDO_GEOMETRY,
    barva VARCHAR(6) NOT null,
    CONSTRAINT pk_mapa PRIMARY KEY (id)
);

-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'MAPA' AND COLUMN_NAME = 'GEOMETRIE';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'mapa', 'geometrie',
	-- souradnice X,Y s hodnotami 0-300 a presnosti 0.1 bod (velikost mrizky a hustota budu v planu z prikladu, napr. kulate rohy komunikace s presnosti 1 bod a stromy v zeleni s presnosti 0.1 bod)
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 550, 0.1), SDO_DIM_ELEMENT('Y', 0, 550, 0.1)),
	-- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
	NULL
);

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT id, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 0.1) valid -- 0.1=presnost
FROM mapa;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.id, m.geometrie.ST_IsValid()
FROM mapa m;

--==========================================
-- linky
--==========================================
DROP SEQUENCE "LINKA_SEQ";
CREATE SEQUENCE  "LINKA_SEQ"   INCREMENT BY 1 START WITH 1;

CREATE TABLE linka (
    id NUMBER NOT null,
    nazev VARCHAR(32),
    id_objektu NUMBER NOT null,
    CONSTRAINT pk_linka PRIMARY KEY (id),
    CONSTRAINT fk_linka_mapa FOREIGN KEY (id_objektu) REFERENCES mapa(id) on delete cascade
);

COMMIT;


--==========================================
-- stanice
--==========================================
DROP SEQUENCE "STANICE_SEQ";
CREATE SEQUENCE  "STANICE_SEQ"   INCREMENT BY 1 START WITH 1;

CREATE TABLE stanice (
    id NUMBER NOT null,
    nazev VARCHAR(32),
    id_objektu NUMBER not null,
    CONSTRAINT pk_stanice PRIMARY KEY (id),
    CONSTRAINT fk_stanice_mapa FOREIGN KEY (id_objektu) REFERENCES mapa(id) on delete cascade
);

COMMIT;


--==========================================
-- obvod
--==========================================
DROP SEQUENCE "OBVOD_SEQ";
CREATE SEQUENCE  "OBVOD_SEQ"   INCREMENT BY 1 START WITH 1;

CREATE TABLE obvod (
    id NUMBER NOT null,
    nazev VARCHAR(32),
    id_objektu NUMBER NOT null,
    CONSTRAINT pk_obvod PRIMARY KEY (id),
    CONSTRAINT fk_obvod_mapa FOREIGN KEY (id_objektu) REFERENCES mapa(id) on delete cascade
);

COMMIT;

--==========================================
-- incident
--==========================================
DROP SEQUENCE "INCIDENT_SEQ";
CREATE SEQUENCE  "INCIDENT_SEQ"   INCREMENT BY 1 START WITH 1;

CREATE TABLE incident (
    id NUMBER NOT null,
    stanice NUMBER NOT null,
    udalost VARCHAR(32) NOT null,
    zpozdeni NUMBER,
    popis VARCHAR(1000),
    datum DATE NOT null,
    pricina VARCHAR(32) NOT null,
    zraneni VARCHAR(32) NOT null,
    pohlavi VARCHAR(32) NOT null,
    vek NUMBER NOT null,
    CONSTRAINT pk_incident PRIMARY KEY (id),
    CONSTRAINT fk_incident_stanice FOREIGN KEY (stanice) REFERENCES stanice(id) on delete cascade
);

--==========================================
-- fotografie
--==========================================
DROP SEQUENCE "FOTOGRAFIE_SEQ";
CREATE SEQUENCE "FOTOGRAFIE_SEQ" START WITH 1 INCREMENT BY 1;

CREATE TABLE fotografie (
  	id NUMBER NOT null,
  	incident NUMBER NOT null,
    foto ORDSYS.ORDImage,
    foto_si ORDSYS.SI_StillImage,
    foto_ac ORDSYS.SI_AverageColor,
    foto_ch ORDSYS.SI_ColorHistogram,
    foto_pc ORDSYS.SI_PositionalColor,
    foto_tx ORDSYS.SI_Texture,
    CONSTRAINT pk_fotografie PRIMARY KEY (id),
    CONSTRAINT fk_fotografie_incident FOREIGN KEY (incident) REFERENCES incident(id) on delete cascade
);

COMMIT;

CREATE INDEX mapa_index ON mapa (geometrie) INDEXTYPE IS MDSYS.SPATIAL_INDEX;
CREATE INDEX fotografie_index ON fotografie (foto_si) INDEXTYPE IS ordsys.ordimageindex;

--==========================================
-- PROCEDURY
--==========================================
create or replace PROCEDURE rotate (image_id NUMBER) AS
  image ORDSYS.ORDIMAGE;
BEGIN
  SELECT foto INTO image FROM fotografie WHERE id = image_id FOR UPDATE;
  ORDSYS.ORDIMAGE.process(image,'rotate=90');
  
EXCEPTION
  WHEN OTHERS THEN
    RAISE;
END;
/

COMMIT;