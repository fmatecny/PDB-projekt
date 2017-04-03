/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package DB.Queries;

import LOGIC.Incident.TIncident;

/**
 * Třída s agregačními dotazy.
 */
public class Aggregation {
	/**
	 * Získá dotaz pro souhrnnou statistiku o evidovaných datech.
	 * @return řetězec s dotazem
	 */
	public static String getStats() {
		String query = String.format("SELECT 'Počet incidentů' AS Položka, CAST(COUNT(*) AS VARCHAR(32)) AS Hodnota FROM INCIDENT "			
			+ "UNION SELECT * "
			+ "FROM (SELECT 'Nejčastější událost ', udalost || ' (' || COUNT(udalost) || 'x)' "
			+ "FROM INCIDENT GROUP BY udalost "
			+ "ORDER BY COUNT(udalost) DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT * "
			+ "FROM (SELECT 'Nejčastější přičina ', pricina || ' (' || COUNT(pricina) || 'x)' "
			+ "FROM INCIDENT GROUP BY pricina "
			+ "ORDER BY COUNT(pricina) DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT * "
			+ "FROM (SELECT 'Nejčastější zranění ', zraneni || ' (' || COUNT(zraneni) || 'x)' "
			+ "FROM INCIDENT GROUP BY zraneni "
			+ "ORDER BY COUNT(zraneni) DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT 'Průměrné zpoždění', CAST(ROUND(AVG(zpozdeni), 0) AS VARCHAR(32)) || ' min' FROM incident "

			+ "UNION SELECT 'Průměrný věk', CAST(ROUND(AVG(vek), 0) AS VARCHAR(32)) FROM incident "

			+ "UNION SELECT 'Nejnehodovější stanice', nazev_stanice || ' (' || Pocet || 'x)' "
			+ "FROM "
			+ "  (SELECT S.id AS id_stanice, S.nazev AS nazev_stanice, COUNT(S.id) as Pocet, S.id_objektu AS id_objektu "
			+ "  FROM stanice S "
			+ "  JOIN incident I ON S.id = I.stanice "
			+ "  GROUP BY S.id, S.nazev, S.id_objektu "
			+ "  ORDER BY Pocet DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT 'Obvod s nejvíce stanicemi', Obvod || ' (' || Pocet || 'x)' "
			+ "FROM "
			+ "  (SELECT M.id, O.nazev Obvod, COUNT(S.nazev) Pocet"
			+ "  FROM mapa M"
			+ "  JOIN mapa N ON N.id <> M.id"
			+ "  JOIN stanice S ON S.id_objektu = N.id"
			+ "  JOIN obvod O ON M.id = O.id_objektu"
			+ "  WHERE SDO_GEOM.RELATE(M.geometrie, 'contains+inside',  N.geometrie, 1) <> 'FALSE'"
			+ "  GROUP BY M.id, O.nazev"
			+ "  ORDER BY Pocet DESC) "
			+ "WHERE ROWNUM <= 1 "

			+ "UNION SELECT 'Linka s nejvíce stanicemi', Linka || ' (' || Pocet || ' stanic)' "
			+ "FROM "
			+ "  (SELECT M.id, L.nazev Linka, COUNT(S.nazev) Pocet "
			+ "  FROM mapa M "
			+ "  JOIN mapa N ON N.id <> M.id "
			+ "  JOIN stanice S ON S.id_objektu = N.id "
			+ "  JOIN linka L ON M.id = L.id_objektu "
			+ "  WHERE SDO_GEOM.RELATE(M.geometrie, 'touch+contains+inside',  N.geometrie, 1) <> 'FALSE' "
			+ "  GROUP BY M.id, L.nazev "
			+ "  ORDER BY Pocet DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT 'Nejnehodovější obvod', Obvod || ' (' || Pocet || 'x)' "
			+ "FROM "
			+ "  (SELECT M.id, O.nazev Obvod, COUNT(I.id) AS Pocet "
			+ "  FROM mapa M "
			+ "  JOIN obvod O ON M.id = O.id_objektu "
			+ "  JOIN mapa N ON M.id <> N.id "
			+ "  JOIN stanice S ON N.id = S.id_objektu "
			+ "  JOIN incident I ON I.stanice = S.id "
			+ "  WHERE SDO_GEOM.RELATE(M.geometrie, 'contains+inside',  N.geometrie, 1) <> 'FALSE' "
			+ "  GROUP BY M.id, O.nazev "
			+ "  ORDER BY Pocet DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT 'Nejnehodovější linka', Linka || ' (' || Pocet || 'x)' "
			+ "FROM "
			+ "  (SELECT M.id, L.nazev Linka, COUNT(I.id) AS Pocet "
			+ "  FROM mapa M "
			+ "  JOIN linka L ON M.id = L.id_objektu "
			+ "  JOIN mapa N ON M.id <> N.id "
			+ "  JOIN stanice S ON N.id = S.id_objektu "
			+ "  JOIN incident I ON I.stanice = S.id "
			+ "  WHERE SDO_GEOM.RELATE(M.geometrie, 'touch+inside+contains',  N.geometrie, 1) <> 'FALSE' "
			+ "  GROUP BY M.id, L.nazev "
			+ "  ORDER BY Pocet DESC) "
			+ "WHERE ROWNUM <= 1"

			+ "UNION SELECT 'Délka sítě metra', CAST(ROUND(SUM(Delka), 0) AS VARCHAR(32)) || ' m'"
			+ "FROM "
			+ "(SELECT M.id, L.id as id_linky, L.nazev, SDO_GEOM.SDO_LENGTH(M.geometrie,1,null) as Delka "
			+ "FROM linka L "
			+ "JOIN mapa M ON L.id_objektu = M.id)");
		return query;
	}
		
	/**
	 * Získá statistiku o zadané stanici.
	 * @param ta struktura s identifikací požadované stanice
	 * @param queryID ID dotazu k vykonání
	 * @return řetězec s dotazem
	 */
	public static String getStation(TIncident ta, int queryID)
	{
		String query = "";
		switch (queryID) {
			case 1:  
				// název stanice
				query = String.format("SELECT nazev FROM stanice WHERE id = %d" , ta.I_stanice);
				break;
			case 2:  

				query = String.format("SELECT \n" +
					"/*O.id O_id, S.id S_id,\n" +
					"S.nazev S_nazev,*/\n" +
					"L.nazev \"Na lince\",\n" +
					"O.nazev \"Dostupná v obvodu\",\n" +
					"/*L.nazev \"Dostupné linky\",*/ \n" +
					"ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) || ' m' AS \"Délka v obvodu\",\n" +
					"S.incidentu \"Incidentů ve stanici\"\n" +
					"FROM obvod O\n" +
					"JOIN mapa O1 ON O1.id = O.id_objektu\n" +
					"JOIN mapa L1 ON L1.id <> O1.id\n" +
					"JOIN linka L ON L.id_objektu = L1.id\n" +
					"JOIN mapa S1 ON S1.id <> L1.id\n" +
					"JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu\n" +
					"    FROM stanice S\n" +
					"    LEFT JOIN incident I ON I.stanice = S.id\n" +
					"    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id\n" +
					"WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND \n" +
					"      S.id = %d\n" +
					"ORDER BY S.nazev, O.nazev, L.nazev" , 
					ta.I_stanice);
				break;
		}

//		System.out.println("Q:"+query);
		return query;
	}
	
	/**
	 * Získá statistiku o zadané lince.
	 * @param ta struktura s identifikací požadované lince
	 * @param queryID ID dotazu k vykonání
	 * @return řetězec s dotazem
	 */
	public static String getLine(TIncident ta, int queryID)
	{
		String query = "";
		switch (queryID) {
			case 1:  
				// název linky
				query = String.format("SELECT nazev FROM linka WHERE id = %d" , ta.L_id);
				break;
			case 2:  
				query = String.format("SELECT \n" +
					"/*O.id O_id,*/ \n" +
					"O.nazev \"Dostupná v obvodu\",\n" +
					"/*L.nazev \"Dostupné linky\",*/ \n" +
					"COUNT(S.nazev) AS \"Stanic v obvodu\", \n" +
					"SUM(incidentu) \"Incidentů v obvodu\",\n" +
					"ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) || ' m' AS \"Délka v obvodu\"\n" +
					"FROM obvod O\n" +
					"JOIN mapa O1 ON O1.id = O.id_objektu\n" +
					"JOIN mapa L1 ON L1.id <> O1.id\n" +
					"JOIN linka L ON L.id_objektu = L1.id\n" +
					"JOIN mapa S1 ON S1.id <> L1.id\n" +
					"JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu\n" +
					"    FROM stanice S\n" +
					"    LEFT JOIN incident I ON I.stanice = S.id\n" +
					"    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id\n" +
					"WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE'    \n" +
					"GROUP BY O.id, O.nazev, L.id, L.nazev, \n" +
					"         SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null)\n" +
					"HAVING L.id = %d\n" +
					"ORDER BY O.nazev, L.nazev", 
						ta.L_id);
//				System.out.println(query);
				break;
			case 3:
				query = String.format("SELECT \n" +
					"/*L.id,\n" +
					"L.nazev \"Dostupné linky\", */\n" +
					"ROUND(SDO_GEOM.SDO_LENGTH(M.geometrie, 1 , null), 0) || ' m' AS \"Délka linky\"\n" +
					"FROM mapa M\n" +
					"JOIN linka L ON M.id = L.id_objektu\n" +
					"WHERE L.id = %d\n" +
					"ORDER BY L.nazev", 
						ta.L_id);
		}

//		System.out.println("QLI:"+query);
		return query;
	}
	/**
	 * Získá statistiku o zadané oblasti.
	 * @param ta struktura s identifikací požadované oblasti
	 * @param queryID ID dotazu k vykonání
	 * @return řetězec s dotazem
	 */
	public static String getArea(TIncident ta, int queryID)
	{
		String query = "";
		switch (queryID) {
			case 1:  
				// název obvodu
				query = String.format("SELECT nazev FROM obvod WHERE id = %d" , ta.O_id);
				break;
				
			case 2:
				query = String.format("SELECT \n" +
					"/*O.id O_id, O.nazev O_nazev,*/\n" +
					"L.nazev \"Dostupné linky\", \n" +
					"COUNT(S.nazev) AS \"Stanic v obvodu\", \n" +
					"SUM(incidentu) \"Incidentů v obvodu\",\n" +
					"ROUND(SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null), 0) || ' m' AS \"Délka v obvodu\"\n" +
					"FROM obvod O\n" +
					"JOIN mapa O1 ON O1.id = O.id_objektu\n" +
					"JOIN mapa L1 ON L1.id <> O1.id\n" +
					"JOIN linka L ON L.id_objektu = L1.id\n" +
					"JOIN mapa S1 ON S1.id <> L1.id\n" +
					"JOIN (SELECT S.id, S.id_objektu, S.nazev, COUNT(I.id) incidentu\n" +
					"    FROM stanice S\n" +
					"    LEFT JOIN incident I ON I.stanice = S.id\n" +
					"    GROUP BY S.id, S.id_objektu, S.nazev) S ON S.id_objektu = S1.id\n" +
					"WHERE SDO_GEOM.RELATE(L1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+overlapbdydisjoint+inside+covers+coverdby+touch',  L1.geometrie, 1) <> 'FALSE' AND\n" +
					"      SDO_GEOM.RELATE(O1.geometrie, 'contains+inside+touch',  S1.geometrie, 1) <> 'FALSE'    \n" +
					"GROUP BY O.id, O.nazev, L.id, L.nazev, \n" +
					"         SDO_GEOM.SDO_LENGTH(SDO_GEOM.SDO_INTERSECTION(O1.geometrie, L1.geometrie,0.5), 1 , null)\n" +
					"HAVING O.id = %d\n" +
					"ORDER BY O.nazev, L.nazev", 
						ta.O_id);
				break;
			case 3:
				query = String.format("SELECT \n" +
					"  /*O.id, O.nazev,*/\n" +
					"  SDO_GEOM.SDO_LENGTH(O1.geometrie,1,null) || ' m'  AS \"Délka hranice\", \n" +
					"  SDO_GEOM.SDO_AREA(O1.geometrie,1,null) || ' m2'  AS \"Plocha\"\n" +
					"FROM obvod O\n" +
					"JOIN mapa O1 ON O1.id = O.id_objektu\n" +
					"WHERE O.id = %d",
						ta.O_id);
				break;
			case 4:
				query = String.format("SELECT LISTAGG(O2.nazev, ',') WITHIN GROUP(ORDER BY O2.nazev) as \"Sousední obvody\"\n" +
					"FROM mapa M\n" +
					"JOIN mapa N ON M.id <> N.id\n" +
					"JOIN obvod O1 ON O1.id_objektu = M.id\n" +
					"JOIN obvod O2 ON O2.id_objektu = N.id\n" +
					"WHERE (O1.id = %d) AND SDO_GEOM.RELATE(M.geometrie, 'touch',  N.geometrie, 1) <> 'FALSE'\n" +
					"ORDER BY N.id ASC",
							ta.O_id);
				break;
		}
//		System.out.println("QLI:"+query);
		return query;
	}
}
