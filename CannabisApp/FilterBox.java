import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterBox extends JDialog  {
	Connection db;
	
	Vector<JCheckBox> types;
	Vector<JCheckBox> flavors;
	Vector<JCheckBox> effects;
	
	JTextField rating;
	JComboBox order;
	
	public FilterBox() {
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		this.setSize(new Dimension(200, 500));
		this.setVisible(false);
		rating = new JTextField("0.0");
		
		types = new Vector<JCheckBox>();
		flavors = new Vector<JCheckBox>();
		effects = new Vector<JCheckBox>();
		
		
		
	}
	public void setUp() throws SQLException {
//		String query =
//				"SELECT Flavor " +
//				"FROM flavor";
//		PreparedStatement ps = db.prepareStatement(query);
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//			flavors.add(new JCheckBox(rs.getString(1)));
//		}
//		
//		query =
//				"SELECT Effect " +
//				"FROM effect";
//		ps = db.prepareStatement(query);
//		rs = ps.executeQuery();
//		while (rs.next()) {
//			effects.add(new JCheckBox(rs.getString(1)));
//		}
//		
//		query =
//				"SELECT Type " +
//				"FROM type";
//		ps = db.prepareStatement(query);
//		rs = ps.executeQuery();
//		while (rs.next()) {
//			types.add(new JCheckBox(rs.getString(1)));
//		}
//		
		flavors.add(new JCheckBox("Citrus"));
		types.add(new JCheckBox("Hybrid"));
		effects.add(new JCheckBox("Deddy"));
		add(setTypeBox());
		add(setFlavorBox());
		add(setEffectBox());
		add(new JLabel("Rating: "));
		add(rating);
		this.pack();
		
		
	}
	
	private JPanel setTypeBox() throws SQLException {
		
		
	
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("TYPES"));
		for (JCheckBox box: types) {
			box.setSelected(true);
			panel.add(box);
		}
		return panel;
	}
	
	private JPanel setFlavorBox() throws SQLException {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("FLAVORS"));
		for (JCheckBox box: flavors) {
			box.setSelected(true);
			panel.add(box);
		}
		return panel;
	}
	private JPanel setEffectBox()  {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("EFFECTS"));
		for (JCheckBox box: effects) {

			box.setSelected(true);
			panel.add(box);
		}
		return panel;
	}
	
	
	public String getMinRating() {
		if (Double.parseDouble(rating.getText()) > 5.0) {
			rating.setText("5.0");
		}
		if (Double.parseDouble(rating.getText()) < 0.0) {
			rating.setText("0.0");
		}
		return rating.getText();
	}
	
	public String getEffects() {
		String ans = "";
		int count = 0;
		for (JCheckBox box: effects) {
			if (box.isSelected()) {
				ans += count + ", ";
			}
			count++;
		}
		return ans.substring(0, ans.length()-2);
	}
	
	public String getFlavors() {
		String ans = "";
		int count = 0;
		for (JCheckBox box: flavors) {
			if (box.isSelected()) {
				ans += count + ", ";
			}
			count++;
		}
		return ans.substring(0, ans.length()-2);
	}
	
	public String getTypes() {
		String ans = "";
		int count = 0;
		for (JCheckBox box: types) {
			if (box.isSelected()) {
				ans += count + ", ";
			}
			count++;
		}
		return ans.substring(0, ans.length()-2);
	}
	
	public String getOrder() {
		return "s.Strain, s.Rating, t.Type";
	}
	

	
	 public void login(String username, String password) throws SQLException, ClassNotFoundException {
	        Class.forName("org.postgresql.Driver");
	        String connectString = "jdbc:postgresql://codd.mines.edu:5433/csci403";

	        db = DriverManager.getConnection(connectString, username, password);
	    }
}
