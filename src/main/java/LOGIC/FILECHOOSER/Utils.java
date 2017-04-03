/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package LOGIC.FILECHOOSER;

import java.io.File;
import javax.swing.ImageIcon;

/**
 * Třída pro filechooser.
 * @author petr
 */
public class Utils {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
 
    /**
	 * Získá příponu souboru.
	 * @param f ověřovaný soubor
	 * @return přípona souboru
	 */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
 
    /**
	 * Vrátí obrázek jako ImageIcon nebo null, pokud cesta neexistuje.
	 * @param path cesta k obrázku
	 * @return brázek jako ImageIcon nebo null
	 */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
