/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package DB.Objects;

import java.awt.Color;
import java.awt.Rectangle;

public class DrawRectangle extends Rectangle {

    public int id;
    public String name;
    public Color color;
    public boolean active = false;
    public boolean geometryChanged = false;
    
    public DrawRectangle(int i, int i1, int i2, int i3) {
        super(i, i1, i2, i3);
    }
}
