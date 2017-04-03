/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package DB;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JLabel;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Třída pro připojení a udržování spojení s DB.
 */
public class DBConnection {
	private Connection conn; // udržované spojení
	private String login; // uživatelské jméno
	private String password; // heslo
	
    /**
	 * Provede přihlášení zadaného uživatele do DB.
	 * @param login uživatelské jméno
	 * @param password heslo
	 * @param warning předaný jLabel pro vypsání chybového hlášení
	 * @return true, pokud bylo přihlášení úspěšné
	 */
	public boolean connect(String login, String password, JLabel warning) {
		System.out.println("Connect...");
		this.login = login;
		this.password = password;
		closeConnection();
		try {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@//berta.fit.vutbr.cz:1526/pdb1");
			ods.setUser(login);
			ods.setPassword(password);
			conn = ods.getConnection();
			return true;
		} 
		catch (SQLException sqlEx) {
			if (warning != null)
				warning.setText("<html>"+ sqlEx.getMessage() +"</html>");
			closeConnection();
			return false;
		}
	}
	
	/**
	 * Získá vytvoření spojení nebo jej aktualizuje a rovněž vrátí.
	 * @return 
	 */
	public Connection getConnection()
	{
		if (conn == null) // ??? možná nefunguje přihlášení po timeoutu
			connect(login, password, null);
		return conn;
	}
	
	/**
	 * Uzavře spojení s DB.
	 */
	public void closeConnection(){
		try {
			if (conn != null)
				conn.close();	
		}
		catch (SQLException sqlEx) {
            System.err.println("SQLException (closeConnection): " + sqlEx.getMessage());
        }		
	}
}