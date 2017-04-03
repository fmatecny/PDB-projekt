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

import GUI.Main;
import LOGIC.Incident.TIncident;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

/**
 * Třída s dotazy do DB týkající se incidentů.
 */
public class Incident {
	/**
	 * Vrátí strukturu s hodnotami incidentu.
	 * @param incidentID ID požadovaného incidentu z DB
	 * @return struktura s hodnotami
	 */
	public static TIncident getIncident(int incidentID) 
	{
		TIncident ta = new TIncident();
		String query = String.format("SELECT "
				+ "id, TO_CHAR(datum, 'dd.mm.yyyy') AS datum, stanice, "
				+ "udalost, zpozdeni, pricina, zraneni, pohlavi, vek, popis "
				+ "FROM incident "
				+ "WHERE id = %d",
				incidentID);
//		System.out.println(query);
		if (Main.db == null)
			return null;
		
		try (Statement stmt = Main.db.getConnection().createStatement())
		{
			try (ResultSet rs = stmt.executeQuery(query))
			{
				while(rs.next())
				{
					// naplnění struktury
					ta.I_id = rs.getInt("id");
					ta.I_dateFrom = rs.getString("datum");
					ta.I_stanice = rs.getInt("stanice");
					ta.I_popis = rs.getString("popis");
					ta.I_udalost = rs.getString("udalost");
					ta.I_zpozdeni = rs.getInt("zpozdeni");
					ta.I_pohlavi = rs.getString("pohlavi");
					ta.I_vek = rs.getInt("vek");
					ta.I_pricina = rs.getString("pricina");
					ta.I_zraneni = rs.getString("zraneni");
				}
			}
		}
		catch (SQLException sqlEx) {
			System.err.println("SQLException (getIncident()): " + sqlEx.getMessage());
		}
			
		return ta;
	}
	
	/**
	 * Vrátí dotaz pro získání všech incidentů z databáze relevantní k vyplněným
	 * hodnotám ve struktuře jejich možných atributů.
	 * @param ta filtrovací struktura
	 * @return řetězec s dotazem
	 */
	public static String getIncidents(TIncident ta) 
	{
		String query = String.format("SELECT DISTINCT (I.id) AS id, TO_CHAR(I.datum, 'dd.mm.yyyy') AS datum, S.nazev AS stanice, L.nazev AS linka, O.nazev AS obvod, I.udalost, I.pricina, I.zraneni, I.zpozdeni, I.pohlavi, I.vek\n" +
			"FROM incident I\n" +
			"JOIN stanice S ON S.id = I.stanice\n" +
			"JOIN mapa M ON M.id = S.id_objektu\n" +
			"JOIN mapa N ON M.id <> N.id\n" +
			"JOIN obvod O ON N.id = O.id_objektu\n" +
			"JOIN mapa X ON X.id <> M.id\n" +
			"JOIN linka L ON L.id_objektu = X.id\n" +
			"WHERE SDO_GEOM.RELATE(N.geometrie, 'contains+inside+covers+coverdby+touch',  M.geometrie, 1) <> 'FALSE'\n" +
			"  AND SDO_GEOM.RELATE(X.geometrie, 'contains+inside+touch',  M.geometrie, 1) <> 'FALSE' AND %s",
				DB.Queries.Incident.getConcatCompound(ta));
//		System.out.println(query);
		return query;
	}
	
	/**
	 * Vloží/aktualizuje požadovaný incident - jeho položky a obrázky do DB.
	 * @param vecImages vektor s obrázky incidentu
	 * @param incidentID ID aktualizovaného incidentu
	 * @param ac struktura s položkami incidentu
	 * @param isEditMode příznak editace nebo vkládání incidentu
	 */
	public static void insertIncident(Vector<File> vecImages, int incidentID, TIncident ac, boolean isEditMode)
	{
		/* SPojit inset images s insertem incidentu ???
		INSERT ALL
		INTO incident (I_id,S_stanice, I_udalost,I_zpozdeni,I_popis,datum,pricina,I_zraneni, I_pohlavi, I_vek) VALUES (INCIDENT_SEQ.NEXTVAL,1,'pád',5,'text','12.12.2016','pád','bez','muz',40)
		INTO fotografie (I_id,incident,foto) VALUES (fotografie_seq.nextval, INCIDENT_SEQ.CURRVAL,ordsys.ordimage.init())
		SELECT * FROM dual;
		*/
		String query;
		// Vložení incidentu a následně obrázků
		if(!isEditMode) {
			query = String.format("INSERT INTO \"INCIDENT\" "
					+ "(id, stanice, udalost, zpozdeni, popis, datum, pricina, zraneni, pohlavi, vek) "
					+ "VALUES (INCIDENT_SEQ.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
		}
		else {
			query = String.format("UPDATE INCIDENT "
				+ "SET stanice=?, udalost=?, zpozdeni=?, popis=?, "
				+ "datum=?, pricina=?, zraneni=?, pohlavi=?, vek=?"
				+ "WHERE id=?");
		}
		
		try(PreparedStatement stmt = Main.db.getConnection().prepareStatement(query))
		{
			Main.db.getConnection().setAutoCommit(false);
			stmt.setInt(1, ac.I_stanice); // 
			stmt.setString(2, ac.I_udalost);
			stmt.setInt(3,ac.I_zpozdeni);
			stmt.setString(4,ac.I_popis);
			stmt.setString(5,ac.I_dateFrom);
			stmt.setString(6,ac.I_pricina);
			stmt.setString(7,ac.I_zraneni);
			stmt.setString(8,ac.I_pohlavi);
			stmt.setInt(9,ac.I_vek);
			
			if(isEditMode)
				stmt.setInt(10, ac.I_id);
			
			stmt.executeUpdate();
			//Main.db.getConnection().commit();
		}
		catch(SQLException SQLEx)
		{
			System.err.println("SQLxception: " + SQLEx.getMessage());
		}
		
		try {
			// Iterace nad všemi obrázky, které chceme vložit
			for(File f: vecImages)
			{
				if(isEditMode)
					query = "INSERT INTO fotografie (id, incident, foto) VALUES (fotografie_seq.nextval, ? ,ordsys.ordimage.init())";
				else
					query = "INSERT INTO fotografie (id, incident, foto) VALUES (fotografie_seq.nextval, incident_seq.currval ,ordsys.ordimage.init())";

				try(PreparedStatement stmt = Main.db.getConnection().prepareStatement(query))
				{
					if(isEditMode)
						stmt.setInt(1, incidentID);

					stmt.executeUpdate();
					
					query = "SELECT id, foto FROM fotografie ORDER BY id DESC FOR UPDATE";
					
					OracleResultSet rs = (OracleResultSet)stmt.executeQuery(query);
					
					if (!rs.next())
						System.out.println("Error in SQL out: SELECT id, foto FROM fotografie ORDER BY id DESC FOR UPDATE");
					
					OrdImage imgProxy = (OrdImage) rs.getORAData("foto", OrdImage.getORADataFactory());
					int id = rs.getInt("id");	
					rs.close();
					
					try {
						 imgProxy.loadDataFromFile(f.getAbsolutePath());
						 imgProxy.setProperties();
					 }
					 catch (IOException IOEx) {
						 System.err.println("IOException: " + IOEx.getMessage());
					 }	
					
					try (OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("UPDATE fotografie SET foto = ? WHERE id = ?"))
					{
						pstmt.setORAData(1, imgProxy);
						pstmt.setInt(2, id);
						pstmt.executeUpdate();
					}

					try (OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("UPDATE fotografie f SET f.foto_si = SI_StillImage(f.foto.getContent()) WHERE id = ?"))
					{
						pstmt.setInt(1, id);
						pstmt.executeUpdate();
					}

					try (OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("UPDATE fotografie SET "
							+ "foto_ac = SI_AverageColor(foto_si), "
							+ "foto_ch = SI_ColorHistogram(foto_si), "
							+ "foto_pc = SI_PositionalColor(foto_si), "
							+ "foto_tx = SI_Texture(foto_si) "
							+ "WHERE id = ?"))
					{
						pstmt.setInt(1, id);
						pstmt.executeUpdate();
					}					
				}
                catch (SQLException sqlEx) {
                    System.err.println("SQLException: " + sqlEx.getMessage());
                }
			}
			Main.db.getConnection().commit();
		}
		catch (Exception sqlEx)
		{
			System.err.println("SQLException (insertIncident): " + sqlEx.getMessage());

		}
	}
	
	/**
	 * Vrátí statement pro získání obrázků z DB pro požadované ID incidentu.
	 * @param incidentID ID incidentu
	 * @return statement
	 */
	public static OraclePreparedStatement getImagesFromDb(int incidentID)
	{
        try
		{
			OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("SELECT id, foto FROM fotografie WHERE incident = ?");
			pstmt.setInt(1,incidentID);
			return pstmt;
		}
		catch (SQLException sqlEx)
		{
			System.err.println("SQLException getImagesFromDb(): " + sqlEx.getMessage());
			return null;
		}
	}
	
	/**
	 * Smaže obrázek s požadovaným ID.
	 * @param imgID ID mazaného obrázku
	 */
	public static void deleteImageFromDB(int imgID)
	{
		try(OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("DELETE FROM fotografie WHERE id = ?");)
		{
			Main.db.getConnection().setAutoCommit(false);
			pstmt.setInt(1,imgID);	
			pstmt.executeUpdate();
			Main.db.getConnection().commit();
		} catch (SQLException ex) {
			System.err.println("SQLException (deleteImageFromDB): " + ex.getMessage());
		}
	}
	
	/**
	 * Smaže incident s požadovaným ID.
	 * @param incidentID ID mazaného incidentu
	 */
	public static void deleteIncident(int incidentID)
	{
		try(OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("DELETE FROM fotografie WHERE incident = ?");)
		{
			Main.db.getConnection().setAutoCommit(false);
			pstmt.setInt(1, incidentID);	
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("SQLException (deleteIncident): " + ex.getMessage());
		}
		
		try(OraclePreparedStatement pstmt = (OraclePreparedStatement) Main.db.getConnection().prepareStatement("DELETE FROM incident WHERE id = ?");)
		{
			pstmt.setInt(1, incidentID);	
			pstmt.executeUpdate();
			Main.db.getConnection().commit();
		} catch (SQLException ex) {
			System.err.println("SQLException (deleteIncident): " + ex.getMessage());
		}
	}
	
	/**
	 * Smaže incidenty odpovídající filtrovací struktuře - vzorový příklad 
	 * temporálního dotazu pro DELETE.
	 * @param ta filtrovací struktura
	 */
	public static void deleteIncidents(TIncident ta)
	{
		String query = String.format("DELETE FROM incident I WHERE %s",
		DB.Queries.Incident.getConcatCompound(ta));
		
		try(Statement pstmt = Main.db.getConnection().createStatement())
		{
			Main.db.getConnection().setAutoCommit(false);
			pstmt.executeUpdate(query);
			Main.db.getConnection().commit();
		} catch (SQLException ex) {
			System.err.println("SQLException (deleteIncidents): " + ex.getMessage());
		}
	}
	
	/**
	 * Vynuluje zpoždění incidentům odpovídajícím filtrovací struktuře - vzorový
	 * příklad temporálního dotazu pro UPDATE.
	 * @param ta 
	 */
	public static void groupUpdateIncidents(TIncident ta)
	{
		String query = String.format("UPDATE incident I SET I.zpozdeni = 0 WHERE %s",
		DB.Queries.Incident.getConcatCompound(ta));
		
		try(Statement pstmt = Main.db.getConnection().createStatement())
		{
			Main.db.getConnection().setAutoCommit(false);
			pstmt.executeUpdate(query);
			Main.db.getConnection().commit();
		} catch (SQLException ex) {
			System.err.println("SQLException (groupUpdateIncidents): " + ex.getMessage());
		}
	}
	
	/**
	 * Otočí obrázek incidentu se zadaným ID.
	 * @param imageID 
	 */
	public static void rotateImageFromDB(int imageID)
	{

		try(CallableStatement pstmt = Main.db.getConnection().prepareCall("{call rotate(?)}");)
		{
			pstmt.setInt(1, imageID);	
			pstmt.executeUpdate();
			Main.db.getConnection().commit();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}		
	}
	
	    public static OraclePreparedStatement searchByFoto(int id, double weightAC, double weightCH, double weightPC, double weightTX) throws SQLException {
		OraclePreparedStatement pstmt = (OraclePreparedStatement)Main.db.getConnection().prepareStatement(""
			+ "SELECT dst.id AS id, dst.foto as foto, dst.incident as incident, SI_ScoreByFtrList("
			+ "new SI_FeatureList(src.foto_ac,?,src.foto_ch,?,src.foto_pc,?,src.foto_tx,?),dst.foto_si)"
			+ "as similarity, I.popis AS popisek FROM fotografie src, fotografie dst "
			+ "JOIN incident I ON I.id = dst.incident "
			+ "WHERE src.id = ? AND dst.id <> src.id ORDER BY similarity ASC");

		
		pstmt.setDouble(1, weightAC);
		pstmt.setDouble(2, weightCH);
		pstmt.setDouble(3, weightPC);
		pstmt.setDouble(4, weightTX);
		pstmt.setInt(5, id);

		return pstmt;
	}
		
	/**
	 * Získá podmínku následovanou po WHERE pro vyplněné hodnoty struktury WHERE.
	 * @param ta filtrovací staruktura
	 * @return řetězec podmínky WHERE
	 */
	public static String getConcatCompound(TIncident ta) 
	{
		Field[] allFields = ta.getClass().getDeclaredFields();
		StringBuilder stringBuilder = new StringBuilder();
		
		// filtrování datem (operátor BETWEEN)
		stringBuilder.append("I.datum BETWEEN "+((ta.I_dateFrom == null) ? ta.I_dateFrom : "'"+ta.I_dateFrom+"'")+""
						   + " AND "+((ta.I_dateTo == null) ? ta.I_dateTo : "'"+ta.I_dateTo+"'")+" ");
		
		// pro ostatní hodnoty (operátor =)
		for (Field field : allFields) {		
			try {
				if (!(field.getName().equals("I_dateFrom") || field.getName().equals("I_dateTo") || 
					  field.get(ta).equals(-1) || field.get(ta).equals("--") || 
					  field.get(ta).equals("")))
				{
					stringBuilder.append(" AND ");
					String col = field.getName().replace("_", ".");
					stringBuilder.append(""+col+" = '"+field.get(ta)+"'");
				}
			}
			catch (Exception e) {
			}
		}

		return stringBuilder.toString();
	}
}
