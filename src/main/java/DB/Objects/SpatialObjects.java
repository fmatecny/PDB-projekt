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

import java.util.ArrayList;
import java.util.List;

public class SpatialObjects {

    public List<DrawPoint> listPoints;
    public List<DrawRectangle> listRectangles;
    public List<DrawPolygon> listPolylines;
    public List<DrawPolygon> listPolygons;
    
    
    public SpatialObjects() {
        
        listPoints = new ArrayList<>();
        listRectangles = new ArrayList<>();
        listPolylines = new ArrayList<>();
        listPolygons = new ArrayList<>();
    }
}
