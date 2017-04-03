/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package GUI.TABS;

import GUI.MYOBJECTS.DateLabelFormatter;
import GUI.MYOBJECTS.MyComboBox;
import GUI.Main;
import GUI.PANELS.MapSummatyViewer;
import LOGIC.Incident.TIncident;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

public class Summary extends javax.swing.JPanel {

    JDatePickerImpl datePicker1;
    JDatePickerImpl datePicker2;
	TIncident ta = new TIncident();
    /**
     * Vytvoří záložku shrnutí.
     */
    public Summary() {
        initComponents();
		jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);
		
		// inicializce formuláře s datem
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		datePicker1.getModel().setDate(mYear, mMonth-1, mDay);
		datePicker1.getModel().setSelected(true);
		datePicker2.getModel().setDate(mYear, mMonth, mDay);
		datePicker2.getModel().setSelected(true);
		
		jBtnRefreshActionPerformed(null);
    }    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dateFrom = new javax.swing.JPanel();
        dateTo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        station = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        line = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        area = new javax.swing.JComboBox<>();
        jBtnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        mapViewer1 = new GUI.PANELS.MapSummatyViewer();
        areaName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabArea = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabLine = new javax.swing.JTable();
        stationName = new javax.swing.JLabel();
        lineName = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabStation = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTabArea2 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTabLine2 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTabArea3 = new javax.swing.JTable();

        jPanel2.setMaximumSize(null);

        jLabel6.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jLabel6.setText("Souhnné informace");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Datum");

        dateFrom.setMaximumSize(new java.awt.Dimension(96, 2147483647));
        dateFrom.setPreferredSize(new java.awt.Dimension(96, 21));
        dateFrom.setLayout(new java.awt.BorderLayout());

        dateTo.setMaximumSize(new java.awt.Dimension(96, 2147483647));
        dateTo.setPreferredSize(new java.awt.Dimension(96, 0));
        dateTo.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Stanice");

        station.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getStations(false)));
        station.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stationActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Linka");

        line.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getLines(false)));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Obvod");

        area.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getAreas(false)));

        jBtnRefresh.setText("Aktualizuj");
        jBtnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRefreshActionPerformed(evt);
            }
        });

        SqlDateModel model1 = new SqlDateModel();
        Properties p1 = new Properties();
        p1.put("text.today", "Today");
        p1.put("text.month", "Month");
        p1.put("text.year", "Year");
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1, p1);
        datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        dateFrom.add(datePicker1);
        SqlDateModel model2 = new SqlDateModel();
        Properties p2 = new Properties();
        p2.put("text.today", "Today");
        p2.put("text.month", "Month");
        p2.put("text.year", "Year");
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
        datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        dateTo.add(datePicker2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 80, Short.MAX_VALUE))
                    .addComponent(dateTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(station, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(line, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(area, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(dateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(dateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(station, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addComponent(line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBtnRefresh)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout mapViewer1Layout = new javax.swing.GroupLayout(mapViewer1);
        mapViewer1.setLayout(mapViewer1Layout);
        mapViewer1Layout.setHorizontalGroup(
            mapViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        mapViewer1Layout.setVerticalGroup(
            mapViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(mapViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(mapViewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        areaName.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        areaName.setText("Obvod");

        jScrollPane1.setViewportView(jTabArea);

        jTabLine.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTabLine);

        stationName.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        stationName.setText("Stanice");

        lineName.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        lineName.setText("Linka");

        jTabStation.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTabStation);

        jScrollPane5.setViewportView(jTabArea2);

        jScrollPane6.setViewportView(jTabLine2);

        jScrollPane7.setViewportView(jTabArea3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lineName)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(stationName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(areaName, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 152, Short.MAX_VALUE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(22, 22, 22))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stationName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(areaName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 322, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefreshActionPerformed
		// vytvoření struktury pro hodnoty formuláře

		// získání hodnot formuláře
		ta.I_stanice = ((((MyComboBox)station.getSelectedItem()) == null) ? 0 : ((MyComboBox)station.getSelectedItem()).getValue());
		ta.L_id = ((((MyComboBox)line.getSelectedItem()) == null) ? 0 : ((MyComboBox)line.getSelectedItem()).getValue());  
		ta.O_id = ((((MyComboBox)area.getSelectedItem()) == null) ? 0 : ((MyComboBox)area.getSelectedItem()).getValue()); 
		ta.I_dateFrom = datePicker1.getJFormattedTextField().getText();
		ta.I_dateTo = datePicker2.getJFormattedTextField().getText();   
		
		// nastavení ignorovaných hodnot
		ta.I_id = -1;
		ta.I_vek = -1;
		ta.I_zpozdeni = -1;
                
                try {
                    // překreslení mapy
                     mapViewer1.loadObjectsFromDb(ta.I_stanice, ta.L_id, ta.O_id);
                } catch (Exception ex) {
                    Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                }
		// překreslení mapy
		mapViewer1.repaint();	
		
		setArea(ta);
		setStation(ta);
		setLine(ta);
		
    }//GEN-LAST:event_jBtnRefreshActionPerformed

    private void stationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stationActionPerformed
	private void setArea(TIncident ta)
	{
		// získání statement
		try(Statement stmt = Main.db.getConnection().createStatement()) 
		{
			// získání resultSet
			String query = DB.Queries.Aggregation.getArea(ta, 1);
//			System.out.println("query:"+query);
			try (ResultSet rs = stmt.executeQuery(query))
			{
				// pro každý obrázek v DB
				while (rs.next()) {
//					System.out.println("cyklus");
					areaName.setText("Obvod: "+rs.getString("nazev"));
				}
			}
		}
		catch (SQLException e)
		{
			System.err.println("Exception setThumbnailsPanel1(): " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println("Exception setThumbnailsPanel1(): " + e.getMessage());
		}
		
		jTabArea.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getArea(ta, 2)));
		jTabArea2.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getArea(ta, 3)));
		jTabArea3.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getArea(ta, 4)));

	}
	
	private void setStation(TIncident ta)
	{
		// získání statement
		try(Statement stmt = Main.db.getConnection().createStatement()) 
		{
			// získání resultSet
			String query = DB.Queries.Aggregation.getStation(ta, 1);
//			System.out.println("query:"+query);
			try (ResultSet rs = stmt.executeQuery(query))
			{
				// pro každý obrázek v DB
				while (rs.next()) {
//					System.out.println("cyklus");
					stationName.setText("Stanice: "+rs.getString("nazev"));
				}
			}
		}
		catch (SQLException e)
		{
			System.err.println("Exception setThumbnailsPanel1(): " + e.getMessage());
		}
		catch (Exception e)
		{
		}
		
		jTabStation.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getStation(ta, 2)));

	}
	
	private void setLine(TIncident ta)
	{
		// získání statement
		try(Statement stmt = Main.db.getConnection().createStatement()) 
		{
			// získání resultSet
			String query = DB.Queries.Aggregation.getLine(ta, 1);
//			System.out.println("query:"+query);
			try (ResultSet rs = stmt.executeQuery(query))
			{
				// pro každý obrázek v DB
				while (rs.next()) {
//					System.out.println("cyklus");
					lineName.setText("Linka: "+rs.getString("nazev"));
				}
			}
		}
		catch (SQLException e)
		{
			System.err.println("Exception setThumbnailsPanel1(): " + e.getMessage());
		}
		catch (Exception e)
		{
		}
		
		jTabLine.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getLine(ta, 2)));
		jTabLine2.setModel(GUI.MYOBJECTS.MyTableModel.buildTableModel(DB.Queries.Aggregation.getLine(ta, 3)));
	}
	
    public MapSummatyViewer getMapSummatyViewer(){
    
        return mapViewer1;
    
    }
    
    public void setComboBoxs(){
        station.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getStations(false)));
        line.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getStations(false)));  
        area.setModel(GUI.MYOBJECTS.MyComboBox.getCombo(DB.Queries.ComboBox.getStations(false)));  
    }
    
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> area;
    private javax.swing.JLabel areaName;
    private javax.swing.JPanel dateFrom;
    private javax.swing.JPanel dateTo;
    private javax.swing.JButton jBtnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTabArea;
    private javax.swing.JTable jTabArea2;
    private javax.swing.JTable jTabArea3;
    private javax.swing.JTable jTabLine;
    private javax.swing.JTable jTabLine2;
    private javax.swing.JTable jTabStation;
    private javax.swing.JComboBox<String> line;
    private javax.swing.JLabel lineName;
    private GUI.PANELS.MapSummatyViewer mapViewer1;
    private javax.swing.JComboBox<String> station;
    private javax.swing.JLabel stationName;
    // End of variables declaration//GEN-END:variables
}
