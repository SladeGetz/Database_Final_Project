import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class GuiModel extends DefaultTableModel {

    Connection db;
    Vector<Integer> strainIDs;   // will hold strain ID values corresponding to search results
    int selectedRow;            // current selected row

    public GuiModel() {
        Object[] a = {"Rating", "Strain", "Type", "Effects", "Flavors" };
        setColumnIdentifiers(a);
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    public void removeSelectedRow() {
        removeRow(selectedRow);
    }


    public int getStrainID() {
        if (selectedRow != -1) {
            return strainIDs.elementAt(selectedRow);
        }
        else {
            return -1;
        }
    }

    public String getStrain() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 1);
        }
        else {
            return "";
        }
    }

    public String getStrainType() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 2);
        }
        else {
            return "";
        }
    }

    public String getStrainRating() {
        if (selectedRow != -1) {
            return getValueAt(selectedRow, 0).toString();
        }
        else {
            return "";
        }
    }
    
    public String getStrainEffects() {
    	 if (selectedRow != -1) {
             return (String) getValueAt(selectedRow, 3);
         }
         else {
             return "";
         }
    }

    public String getStrainFlavors() {
    	 if (selectedRow != -1) {
             return (String) getValueAt(selectedRow, 4);
         }
         else {
             return "";
         }
    }
    
    public String getStrainDescription() throws SQLException {
    	String query = "SELECT description " +
    				"FROM strain " +
    				"WHERE id = ?";
    	PreparedStatement ps = db.prepareStatement(query);
    	ps.setInt(1, getStrainID());
    	ResultSet rs = ps.executeQuery();
    	rs.next();
    	String description = rs.getString(1);
    	String ans = "";
    	int lineLength = 0;
    	for (int i = 0; i< description.length(); i++) {
    		if (description.charAt(i) == '\n') {
    			lineLength = 0;
    		} else {
    			lineLength++;
    		}
    		ans += description.charAt(i);
    		if (lineLength > 35) {
    			if (description.charAt(i) == ' '||description.charAt(i) == ',' || description.charAt(i) == '.') {
    				ans +="\n";
    				lineLength = 0;
    			}
    		} 
    		
    	}
    	return ans;
    }
    public void login(String username, String password) throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        String connectString = "jdbc:postgresql://codd.mines.edu:5433/csci403";

        db = DriverManager.getConnection(connectString, username, password);
    }

    public void search(FilterBox filterBox, String val) throws SQLException {
        ResultSet rs;
        rs = searchByStrain(val, filterBox);
       
        if (rs == null) {
            setNumRows(1);
            setValueAt("Search by "  + ":", 0, 0);
            setValueAt(val, 0, 1);
        }
        else {
            dataVector = new Vector<Vector>();
            strainIDs = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getDouble(3));
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(4));
                row.add(rs.getString(5));
                dataVector.add(row);
                strainIDs.add(rs.getInt(6));
            }
            if (dataVector.isEmpty()) {
            	filterBox.setVisible(true);
            	JOptionPane.showMessageDialog(filterBox.getContentPane(), "NO RESULTS", "QUERY ERROR", JOptionPane.ERROR_MESSAGE);
            	
            }
            this.fireTableDataChanged();
        }
    }

 

    public ResultSet searchByStrain(String val, FilterBox filterBox) throws SQLException {
    	String query =
    			"SELECT s.Strain, t.Type, s.Rating, STRING_AGG(DISTINCT e.Effect, ', ' ORDER BY e.Effect) Effects, STRING_AGG(DISTINCT f.Flavor, ', ' ORDER BY f.Flavor) Flavors, s.id " +
    					"FROM type AS t, type_xref AS tx, strain AS s, (SELECT Effect, id FROM effects WHERE id IN (" + filterBox.getEffects() + ")) AS e, (SELECT Flavor, id FROM flavors WHERE id IN (" + filterBox.getFlavors() + ")) AS f, effect_xref AS ex, flavor_xref AS fx " +
    					"WHERE lower(s.Strain) LIKE lower(?) " +
    					"AND s.id = tx.Strain_id " +
    					"AND s.Rating >= ? " + // get min rating
    					"AND t.id IN (" + filterBox.getTypes() + ") " +
    					"AND tx.Type_id = t.id "+
    					"AND e.id = ex.Effect_id " +
    					"AND ex.Strain_id = s.id " +
    					"AND f.id = fx.Flavor_id " +
    					"AND fx.Strain_id = s.id " +
    					"GROUP BY s.id, t.Type " +
    					"ORDER BY " + filterBox.getOrder(); // filterbox sets order
    	
    	
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, "%" + val + "%");
            ps.setDouble(2, Double.parseDouble(filterBox.getMinRating()));
            return ps.executeQuery();
    }

    public Vector<String> getFlavors() throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Flavor " +
			"FROM flavors";
		PreparedStatement ps = db.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }
    
    public Vector<String> getEffects() throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Effect " +
			"FROM effects";
		PreparedStatement ps = db.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }
    
    public Vector<String> getTypes() throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Type " +
			"FROM type";
		PreparedStatement ps = db.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }


    
    public Vector<String> getFlavors(int strainID) throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Flavor " +
			"FROM flavors " +
			"WHERE flavors.id IN " +
					"(SELECT f.flavor_id " +
					"FROM flavor_xref AS f " +
					"WHERE f.Strain_id = ?)";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, strainID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }
    
    public Vector<String> getEffects(int strainID) throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Effect " +
			"FROM effects " +
			"WHERE effects.id IN " +
				"(SELECT e.effect_id " +
				"FROM effect_xref AS e " +
				"WHERE e.Strain_id = ?)";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, strainID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }
    
    public String getStrainInfo() throws SQLException {
    	String flav = getFlavors(getStrainID()).toString();
    	String fx = getEffects(getStrainID()).toString();
    	String info = "Strain: " + getStrain() +
    			"\n\nFlavors: " + flav.substring(1, flav.length() -1) +
    			"\n\nEffects: " + fx.substring(1, fx.length() -1 ) +
    			"\n\nRating: " + getStrainRating() + ",\t Type: " + getStrainType() +
    			"\n\nDescription: " + getStrainDescription();
    	return info;
    }
}
