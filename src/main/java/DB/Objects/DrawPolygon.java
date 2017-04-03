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
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 * Třída vykreslující polygon
 */
public class DrawPolygon extends Polygon {

    public int id;
    public String name;
    public Color color;
    public boolean active = false;
    public boolean geometryChanged = false;
    
	/**
	 * Vykreslí polygon
	 */
    public DrawPolygon() {
        this.xpoints = new int[10];
        this.ypoints = new int[10];
    }

    public void addPoint(Point2D[] javaPoints) {
       if(javaPoints != null) { 
        for (Point2D javaPoint : javaPoints) {
            this.npoints++;
            this.xpoints[this.npoints-1] = (int) javaPoint.getX();
            this.xpoints[this.npoints-1] = (int) javaPoint.getX();
        }
       }
    }   
}
