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

/**
 * Třída struktury možných hodnot složek incidentu pro potřeby vyhledávání,
 * ukládání a modifikaci incidentů.
 */
public class TIncident {
    public int I_id;
    public int I_vek;
    public int I_zpozdeni;
    public String I_dateFrom;
    public String I_dateTo;
    public String I_popis;
    public String I_udalost;
    public String I_zraneni;
    public int L_id;
    public String I_pricina;
    public String I_pohlavi;
    public int I_stanice;
    public int O_id;
}
