/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package LOGIC.Incident;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * Třída pro ověření hodnot fomulářů pro vyhledávání, ukládání a modifikaci
 * položek incidentu.
 */
public class ValidateForm {
	/**
	 * Ověří hodnotu typu JTextField, zda vyhovuje danému regulárnímu výrazu.
	 * @param field ověřované pole
	 * @param pattern regulání výraz
	 * @return true, zda odpovídá výrazu
	 */
	public static boolean validateValue(JTextField field, String pattern)
	{
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(field.getText());
		
		if(!m.matches())
		{
			field.setBackground(Color.red);
			return false;
		}
		field.setBackground(null);
		return true;
	}
	/**
	 * Ověří hodnotu typu JSpinner, zda vyhovuje danému regulárnímu výrazu.
	 * @param field ověřované pole
	 * @param pattern regulání výraz
	 * @return true, zda odpovídá výrazu
	 */
	public static boolean validateValue(JSpinner field, String pattern)
	{
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher((String) field.getValue().toString());
		
		if(!m.matches())
		{
			field.setBackground(Color.red);
			return false;
		}
		field.setBackground(null);
		return true;
	}
}
