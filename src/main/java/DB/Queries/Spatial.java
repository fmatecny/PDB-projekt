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

import DB.Objects.DrawPoint;
import DB.Objects.DrawPolygon;
import DB.Objects.DrawRectangle;
import GUI.Main;
import GUI.PANELS.MapPanel;
import LOGIC.Incident.TIncident;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import static java.lang.Math.abs;
import java.net.URL;
import DB.ScriptRunner;

/**
 * Třída s mapu.
 */
public class Spatial {  
	public static void initDB(URL urlPath, URL urlPath1)
	{
		//Now read line bye line
		/*String thisLine, sqlQuery;
		try {
			BufferedReader d = new BufferedReader(new InputStreamReader(urlPath.openStream()));
			sqlQuery = "";
			while ((thisLine = d.readLine()) != null) 
			{  
				if(thisLine.length() > 0 && thisLine.charAt(0) == '-' || thisLine.length() == 0 ) 
					continue;
				sqlQuery = sqlQuery + " " + thisLine;

				if(sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
					sqlQuery = sqlQuery.replace(';' , ' '); //Remove the ; since jdbc complains
					try(Statement stmt = Main.db.getConnection().createStatement())
					{
						stmt.execute(sqlQuery);
					}
					catch(SQLException ex) {
						System.out.println("Chyba vykonání SQL příkazu ("+sqlQuery+"): "+ex.getMessage());
					}
					catch(Exception ex) {
						System.out.println("Chyba čtení SQL ze souboru: "+ex.getMessage());
					}
					sqlQuery = "";
				}   
			}
		}
		catch(IOException ex) {
		}
		catch(Exception ex) {
			System.out.println("Chyba otevíráni soubour s SQL: "+ex.getMessage());
		}*/
		
		try {
			// Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(Main.db.getConnection(), false, false);

			// Give the input file to Reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlPath.openStream()));

			// Exctute script
			sr.runScript(reader);

		} catch (Exception e) {
			System.err.println("Failed to Execute The error is " + e.getMessage());
		}
            if (urlPath1 != null){
		try {
			// Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(Main.db.getConnection(), false, false);
                        sr.setDelimiter(";;", true);
			// Give the input file to Reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlPath1.openStream()));

			// Exctute script
			sr.runScript(reader);

		} catch (Exception e) {
			System.err.println("Failed to Execute The error is " + e.getMessage());
		}
            }
	}
    
    public static String getIdStationFromIncidents(TIncident ta){
        
        if (ta != null){
        return String.format("SELECT DISTINCT (S.id) AS id_stanice, S.nazev " +
                                        "FROM incident I " +
                                        "JOIN stanice S ON S.id = I.stanice " +
                                        "JOIN mapa M ON M.id = S.id_objektu " +
                                        "JOIN mapa N ON M.id <> N.id " +
                                        "JOIN obvod O ON N.id = O.id_objektu " +
                                        "JOIN mapa X ON X.id <> M.id " +
                                        "JOIN linka L ON L.id_objektu = X.id " +
                                        "WHERE SDO_GEOM.RELATE(N.geometrie, 'contains+inside+covers+coverdby+touch',  M.geometrie, 1) <> 'FALSE' " +
                                        "AND SDO_GEOM.RELATE(X.geometrie, 'contains+inside+touch',  M.geometrie, 1) <> 'FALSE' AND %s", DB.Queries.Incident.getConcatCompound(ta));
        
            }
            return null;
        }
    
    
	public static String getGeometry(int id, int queryID)
	{
            String query = "";

            if (id < 1 )
                queryID = 0;
            
            switch (queryID) {           
                    
                case 2: //Získání barvy a geometrie obvodu
                    query = String.format("SELECT M.id, O.nazev, O.id AS id_objektu, M.barva, M.geometrie, M.geometrie.GET_WKT() " +
                            "FROM mapa M " +
                            "JOIN obvod O ON M.id = O.id_objektu " +
                            "WHERE O.id = %d", id);
                    break;    
                    
                case 3: //Získání barvy a geometrie linky
                    query = String.format("SELECT M.id, L.nazev, L.id AS id_objektu, M.barva, M.geometrie, M.geometrie.GET_WKT() " +
                            "FROM mapa M " +
                            "JOIN linka L ON M.id = L.id_objektu " +
                            "WHERE L.id = %d", id);
                    break;   
                    
                case 4: //Získání barvy a geometrie stanice
                    query = String.format("SELECT M.id, S.nazev, S.id AS id_objektu, M.barva, M.geometrie " +
                            "FROM mapa M " +
                            "JOIN stanice S ON M.id = S.id_objektu " +
                            "WHERE S.id = %d", id);
                    break; 
                    
                default: 
                    //query = "select barva, geometrie from mapa WHERE idCircuit IN (51, 52, 53,54,55,56)";
                    query = "select barva, geometrie from mapa";
                    break;
                

            }
		return query;
                
//                String query = String.format("SELECT barva, geometrie " 
//									+ "FROM "
//									+ "(SELECT S.idCircuit, S.id_objektu as objekt, S.nazev "
//									+ "FROM stanice S "
//									+ "JOIN incident I ON S.idCircuit = I.stanice "
//									+ "WHERE %s "
//									+ "GROUP BY S.idCircuit, S.id_objektu, S.nazev) "
//									+ "JOIN mapa M ON M.idCircuit = objekt",
//									DB.Querie.Incident.getConcatCompound(ta)); 
	}
	
	 public static void insertLineToDB(DrawPolygon l){        
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO mapa (id, geometrie, barva) " +
                    "VALUES (mapa_seq.NEXTVAL, ?, ?)");)
		{
			Main.db.getConnection().setAutoCommit(false);
            double[] array = new double[l.npoints*2];
            for (int i = 0; i < l.npoints*2; i+=2) {
                array[i] = l.xpoints[i/2];
                array[i+1] = l.ypoints[i/2];
//                System.out.println(array[i] + "," +array[i+1]);
            }
            String hex = String.format("#%02x%02x%02x", l.color.getRed(), l.color.getGreen(), l.color.getBlue());      
            
            JGeometry geo = JGeometry.createLinearLineString(array, 2, 0);

            //convert JGeometry instance to DB STRUCT using the SDO pickler
            STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
            ps.setObject(1, obj);
            ps.setObject(2, hex);
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO linka (id,nazev,id_objektu) "
                                                                    + "VALUES (LINKA_SEQ.nextval, ?, mapa_seq.currval)"))
         { 
			ps.setObject(1, l.name);            
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void insertStationToDB(DrawPoint p){
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO mapa (id, geometrie, barva) " +
                    "VALUES (mapa_seq.nextval, ?, ?)"))
		{
			Main.db.getConnection().setAutoCommit(false);
            double[] abc = new double[2];
            abc[0] = p.p2d.getX();
            abc[1] = p.p2d.getY();
            String hex = String.format("#%02x%02x%02x", p.color.getRed(), p.color.getGreen(), p.color.getBlue());
            
            JGeometry geo = JGeometry.createPoint(abc, 0, 0);

            //convert JGeometry instance to DB STRUCT using the SDO pickler
            STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
            ps.setObject(1, obj);
            ps.setObject(2, hex);
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO stanice (id,nazev,id_objektu)"
                                                        + " VALUES (stanice_seq.nextval, ?,mapa_seq.currval)"))
		{
            ps.setObject(1, p.name);
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    public static void insertCircuitToDB(DrawPolygon p, DrawRectangle r){
        double[] array = null;
        String hex;
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO mapa (id, geometrie, barva) " +
                    "VALUES (mapa_seq.NEXTVAL, ?, ?)"))
            
		{
			Main.db.getConnection().setAutoCommit(false);
            if(r == null) {
                array = new double[p.npoints*2+2];
                for (int i = 0; i < p.npoints*2; i+=2) {
                    array[i] = p.xpoints[i/2];
                    array[i+1] = p.ypoints[i/2];}
                array[p.npoints*2] = p.xpoints[0];
                array[p.npoints*2+1] = p.ypoints[0];

                hex = String.format("#%02x%02x%02x", p.color.getRed(), p.color.getGreen(), p.color.getBlue()); 
            }
			else {
                array = new double[10];
                array[0] = array[2] = array[8] = r.x;
                array[1] = array[7] = array[9] = r.y;
                array[4] = array[6] = r.x + r.width;
                array[3] = array[5] = r.y + r.height;
//                System.out.println(array[0] + "," +array[1]);
                
                hex = String.format("#%02x%02x%02x", r.color.getRed(), r.color.getGreen(), r.color.getBlue()); 
            }

            JGeometry geo = JGeometry.createLinearPolygon(array, 2, 0);

            //convert JGeometry instance to DB STRUCT using the SDO pickler
            STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
            ps.setObject(1, obj);
            ps.setObject(2, hex);
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try (
            PreparedStatement ps = Main.db.getConnection().prepareStatement("INSERT INTO obvod (id,nazev,id_objektu) "
                                                                    + "VALUES (obvod_seq.nextval, ?, mapa_seq.currval)"))
		{
            if (r == null) {
                ps.setObject(1, p.name);}
            else{
                ps.setObject(1, r.name);}
                     
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
  
    public class JGeometry2ShapeException extends Exception {
        };

    public static void loadObjectFromDb(){
            // perform the query
            String[] query = new String[3];
            query[0] = "SELECT M.id, S.nazev, S.id AS id_objektu, M.barva, M.geometrie "
                       +"FROM mapa M "
                       + "JOIN stanice S ON M.id = S.id_objektu";
            query[1] = "SELECT M.id, L.nazev, L.id AS id_objektu, M.barva, M.geometrie, M.geometrie.GET_WKT() "
                       +"FROM mapa M "
                       + "JOIN linka L ON M.id = L.id_objektu";
            query[2] = "SELECT M.id, O.nazev, O.id AS id_objektu, M.barva, M.geometrie, M.geometrie.GET_WKT() "
                       +"FROM mapa M "
                       + "JOIN obvod O ON M.id = O.id_objektu";
            for (int i = 0; i < 3; i++) {
                try (Statement stmt = Main.db.getConnection().createStatement())
		{
			try (ResultSet resultSet = stmt.executeQuery(query[i])){
                    while (resultSet.next()) {
                        // get a JGeometry object (the Java representation of SDO_GEOMETRY data)
                        byte[] image = resultSet.getBytes("geometrie");
                        String color = resultSet.getString("barva");
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("nazev");
                        JGeometry jGeometry = JGeometry.load(image);
                        // get a Shape object (the object drawable into Java GUI)
                        if(i == 0){
                        jGeometry2DrawObject(jGeometry, null, id, name, color);}
                        else {jGeometry2DrawObject(jGeometry, resultSet.getString(6), id, name, color);}
                    }
			}} catch (SQLException ex) {
                    System.err.println("SQLException(loadALLObjectFromDb): " + ex.getMessage());
                } catch (Exception ex) {
                } 
            }     
    }


    public static void jGeometry2DrawObject(JGeometry jGeometry,  String str, int id, String name, String color) 
            throws JGeometry2ShapeException {
        // check a type of JGeometry object
        switch (jGeometry.getType()) {
            // it is a polygon
            case JGeometry.GTYPE_POLYGON:
                            str = str.replace("POLYGON", " ");
                            str = str.replace("(", "");
                            str = str.replace(")", "");
                            str = str.replace(" ", ",");
                            str = str.replace(",,", ",");
                            String[] parts = str.split(",");
                            double[] array = new double[parts.length-3];
                            for (int i = 0; i < parts.length-3; i++) {
                                array[i] = Double.parseDouble(parts[i+1]);
                            }
                        if(jGeometry.isRectangle()){
                           Main.spacialObjects.listRectangles.add(new DrawRectangle((int) array[0], (int) array[1], (int) abs(array[0] - array[4]), (int) abs(array[1] - array[5])));
                           Main.spacialObjects.listRectangles.get(Main.spacialObjects.listRectangles.size()-1).id = id;
                           Main.spacialObjects.listRectangles.get(Main.spacialObjects.listRectangles.size()-1).name = name;
                           Main.spacialObjects.listRectangles.get(Main.spacialObjects.listRectangles.size()-1).color = Color.decode(color);
                        }
                        else{
                            Main.spacialObjects.listPolygons.add(new DrawPolygon());
                            for (int i = 0; i < array.length; i+=2) {
                                Main.spacialObjects.listPolygons.get(Main.spacialObjects.listPolygons.size()-1).addPoint((int) array[i], (int) array[i+1]);
                            }
                            Main.spacialObjects.listPolygons.get(Main.spacialObjects.listPolygons.size()-1).id = id;
                            Main.spacialObjects.listPolygons.get(Main.spacialObjects.listPolygons.size()-1).name = name;
                            Main.spacialObjects.listPolygons.get(Main.spacialObjects.listPolygons.size()-1).color = Color.decode(color);
                        }
                break;
                
            case JGeometry.GTYPE_CURVE:
                        str = str.replace("POLYGON", " ");
                        str = str.replace("(", "");
                        str = str.replace(")", "");
                        str = str.replace(" ", ",");
                        str = str.replace(",,", ",");
                        String[] parts1 = str.split(",");
                        double[] array1 = new double[parts1.length-1];
                        
                        for (int i = 0; i < parts1.length-1; i++) {
                            array1[i] = Double.parseDouble(parts1[i+1]);
                        }

                        Main.spacialObjects.listPolylines.add(new DrawPolygon());
                        for (int i = 0; i < array1.length; i+=2) {
                            Main.spacialObjects.listPolylines.get(Main.spacialObjects.listPolylines.size()-1).addPoint((int) array1[i], (int) array1[i+1]);
                        }
                        Main.spacialObjects.listPolylines.get(Main.spacialObjects.listPolylines.size()-1).id = id;
                        Main.spacialObjects.listPolylines.get(Main.spacialObjects.listPolylines.size()-1).name = name;
                        Main.spacialObjects.listPolylines.get(Main.spacialObjects.listPolylines.size()-1).color = Color.decode(color);
                        break; 
               
            case JGeometry.GTYPE_POINT:
                        Main.spacialObjects.listPoints.add(new DrawPoint());
                        Main.spacialObjects.listPoints.get(Main.spacialObjects.listPoints.size()-1).p2d = jGeometry.getJavaPoint();
                        Main.spacialObjects.listPoints.get(Main.spacialObjects.listPoints.size()-1).id = id;
                        Main.spacialObjects.listPoints.get(Main.spacialObjects.listPoints.size()-1).name = name;
                        Main.spacialObjects.listPoints.get(Main.spacialObjects.listPoints.size()-1).color = Color.decode(color);
                        break;
            // it is something else (we do not know how to convert)
            default:
                System.out.println("neznámý typ geometrie: " + jGeometry.getType());
                
        }
    }

    public static List<Integer> getIdIncidents(int idStation){
        List<Integer> id = new ArrayList<>();
        
        String query = String.format("SELECT I.id, S.id as id_stanice " +
                                   "FROM incident I " +
                                   "JOIN stanice S ON I.stanice = S.id " +
                                   "WHERE I.stanice = %d", idStation);
    
        try (Statement stmt = Main.db.getConnection().createStatement())
          {
        try (ResultSet resultSet = stmt.executeQuery(query)){
          while (resultSet.next()) {
              id.add(resultSet.getInt("id"));
          }
        }
        } catch (SQLException ex) { 
          Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return id;
      
    }
    
    public static List<Integer> getStationsOnLine(int idLine){
        List<Integer> id = new ArrayList<>();
        
        String query = String.format("SELECT M.id as ID_objektu_linka, N.id AS ID_objektu_stanice, S.id as ID_stanice, S.nazev " +
                                "FROM mapa M " +
                                "JOIN mapa N ON M.ID <> N.id " +
                                "JOIN linka L ON M.id = L.id_objektu " +
                                "JOIN stanice S ON N.id = S.id_objektu " +
                                "WHERE (M.id = %d) AND SDO_GEOM.RELATE(M.geometrie, 'touch+inside+contains',  N.geometrie, 1) <> 'FALSE' " +
                                "ORDER BY L.id ASC", idLine);
    
        try (Statement stmt = Main.db.getConnection().createStatement())
          {
        try (ResultSet resultSet = stmt.executeQuery(query)){
          while (resultSet.next()) {
              id.add(resultSet.getInt("ID_stanice"));
          }
        }
        } catch (SQLException ex) { 
          Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return id;
      
    }

    
    public static List<Integer> getLinesInCircuit(int idCyrcle){
        List<Integer> id = new ArrayList<>();
        
        String query = String.format("SELECT O.id, L.id, I.id AS ID_linka FROM mapa O " +
                                    "JOIN mapa L ON O.id <> L.id " +
                                    "JOIN linka I ON I.id_objektu = L.id " +
                                    "WHERE (O.id = %d) AND SDO_GEOM.RELATE(O.geometrie, 'contains+overlapbdydisjoint',  L.geometrie, 1) <> 'FALSE' " +
                                    "ORDER BY L.id ASC", idCyrcle);

        try (Statement stmt = Main.db.getConnection().createStatement())
          {
        try (ResultSet resultSet = stmt.executeQuery(query)){
          while (resultSet.next()) {
              id.add(resultSet.getInt("ID_linka"));
          }
        }
        } catch (SQLException ex) { 
          Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return id;
      
    }
    
    public static int getIdObject(int queryID, int id_in_map){
        String query = null;
        switch (queryID) {
            case 0: query = String.format("SELECT M.id, S.id as idcko " +
                                   "FROM mapa M " +
                                   "JOIN stanice S ON M.id = S.id_objektu " +
                                   "WHERE M.id = %d", id_in_map);
                    break;

            case 1: query = String.format("SELECT M.id, L.id as idcko " +
                                   "FROM mapa M " +
                                   "JOIN linka L ON M.id = L.id_objektu " +
                                   "WHERE M.id = %d", id_in_map);
                    break;
                
            case 2: query = String.format("SELECT M.id, O.id as idcko " +
                                   "FROM mapa M " +
                                   "JOIN obvod O ON M.id = O.id_objektu " +
                                   "WHERE M.id = %d", id_in_map);
                    break;

            default:
                return 0;
        }
         
        try (Statement stmt = Main.db.getConnection().createStatement())
		{
            try (ResultSet resultSet = stmt.executeQuery(query)){
                while (resultSet.next()) {
                    return resultSet.getInt("idcko");
                }
            }
            } catch (SQLException ex) { 
                Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return 0;
    }
 
    
    public static void UpdateStationGeometry(DrawPoint p){
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set geometrie=? where id=?"))
		{
            Main.db.getConnection().setAutoCommit(false);
        double[] abc = new double[2];
        abc[0] = p.p2d.getX();
        abc[1] = p.p2d.getY();
    
        JGeometry geo = JGeometry.createPoint(abc, 0, 0);

        //convert JGeometry instance to DB STRUCT using the SDO pickler
        STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
        ps.setObject(1, obj);
        ps.setObject(2, p.id);
        ps.executeUpdate();
        Main.db.getConnection().commit();
        
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void UpdateLineGeometry(DrawPolygon l){
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set geometrie=? where id=?"))
		{
            Main.db.getConnection().setAutoCommit(false);
            double[] array = new double[l.npoints*2];
            for (int i = 0; i < l.npoints*2; i+=2) {
                array[i] = l.xpoints[i/2];
                array[i+1] = l.ypoints[i/2];
//                System.out.println(array[i] + "," +array[i+1]);
            }
    
        JGeometry geo = JGeometry.createLinearLineString(array, 2, 0);

        //convert JGeometry instance to DB STRUCT using the SDO pickler
        STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
        ps.setObject(1, obj);
        ps.setObject(2, l.id);
        ps.executeUpdate();
        Main.db.getConnection().commit();
        
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void UpdateCircuitGeometry(DrawPolygon p, DrawRectangle r){
        double[] array = null;
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set geometrie=? where id=?"))
		{
            Main.db.getConnection().setAutoCommit(false);
            if(r == null) {
                array = new double[p.npoints*2+2];
                for (int i = 0; i < p.npoints*2; i+=2) {
                    array[i] = p.xpoints[i/2];
                    array[i+1] = p.ypoints[i/2];}
                array[p.npoints*2] = p.xpoints[0];
                array[p.npoints*2+1] = p.ypoints[0];
            }
            else{
                array = new double[10];
                array[0] = array[2] = array[8] = r.x;
                array[1] = array[7] = array[9] = r.y;
                array[4] = array[6] = r.x + r.width;
                array[3] = array[5] = r.y + r.height;
//                System.out.println(array[0] + "," +array[1]);
            }
    
            JGeometry geo = JGeometry.createLinearPolygon(array, 2, 0);

            //convert JGeometry instance to DB STRUCT using the SDO pickler
            STRUCT obj = JGeometry.store(Main.db.getConnection(), geo);
            ps.setObject(1, obj);
            if (r == null){
                ps.setObject(2, p.id);
            }
            else{
                ps.setObject(2, r.id);
            }
            ps.executeUpdate();
            Main.db.getConnection().commit();
        
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void UpdateStationToDB(DrawPoint p) throws SQLException, Exception {
        /// writing a geometry back to database
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set barva=? where id=?"))
		{
            Main.db.getConnection().setAutoCommit(false);
            
            String hex = String.format("#%02x%02x%02x", p.color.getRed(), p.color.getGreen(), p.color.getBlue());
            
            ps.setObject(1, hex);
            ps.setObject(2, p.id);
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
                

        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE stanice set nazev=? where id=?"))
         { 
            ps.setObject(1, p.name);
            ps.setObject(2, getIdObject(0, p.id));
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public static void UpdateLineToDB(DrawPolygon l) throws SQLException, Exception {
        /// writing a geometry back to database
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set barva=? where id=?"))
		{
            Main.db.getConnection().setAutoCommit(false);
            
            String hex = String.format("#%02x%02x%02x", l.color.getRed(), l.color.getGreen(), l.color.getBlue());      
              
            ps.setObject(1, hex);
            ps.setObject(2, l.id);
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE linka set nazev=? where id=?"))
         { 
            ps.setObject(1, l.name);
            ps.setObject(2, getIdObject(1, l.id));
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    
    public static void UpdateCircuitToDB(DrawPolygon p, DrawRectangle r) throws SQLException, Exception {
        String hex; 
        /// writing a geometry back to database
        try(PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE mapa set barva=? where id=?"))
		{
			Main.db.getConnection().setAutoCommit(false);
            if(r == null) {
                hex = String.format("#%02x%02x%02x", p.color.getRed(), p.color.getGreen(), p.color.getBlue()); 
            }
            else{                
                hex = String.format("#%02x%02x%02x", r.color.getRed(), r.color.getGreen(), r.color.getBlue()); 
            }
            
            ps.setObject(1, hex);
            if (r == null){
                ps.setObject(2, p.id);
            }
            else{
                ps.setObject(2, r.id);
            }
            
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("UPDATE obvod SET nazev=? WHERE id=?"))
         { 
            if (r == null) {
                ps.setObject(1, p.name);
                ps.setObject(2, getIdObject(2, p.id));}
            else{
                ps.setObject(1, r.name);
                ps.setObject(2, getIdObject(2, r.id));}
            
            
            ps.executeUpdate();
			Main.db.getConnection().commit();
        } catch (Exception ex) {
            Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteObjectInMap(int id){
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM mapa WHERE id = ?"))
        { 
            Main.db.getConnection().setAutoCommit(false);
            ps.setInt(1, id);
            ps.executeUpdate();
            Main.db.getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void deleteStation(int id_in_map) {  
        int idStation = getIdObject(0, id_in_map);
        List<Integer> idIncident = getIdIncidents(idStation);
        
        for (Integer integer : idIncident) {
            Incident.deleteIncident(integer);
        }
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM stanice WHERE id = ?"))
        { 
            Main.db.getConnection().setAutoCommit(false);
            ps.setInt(1, idStation);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        deleteObjectInMap(id_in_map);    
    }
    
    public static void deleteLine(int id_in_map) {
        int idLine = getIdObject(1, id_in_map);
        List<Integer> idStations = getStationsOnLine(idLine);
                
        for (Integer idStation : idStations) {
            deleteStation(idStation);
        }
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM linka WHERE id = ?"))
        { 
            ps.setInt(1, idLine);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        deleteObjectInMap(id_in_map);    
    }
    
    public static void deleteCircuit(int id_in_mapP, int id_in_mapR) {
        int idCircuit = 0;
        if (id_in_mapR < 0){
            idCircuit = getIdObject(2, id_in_mapP);
        }
        else{
            idCircuit = getIdObject(2, id_in_mapR);
        }
        
        List<Integer> idLines = getLinesInCircuit(idCircuit); 
        for (Integer idLine : idLines) {
            deleteLine(idLine);
        }
      
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM obvod WHERE id = ?"))
        { 
//             System.out.println(idCircuit);
            ps.setInt(1, idCircuit);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        deleteObjectInMap(idCircuit);
    }

    
    public static void deleteDB(){
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM fotografie"))
        { 
             Main.db.getConnection().setAutoCommit(false);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM incident"))
        { 
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM stanice"))
        { 
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }     

                try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM linka"))
        { 
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }  
                
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM obvod"))
        { 
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        try (PreparedStatement ps = Main.db.getConnection().prepareStatement("DELETE FROM mapa"))
        { 
            ps.executeUpdate();
            Main.db.getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(Spatial.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
}
