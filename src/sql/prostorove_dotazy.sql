--Vypíše linky, které se nacházejí v daném obvodu
SELECT O.id, L.id FROM mapa O
JOIN mapa L ON I.id <> L.id
JOIN linka I ON I.id_objektu = L.id
WHERE (O.id = 2) AND SDO_GEOM.RELATE(O.geometrie, 'contains+overlapbdydisjoint',  L.geometrie, 1) <> 'FALSE'
ORDER BY L.id ASC;

--Vypíše stanice v daném obvodu
SELECT O.id, L.id FROM mapa O
JOIN mapa L ON O.id <> L.id
JOIN stanice S ON S.id_objektu = L.id
WHERE (O.id = 2) AND SDO_GEOM.RELATE(O.geometrie, 'contains+inside',  L.geometrie, 1) <> 'FALSE'
ORDER BY L.id ASC;

--Vypíše sousední obvody
SELECT M.id, O1.nazev as Nazev, N.id as id_soused, O2.nazev as Soused, M.geometrie, N.geometrie
FROM mapa M
JOIN mapa N ON M.id <> N.id
JOIN obvod O1 ON O1.id_objektu = M.id
JOIN obvod O2 ON O2.id_objektu = N.id
WHERE (O1.id = 2) AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE'
ORDER BY N.id ASC;

--Vypíše pouze názvy sousedních obvodů
--Vypíše sousední obvody
SELECT LISTAGG(O2.nazev, ',') WITHIN GROUP(ORDER BY O2.nazev) as Nazev_souseda
FROM mapa M
JOIN mapa N ON M.id <> N.id
JOIN obvod O1 ON O1.id_objektu = M.id
JOIN obvod O2 ON O2.id_objektu = N.id
WHERE (O1.id = 2) AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE'
ORDER BY N.id ASC;

--Vypíše Stanice na dané lince
SELECT M.id as ID_objektu_linka, N.id AS ID_objektu_stanice, S.id as ID_stanice, S.nazev
FROM mapa M
JOIN mapa N ON M.ID <> N.id
JOIN linka L ON M.id = L.id_objektu
JOIN stanice S ON N.id = S.id_objektu
WHERE (L.id = 1) AND SDO_GEOM.RELATE(M.geometrie, 'touch+inside+contains',  N.geometrie, 1) <> 'FALSE'
ORDER BY L.id ASC;

--Získá název obvodu z ID
SELECT M.id, M.barva, O.id as id_obvodu, O.nazev AS Nazev_obvodu
FROM mapa M
JOIN obvod O ON M.id = O.id_objektu
WHERE M.id = 50;

--Získá název linky z ID
SELECT M.id, M.barva, L.id as id_linky, L.nazev AS Nazev_linky
FROM mapa M
JOIN linka L ON M.id = L.id_objektu
WHERE M.id = 52;

--Získá název stanice z ID
SELECT M.id, M.barva, S.id as id_stanice, S.nazev AS Nazev_stanice
FROM mapa M
JOIN stanice S ON M.id = S.id_objektu
WHERE M.id = 53;

--Získání barvy a geometrie obvodu 
Select M.id, O.nazev, O.id AS id_objektu, M.barva, M.geometrie
FROM mapa M
JOIN obvod O ON M.id = O.id_objektu;

--Získání barvy a geometrie linky
Select M.id, L.nazev, L.id AS id_objektu, M.barva, M.geometrie
FROM mapa M
JOIN linka L ON M.id = L.id_objektu;

--Získání barvy a geometrie stanice
Select M.id, S.nazev, S.id AS id_objektu, M.barva, M.geometrie
FROM mapa M
JOIN stanice S ON M.id = S.id_objektu;

--Vypíše linku, na které se nachází daná stanice
SELECT M.id AS id_objektu_stanice, S.nazev AS Nazev_linky, L.id AS id_linky, L.nazev AS Nazev_linky, N.geometrie
FROM mapa M
JOIN mapa N ON M.id <> N.id
JOIN linka L ON L.id_objektu = N.id
JOIN stanice S ON S.id_objektu = M.id
WHERE S.id = 1 AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE';
-----------------------------------
--DOTAZY předpripravené
--Vrátí délku linky
SELECT M.id, L.id as id_objektu, L.nazev, SDO_GEOM.SDO_LENGTH(M.geometrie,1,null) as Delka
FROM mapa M
JOIN linka L ON M.id = L.id_objektu
WHERE M.id = 52;

--Vrátí obvod s nejvíce sousedy
SELECT id_obvodu, Pocet
FROM
  (SELECT M.id as id_obvodu, O1.nazev,COUNT(M.id) AS Pocet
  FROM mapa M
  JOIN mapa N ON M.id <> N.id
  JOIN obvod O1 ON O1.id_objektu = M.id
  JOIN obvod O2 ON O2.id_objektu = N.id
  WHERE (M.id = 50) AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE'
  GROUP BY M.id, O1.nazev
  ORDER BY Pocet DESC)
WHERE ROWNUM <= 1;

-- Vrátí počet sousedních obvodů
SELECT M.id, O1.nazev,COUNT(M.id) AS Pocet
FROM mapa M
JOIN mapa N ON M.id <> N.id
JOIN obvod O1 ON O1.id_objektu = M.id
JOIN obvod O2 ON O2.id_objektu = N.id
WHERE (M.id = 50) AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE'
GROUP BY M.id, O1.nazev;

--Vrátí vzdálenost dvou bodů
SELECT M.id, S1.nazev, N.id, S2.nazev, (SDO_GEOM.SDO_DISTANCE(M.geometrie, N.geometrie, 1, null)) as Vzdalenost
FROM mapa M
JOIN Mapa N ON M.id <> N.id
JOIN stanice S1 ON M.id = S1.id_objektu
JOIN stanice S2 ON N.id = S2.id_objektu
WHERE M.id = 53 AND N.id = 54;

--Vrátí 3 nejvíce nehodové stanice
SELECT id_stanice, nazev_stanice, Pocet
FROM
  (SELECT S.id AS id_stanice, S.nazev AS nazev_stanice, COUNT(S.id) as Pocet, S.id_objektu AS id_objektu
  FROM stanice S
  JOIN incident I ON S.id = I.stanice
  GROUP BY S.id, S.nazev, S.id_objektu
  ORDER BY Pocet DESC)
--JOIN mapa M ON M.id = id_objektu
WHERE ROWNUM <= 1;

--Vrátí přestupní stanice (dodělat)
/*SELECT SDO_GEOM.SDO_INTERSECTION(M.geometrie, N.geometrie,0.5)
FROM mapa M
JOIN mapa N ON M.id <> N.id
WHERE M.id = 73 AND N.id = 52;*/ -- to blbne jaksi
SELECT M.id, M.geometrie, S.id, S.nazev as nazev
FROM mapa M
JOIN stanice S ON M.id = S.id_objektu
JOIN stanice I ON M.id = I.id_objektu
WHERE S.nazev = I.nazev AND S.id <> I.id;

--Vrátí délku linky v daném obvodě
SELECT M.id, O.id AS id_obvodu, O.nazev AS Nazev_obvodu, L.id AS id_linky, L.nazev as Nazev_linky, SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(M.geometrie, N.geometrie,0.5),1,null) as Delka_linky
FROM mapa M
JOIN mapa N ON M.id <> N.id
JOIN obvod O ON O.id_objektu = M.id
JOIN linka L ON L.id_objektu = N.id
WHERE M.id = 51 AND N.ID = 52;

--Vrátí obvod s nejdelší linkou
SELECT id, geometrie, id_obvodu, Nazev_obvodu, id_linky, Nazev_linky, Delka_linky
FROM
  (SELECT M.id AS id, M.geometrie as geometrie, O.id AS id_obvodu, O.nazev AS Nazev_obvodu, L.id AS id_linky, L.nazev as Nazev_linky, SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(M.geometrie, N.geometrie,0.5),1,null) as Delka_linky
  FROM mapa M
  JOIN mapa N ON M.id <> N.id
  JOIN obvod O ON O.id_objektu = M.id
  JOIN linka L ON L.id_objektu = N.id
  WHERE N.id = 52
  ORDER BY Delka_linky DESC)
WHERE ROWNUM <= 1;

--Obvod s nejvíce stanicemi
SELECT * 
FROM 
  (SELECT M.id, O.nazev Obvod, COUNT(S.nazev) Pocet
  FROM mapa M
  JOIN mapa N ON N.id <> M.id
  JOIN stanice S ON S.id_objektu = N.id
  JOIN obvod O ON M.id = O.id_objektu
  WHERE SDO_GEOM.RELATE(M.geometrie, 'contains+inside',  N.geometrie, 1) <> 'FALSE'
  GROUP BY M.id, O.nazev
  ORDER BY Pocet DESC)
WHERE ROWNUM <= 1;

--Linka s nejvíce stanicemi
SELECT * 
FROM 
  (SELECT M.id, L.nazev Linka, COUNT(S.nazev) Pocet
  FROM mapa M
  JOIN mapa N ON N.id <> M.id
  JOIN stanice S ON S.id_objektu = N.id
  JOIN linka L ON M.id = L.id_objektu
  WHERE SDO_GEOM.RELATE(M.geometrie, 'touch+contains+inside',  N.geometrie, 1) <> 'FALSE'
  GROUP BY M.id, L.nazev
  ORDER BY Pocet DESC)
WHERE ROWNUM <= 1;

--Obvod s nejvíce incidenty
SELECT *
FROM
  (SELECT M.id, O.nazev, COUNT(I.id) AS Pocet
  FROM mapa M
  JOIN obvod O ON M.id = O.id_objektu
  JOIN mapa N ON M.id <> N.id
  JOIN stanice S ON N.id = S.id_objektu
  JOIN incident I ON I.stanice = S.id
  WHERE SDO_GEOM.RELATE(M.geometrie, 'contains+inside',  N.geometrie, 1) <> 'FALSE'
  GROUP BY M.id, O.nazev
  ORDER BY Pocet DESC)
WHERE ROWNUM <= 1;

--Linka s nejvíce incidenty
SELECT *
FROM
  (SELECT M.id, L.nazev, COUNT(I.id) AS Pocet
  FROM mapa M
  JOIN linka L ON M.id = L.id_objektu
  JOIN mapa N ON M.id <> N.id
  JOIN stanice S ON N.id = S.id_objektu
  JOIN incident I ON I.stanice = S.id
  WHERE SDO_GEOM.RELATE(M.geometrie, 'touch+inside+contains',  N.geometrie, 1) <> 'FALSE'
  GROUP BY M.id, L.nazev
  ORDER BY Pocet DESC)
WHERE ROWNUM <= 1;

--Délka metra
SELECT SUM(Delka)
FROM
(SELECT M.id, L.id as id_linky, L.nazev, SDO_GEOM.SDO_LENGTH(M.geometrie,1,null) as Delka
FROM linka L
JOIN mapa M ON L.id_objektu = M.id);

-- Dotaz pro souhrné informace o stanici
SELECT 
	O.id O_id, S.id S_id,
	S.nazev "S_nazev",
	L.nazev "Na lince",
	O.nazev "Dostupná v obvodu",
	L.nazev "Dostupné linky", 
	ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) AS "Délka v obvodu",
	S.incidentu Incidentů ve stanici
	FROM obvod O
	JOIN mapa O1 ON O1.id = O.id_objektu
	JOIN mapa L1 ON L1.id <> O1.id
	JOIN linka L ON L.id_objektu = L1.id
	JOIN mapa S1 ON S1.id <> L1.id
	JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu
	    FROM stanice S
	    LEFT JOIN incident I ON I.stanice = S.id
	    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id
	WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND 
	      S.id = 1
	ORDER BY S.nazev, O.nazev, L.nazev

-- Dotaz pro souhrné informace o lince
SELECT 
	O.id O_id, 
	O.nazev as "Dostupná v obvodu",
	L.nazev as "Dostupné linky", 
	COUNT(S.nazev) AS "Stanic v obvodu", 
	SUM(incidentu) "Incidentů v obvodu",
	ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) AS "Délka v obvodu"
	FROM obvod O
	JOIN mapa O1 ON O1.id = O.id_objektu
	JOIN mapa L1 ON L1.id <> O1.id
	JOIN linka L ON L.id_objektu = L1.id
	JOIN mapa S1 ON S1.id <> L1.id
	JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu
	    FROM stanice S
	    LEFT JOIN incident I ON I.stanice = S.id
	    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id
	WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE'    
	GROUP BY O.id, O.nazev, L.id, L.nazev, 
		 SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null)
	HAVING L.id = 3
	ORDER BY O.nazev, L.nazev
    
    
--Dotaz pro souhrné informace o obvodu
SELECT 
	O.id O_id, O.nazev O_nazev,
	L.nazev "Dostupné linky", 
	COUNT(S.nazev) AS "Stanic v obvodu", 
	SUM(incidentu) "Incidentů v obvodu",
	ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) AS "Délka v obvodu"
	FROM obvod O
	JOIN mapa O1 ON O1.id = O.id_objektu
	JOIN mapa L1 ON L1.id <> O1.id
	JOIN linka L ON L.id_objektu = L1.id
	JOIN mapa S1 ON S1.id <> L1.id
	JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu
	    FROM stanice S
	    LEFT JOIN incident I ON I.stanice = S.id
	    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id
	WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND
	      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE'    
	GROUP BY O.id, O.nazev, L.id, L.nazev, 
	         SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null)
	HAVING O.id = 1
	ORDER BY O.nazev, L.nazev  
    
-- Vyhledávání incidentu 
SELECT DISTINCT(I.id), I.datum, I.stanice, I.udalost, I.pricina, I.zraneni, I.zpozdeni, I.pohlavi, I.vek
FROM incident I
JOIN stanice S ON S.id = I.stanice
JOIN mapa M ON M.id = S.id_objektu
JOIN mapa N ON M.id <> N.id
JOIN obvod O ON N.id = O.id_objektu
JOIN mapa X ON X.id <> M.id
JOIN linka L ON L.id_objektu = X.id
WHERE SDO_GEOM.RELATE(N.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  M.geometrie, 1) <> 'FALSE'
  AND SDO_GEOM.RELATE(X.geometrie, 'contains+inside+touch',  M.geometrie, 1) <> 'FALSE'; 
  
--Vrátí ID stanic
SELECT DISTINCT (S.id) AS id_stanice, S.nazev
FROM incident I
JOIN stanice S ON S.id = I.stanice
			JOIN mapa M ON M.id = S.id_objektu
			JOIN mapa N ON M.id <> N.id
			JOIN obvod O ON N.id = O.id_objektu
			JOIN mapa X ON X.id <> M.id
			JOIN linka L ON L.id_objektu = X.id
			WHERE SDO_GEOM.RELATE(N.geometrie, 'contains+inside+covers+coverdby+touch',  M.geometrie, 1) <> 'FALSE'
			  AND SDO_GEOM.RELATE(X.geometrie, 'contains+inside+touch',  M.geometrie, 1) <> 'FALSE';
  