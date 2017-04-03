/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package GUI.MYOBJECTS;

import GUI.Main;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Třída vytvářející plnící tabulku daty z databáze.
 * @author petr
 */
public class MyTableModel {
	/**
	 * Vytvoří data pro tabulku pro zadaný dotaz.
	 * @param query dotaz
	 * @return vzniklý model (data) do tabulky
	 */
	public static DefaultTableModel buildTableModel(String query)
	{
		// vektor s názvy sloupců
		Vector<String> columnNames = new Vector<String>();
		// vektor s hodnotami řádků
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		if (Main.db == null)
			return new DefaultTableModel(data, columnNames);

		try (Statement stmt = Main.db.getConnection().createStatement())
		{
			try (ResultSet rs = stmt.executeQuery(query))
			{
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				// získání názvů sloupců
				for (int column = 1; column <= columnCount; column++) {
					columnNames.add(metaData.getColumnName(column));
				}

				// získání řádků tabulky
				while (rs.next()) {
					// vektor hodnot řádku
					Vector<Object> vector = new Vector<Object>();
					for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
						vector.add(rs.getObject(columnIndex));
					}
					data.add(vector);
				}
				return new DefaultTableModel(data, columnNames){
					@Override
					public boolean isCellEditable(int row, int column) {
						return column == 3;
					}
				 };
			}
			
		} catch (SQLException sqlEx) {
			System.err.println("SQLException (DefaultTableModel): " + sqlEx.getMessage());
		}
		return new DefaultTableModel(data, columnNames);
	}
}
