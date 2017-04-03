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
import java.awt.geom.Point2D;

/**
 * Třída vykreslující bod mapy.
 */
public class DrawPoint {
    public int id;
    public String name;
    public Color color;
    public boolean active = false;
    public boolean geometryChanged = false;
    public Point2D p2d;
    
    private double x = 0;
    private double y = 0;

	/**
	 * Vykreslí bod zadaný souřadnicemi.
	 * @param x1 x-ová
	 * @param y1 y-ová
	 */
	public DrawPoint(int x1, int y1) {
        this.p2d = new Point2D() {
            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public void setLocation(double d, double d1) {
                x = d;
                y = d1;
            }
        };
        this.x = x1;
        this.y = y1;
    }

    public DrawPoint() {
        this.p2d = new Point2D() {
            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public void setLocation(double d, double d1) {
                x = d;
                y = d1;
            }
        };
    }




    
    
    
}
