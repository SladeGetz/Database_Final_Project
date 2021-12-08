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
	JComboBox orderBy;
	
	JButton typeButton;
	JButton flavorButton;
	JButton effectButton;
	
	public FilterBox() {
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		this.setSize(new Dimension(200, 500));
		this.setVisible(false);
		rating = new JTextField("0.0");
		
		types = new Vector<JCheckBox>();
		flavors = new Vector<JCheckBox>();
		effects = new Vector<JCheckBox>();
		
		orderBy = new JComboBox();
		orderBy.addItem("Strain");
		orderBy.addItem("Type");
		orderBy.addItem("Effects");
		orderBy.addItem("Flavors");
		orderBy.addItem("Rating");
		orderBy.setSelectedIndex(0);
		orderBy.setEditable(false);
		
		typeButton = new JButton("UNSELECT ALL");
		flavorButton = new JButton("UNSELECT ALL");
		effectButton = new JButton("UNSELECT ALL");
	}
	
	public void setUp() throws SQLException {
		String query =
				"SELECT Flavor " +
				"FROM flavors";
		PreparedStatement ps = db.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			flavors.add(new JCheckBox(rs.getString(1)));
		}
		
		query =
				"SELECT Effect " +
				"FROM effects";
		ps = db.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
			effects.add(new JCheckBox(rs.getString(1)));
		}
		
		query =
				"SELECT Type " +
				"FROM type";
		ps = db.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()) {
			types.add(new JCheckBox(rs.getString(1)));
		}
		
		
		add(setTypeBox());
		add(setFlavorBox());
		add(setEffectBox());
		add(new JLabel("Rating: "));
		add(rating);
		
		add(new JLabel("Order By"));
		add(orderBy);
		
		this.pack();
		
		
	}
	
	private JPanel setTypeBox() throws SQLException {
		
		
	
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("TYPES"));
		panel.add(typeButton);
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
		panel.add(flavorButton);
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
		panel.add(effectButton);
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
		System.out.println(ans.substring(0, ans.length()-2));
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
		System.out.println(ans.substring(0, ans.length()-2));
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
		System.out.println(ans.substring(0, ans.length()-2));
		return ans.substring(0, ans.length()-2);
	}
	
	
	
	public String getOrder() {
		
		if (orderBy.getSelectedItem().equals("Type")) {
			return "t.Type, s.Rating DESC, s.Strain, Flavors, Effects";
		}
		else if (orderBy.getSelectedItem().equals("Effects")) {
			return "Effects, s.Rating DESC, s.Strain, t.Type, Flavors";
		}
		else if (orderBy.getSelectedItem().equals("Flavors")) {
			return "Flavors, s.Rating DESC, s.Strain, t.Type, Effects";
		}
		else if (orderBy.getSelectedItem().equals("Rating")) {
			return "s.Rating DESC, s.Strain, t.Type, Flavors, Effects";	
		}

		return "s.Strain, s.Rating DESC, t.Type, Flavors, Effects";
		
	}
	

	
	 public void login(String username, String password) throws SQLException, ClassNotFoundException {
	        Class.forName("org.postgresql.Driver");
	        String connectString = "jdbc:postgresql://codd.mines.edu:5433/csci403";

	        db = DriverManager.getConnection(connectString, username, password);
	    }
	 
	 public void switchEffects() {
		 if (effectButton.getText().equals("UNSELECT ALL")) {
			 for (JCheckBox box: effects) {
				 box.setSelected(false);
			 }
			 effectButton.setText("SELECT ALL");
		 } else {
			 for (JCheckBox box: effects) {
				 box.setSelected(true);
			 }
			 effectButton.setText("UNSELECT ALL");
		 }
		 repaint();
	 }
	 public void switchTypes() {
		 if (typeButton.getText().equals("UNSELECT ALL")) {
			 for (JCheckBox box: types) {
				 box.setSelected(false);
			 }
			 typeButton.setText("SELECT ALL");
		 } else {
			 for (JCheckBox box: types) {
				 box.setSelected(true);
			 }
			 typeButton.setText("UNSELECT ALL");
		 }
		 repaint();
	 }
	 public void switchFlavors() {
		 if (flavorButton.getText().equals("UNSELECT ALL")) {
			 for (JCheckBox box: flavors) {
				 box.setSelected(false);
			 }
			 flavorButton.setText("SELECT ALL");
		 } else {
			 for (JCheckBox box: flavors) {
				 box.setSelected(true);
			 }
			 flavorButton.setText("UNSELECT ALL");
		 }
		 repaint();
	 }
	 
}
