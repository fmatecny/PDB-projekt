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

import GUI.MYOBJECTS.MyComboBox;
import GUI.Main;
import java.util.Vector;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Struktura s dotazy pro data do comboboxů.
 */
public class ComboBox {
	/**
	 * Získá názvy linek.
	 * @param isFirstEmpty přidá možnost prázdné volby
	 * @return vektor hodnot
	 */
	public static Vector getLines(boolean isFirstEmpty)
	{
		return ComboBox.getObject("Linka", isFirstEmpty);
		}
	/**
	 * Získá názvy obvodů.
	 * @param isFirstEmpty přidá možnost prázdné volby
	 * @return vektor hodnot
	 */	
	public static Vector getAreas(boolean isFirstEmpty)
	{
		return ComboBox.getObject("Obvod", isFirstEmpty);
	}
	/**
	 * Získá názvy stanic.
	 * @param isFirstEmpty přidá možnost prázdné volby
	 * @return vektor hodnot
	 */
	public static Vector getStations(boolean isFirstEmpty)
	{
		return ComboBox.getObject("Stanice", isFirstEmpty);
	}
	
	/**
	 * Získá seznam stanic/linek/obvodů.
	 * @param name název požadované hodnoty
	 * @param isFirstEmpty přidá možnost prázdné volby
	 * @return vektor hodnot
	 */
	public static Vector getObject(String name, boolean isFirstEmpty)
	{
		Vector items = new Vector();
				
		String query = String.format("SELECT id, nazev FROM %s ORDER BY nazev", name);
		
		if (Main.db == null)
			return items;
		
		try (Statement stmt = Main.db.getConnection().createStatement())
		{
			try (ResultSet rs = stmt.executeQuery(query))
			{
				// vložení prázdné volby
				if (isFirstEmpty)
					items.addElement(new MyComboBox("--",-1));
				while(rs.next())
				{
					items.addElement(new MyComboBox(rs.getString(2),rs.getInt(1)));
				}
				return items;
			}
		}
		catch (SQLException sqlEx)
		{
			System.err.println("SQLException (getObject()): " + sqlEx.getMessage());
			items.addElement(new MyComboBox("EMPTY",-1));
			return items;
		}
	}
}
