-- Polygon - okruh (toto jsou jen dva čtverece)
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2003, null, null,
    SDO_ELEM_INFO_ARRAY(1,1003,3),
    SDO_ORDINATE_ARRAY(100,5, 140,90)),
    '#000000'); 
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2003, null, null,
    SDO_ELEM_INFO_ARRAY(1,1003,3),
    SDO_ORDINATE_ARRAY(95,90, 145,115)),
    '#000000');      
COMMIT;

-- Lomená čára - linka
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2002, null, null,
    SDO_ELEM_INFO_ARRAY(1,2,1),
    SDO_ORDINATE_ARRAY(125,10, 135,20, 110,80, 125,100)),
    '#000000');
    
--BOD - stanice
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2001, null, null,
    SDO_ELEM_INFO_ARRAY(1,1,1),
    SDO_ORDINATE_ARRAY(125,10)),
    '#000000');
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2001, null, null,
    SDO_ELEM_INFO_ARRAY(1,1,1),
    SDO_ORDINATE_ARRAY(135,20)),
    '#000000');
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2001, null, null,
    SDO_ELEM_INFO_ARRAY(1,1,1),
    SDO_ORDINATE_ARRAY(110,80)),
    '#000000');
INSERT INTO mapa (id, geometrie,barva) 
VALUES (mapa_seq.nextval, 
  SDO_GEOMETRY(2001, null, null,
    SDO_ELEM_INFO_ARRAY(1,1,1),
    SDO_ORDINATE_ARRAY(125,100)),
    '#000000');    
COMMIT;
    
-- Linky, stanice, obvody
INSERT INTO obvod (id,nazev,id_objektu) VALUES (OBVOD_SEQ.nextval,'obvod-A',1);
INSERT INTO obvod (id,nazev,id_objektu) VALUES (OBVOD_SEQ.nextval,'obvod-B',2);
INSERT INTO linka (id,nazev,id_objektu) VALUES (LINKA_SEQ.nextval,'linka-A',3);
INSERT INTO stanice (id,nazev,linka,id_objektu) VALUES (STANICE_SEQ.nextval,'stanice-A',1,4);
INSERT INTO stanice (id,nazev,linka,id_objektu) VALUES (STANICE_SEQ.nextval,'stanice-B',1,5);
INSERT INTO stanice (id,nazev,linka,id_objektu) VALUES (STANICE_SEQ.nextval,'stanice-C',1,6);
INSERT INTO stanice (id,nazev,linka,id_objektu) VALUES (STANICE_SEQ.nextval,'stanice-D',1,7);


COMMIT;