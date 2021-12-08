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

    public String getStrainTitle() {
        if (selectedRow != -1) {
            return (String) getValueAt(selectedRow, 1);
        }
        else {
            return "";
        }
    }

    public String getStrainYear() {
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
                "FROM type AS t, type_xref AS tx, strain as S" +
                "WHERE lower(s.Strain) LIKE lower(?) " +
                "AND s.id = tx.Strain_ID" +
                "AND s.Rating >= ?" + // get min rating
                "AND s.id IN " + 
                	"(SELECT e.Strain_id, f.Strain_id" +
                	"FROM effects AS e, flavors, AS f" +
                	"WHERE e.id IN (?)" + // get selected effect ids
                	"WHERE f.id IN (?) )" + // get selected flavors 

                "AND t.id IN (?)" +
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

    public Vector<String> getArtists() throws SQLException {
        // TODO: replace this with something that queries the database
        Vector<String> list = new Vector<>();
        list.add("Chris Thile");
        list.add("Hiromi");
        list.add("Jethro Tull");
        return list;
    }

    public void insertArtist(String artist) throws SQLException {
        // TODO: implement this
        System.out.println("Inserting new artist: " + artist + " (Not yet implemented)");
    }

    public void insertStrain(String artist, String title, String year) throws SQLException {
        // TODO: implement this
        System.out.println("Inserting new strain: " + artist + " - " + title + "(" + year + ") " + " (Not yet implemented)");
    }

    public void updateStrain(int strainID, String title, String year) throws SQLException {
        // TODO: implement this
        System.out.println("Updating strain id " + strainID + " to " + title + "(" + year + ") " + " (Not yet implemented)");
        setValueAt(title, selectedRow, 1);
        setValueAt(year, selectedRow, 2);
    }

    public void deleteStrain(int strainID) throws SQLException {
        // TODO: implement this
        System.out.println("Deleting strain id: " + strainID + " (Not yet implemented)");
    }
}
