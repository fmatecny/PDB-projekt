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
import javax.swing.filechooser.*;

/**
 * Třída pro filtrování souborů typu obrázek.
 */
public class ImageFilter extends FileFilter {
 
    /**
	 * Vrátí true pro adresáře a gif, jpg, tiff, nebo png soubory.
	 * @param f ověřovaný soubor
	 * @return true, pokud je adresář nebo obrázek
	 */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                extension.equals(Utils.tif) ||
                extension.equals(Utils.gif) ||
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg) ||
                extension.equals(Utils.png)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    /**
	 * Získání popisku obrázku
	 * @return posisek
	 */
    public String getDescription() {
        return "Just Images";
    }
}

