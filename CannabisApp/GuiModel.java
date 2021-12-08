import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class GuiModel extends DefaultTableModel {

    Connection db;
    Vector<Integer> strainIDs;   // will hold strain ID values corresponding to search results
    int selectedRow;            // current selected row

    public GuiModel() {
        Object[] a = {"Strain", "Type", "Rating"};
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
            return (String) getValueAt(selectedRow, 0);
        }
        else {
            return "";
        }
    }

    public String getStrainType() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 1);
        }
        else {
            return "";
        }
    }

    public String getStrainRating() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 2);
        }
        else {
            return "";
        }
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
            setValueAt("(Not yet implemented)", 0, 2);
        }
        else {
            dataVector = new Vector<Vector>();
            strainIDs = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                dataVector.add(row);
                strainIDs.add(rs.getInt(4));
            }
            this.fireTableDataChanged();
        }
    }

 

    public ResultSet searchByStrain(String val, FilterBox filterBox) throws SQLException {
    	String query =
                "SELECT s.Strain, t.Type, s.Rating " +
                "FROM type AS t, type_xref AS tx, strain AS S " +
                "WHERE lower(s.Strain) LIKE lower(?) " +
                "AND s.id = tx.Strain_id " +
                "AND s.Rating >= ? " + // get min rating
                "AND s.id IN " + 
                	"(SELECT e.Strain_id, f.Strain_id " +
                	"FROM effects AS e, flavors AS f " +
                	"WHERE e.id IN (?) " + // get selected effect ids
                	"AND f.id IN (?)) " + // get selected flavors 
                "AND t.id IN (?) " +
                "ORDER BY ?"; // filterbox sets order

            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, "%" + val + "%");
            ps.setString(2, filterBox.getMinRating());
            ps.setString(3, filterBox.getEffects());
            ps.setString(4,  filterBox.getFlavors());
            ps.setString(5, filterBox.getTypes());
            ps.setString(6, filterBox.getOrder());
            return ps.executeQuery();
    }

    public Vector<String> getFlavors() throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Flavor " +
			"FROM flavor";
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
			"FROM effect";
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

    public void insertArtist(String artist) throws SQLException {
        // TODO: implement this
        System.out.println("Inserting new artist: " + artist + " (Not yet implemented)");
    }

    public void insertStrain(String strain, String type, String rating, Vector<String> flavors, Vector<String> effects, String description) throws SQLException {
        // TODO: implement this
        System.out.println("Inserting new strain: "  + " - "  + "("  + ") " + " (Not yet implemented)");
    }

    public void updateStrain(int strainID, String strain, String type, String rating, Vector<String> flavors, Vector<String> effects, String description) throws SQLException {
        // TODO: implement this
        System.out.println("Updating strain id " + strainID + " to " + strain + "(" + rating + ") " + " (Not yet implemented)");
        setValueAt(strain, selectedRow, 1);
        setValueAt(type, selectedRow, 2);
        setValueAt(rating, selectedRow, 3);
    }

    public void deleteStrain(int strainID) throws SQLException {
        // TODO: implement this
        System.out.println("Deleting strain id: " + strainID + " (Not yet implemented)");
    }
    
    public Vector<String> getFlavors(int strainID) throws SQLException {
        Vector<String> list = new Vector<>();
		String query =
			"SELECT Flavor " +
			"FROM flavor " +
			"WHERE flavor.id IN " +
					"(SELECT f.id " +
					"FROM flavor AS f " +
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
			"FROM effect " +
			"WHERE effect.id IN " +
				"(SELECT e.id " +
				"FROM effect AS e " +
				"WHERE e.Strain_id = ?)";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, strainID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
        
        return list;
    }
}
