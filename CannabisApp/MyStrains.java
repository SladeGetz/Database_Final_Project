import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyStrains extends JDialog {
	Connection db;
    Vector<Integer> strainIDs;   // will hold strain ID values corresponding to search results
    int selectedRow;      
	JTable strains;
	public MyStrains() {
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		DefaultTableModel table = new DefaultTableModel();
		Object [] a = {"Strain", "Tried"};
		table.setColumnIdentifiers(a);

		strainIDs = new Vector<Integer>();
		strains = new JTable();
		strains.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		strains.setShowGrid(false);
		strains.setModel(table);
		
		JScrollPane scroller = new JScrollPane(strains);
		add(scroller);
	}
	
	public void addStrain(int id) {
		strainIDs.add(id);
	}
	public int getStrainID() {
        if (selectedRow != -1) {
            return strainIDs.elementAt(selectedRow);
        }
        else {
            return -1;
        }
    }
	
	 public void login(String username, String password) throws SQLException, ClassNotFoundException {
	        Class.forName("org.postgresql.Driver");
	        String connectString = "jdbc:postgresql://codd.mines.edu:5433/csci403";

	        db = DriverManager.getConnection(connectString, username, password);
	    }
}
