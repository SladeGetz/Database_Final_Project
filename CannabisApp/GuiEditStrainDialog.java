import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

class GuiEditStrainDialog extends JDialog implements ActionListener {
    GuiModel model;
    boolean  isNewStrain;
    int strainID;

    JTextField strainText;
    JComboBox typeCB;
    
    JTextField ratingText;
    JTextField descriptionText;
    
    JList flavors;
    JList effects;
    
    JButton saveButton;
    JButton cancelButton;

    public GuiEditStrainDialog(GuiModel m) {
        model = m;
    }
    public void openForInsert() {
        isNewStrain = true;

        setTitle("Add New Strain");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2, 2, 2));
        pane.add(p1, BorderLayout.CENTER);

        p1.add(new JLabel("Strain:"));
        strainText = new JTextField(20);
        p1.add(strainText);
        
        p1.add(new JLabel("Type:"));
        Vector<String> list = null;
        try {
            list = model.getTypes();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        
        
        typeCB = new JComboBox(list);
        p1.add(typeCB);
        
        p1.add(new JLabel("Rating:"));
        ratingText = new JTextField();
        p1.add(ratingText);
        
        p1.add(new JLabel("Flavors:"));
        list = null;
        try {
            list = model.getFlavors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        flavors = new JList(list);
        flavors.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        p1.add(flavors);
        
        p1.add(new JLabel("Effects:"));
        list = null;
        try {
            list = model.getEffects();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        effects = new JList(list);
        effects.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        p1.add(new JLabel("Description:"));
        descriptionText = new JTextField(30);
        p1.add(descriptionText);
        
        

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        p2.add(saveButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        p2.add(cancelButton);
        pane.add(p2, BorderLayout.SOUTH);

        pack();
        setModal(true);
        setVisible(true);
    }

    public void openForEdit(int id, String strain, String type, String rating) {
        isNewStrain = false;
        strainID = id;

        setTitle("Edit Strain");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2, 2, 2));
        pane.add(p1, BorderLayout.CENTER);

        p1.add(new JLabel("Strain:"));
        strainText = new JTextField(strain);
        p1.add(strainText);
        
        p1.add(new JLabel("Type:"));
        Vector<String> list = null;
        try {
            list = model.getTypes();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        
        
        typeCB = new JComboBox(list);
        typeCB.setSelectedItem(type);
        p1.add(typeCB);
        
        p1.add(new JLabel("Rating:"));
        ratingText = new JTextField(rating);
        p1.add(ratingText);
        
        p1.add(new JLabel("Flavors:"));
        list = null;
        try {
            list = model.getFlavors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        flavors = new JList(list);
        flavors.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        p1.add(flavors);
        
        p1.add(new JLabel("Effects:"));
        list = null;
        try {
            list = model.getEffects();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        effects = new JList(list);
        effects.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        p1.add(new JLabel("Description:"));
        descriptionText = new JTextField(30);
        p1.add(descriptionText);
        
        

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        p2.add(saveButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        p2.add(cancelButton);
        pane.add(p2, BorderLayout.SOUTH);

        pack();
        setModal(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        }
        else if (e.getSource() == saveButton) {
            if (ratingText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a title.",
                        "Error saving album",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if (isNewStrain) {
            	
                try {
                	Vector<String> flavor = new Vector<String>();
                	Vector<String> allFlavors = model.getFlavors();
                	for (int flav: flavors.getSelectedIndices()) {
                		flavor.add(allFlavors.elementAt(flav));
                	}
                	
                	Vector<String> effect = new Vector<String>();
                	Vector<String> allEffects = model.getEffects();
                	for (int fx: effects.getSelectedIndices()) {
                		effect.add(allEffects.elementAt(fx));
                	}
                    model.insertStrain(strainText.getText(), (String)typeCB.getSelectedItem(), ratingText.getText(), flavor, effect, descriptionText.getText());
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Database error: " + ex.getMessage(),
                            "Database error",
                            ERROR_MESSAGE);
                }
            } else {
            	
                try {
                	Vector<String> flavor = new Vector<String>();
                	Vector<String> allFlavors = model.getFlavors();
                	for (int flav: flavors.getSelectedIndices()) {
                		flavor.add(allFlavors.elementAt(flav));
                	}
                	
                	Vector<String> effect = new Vector<String>();
                	Vector<String> allEffects = model.getEffects();
                	for (int fx: effects.getSelectedIndices()) {
                		effect.add(allEffects.elementAt(fx));
                	}
                    model.updateStrain(strainID, strainText.getText(), (String)typeCB.getSelectedItem(), ratingText.getText(), flavor, effect, descriptionText.getText());
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Database error: " + ex.getMessage(),
                            "Database error",
                            ERROR_MESSAGE);
                }
            }
        }
    }
}
