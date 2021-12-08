import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

class GuiEditAlbumDialog extends JDialog implements ActionListener {
    GuiModel model;
    boolean  isNewAlbum;
    int albumID;

    JComboBox artistCB;
    JTextField artistText;
    JTextField titleText;
    JTextField yearText;
    JButton saveButton;
    JButton cancelButton;

    public GuiEditAlbumDialog(GuiModel m) {
        model = m;
    }
    public void openForInsert() {
        isNewAlbum = true;

        setTitle("Add New Album");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2, 2, 2));
        pane.add(p1, BorderLayout.CENTER);

        p1.add(new JLabel("Artist:"));

        Vector<String> list = null;
        try {
            list = model.getArtists();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Database error",
                    ERROR_MESSAGE);
            dispose();
        }
        artistCB = new JComboBox(list);
        p1.add(artistCB);

        p1.add(new JLabel("Album title:"));
        titleText = new JTextField();
        p1.add(titleText);

        p1.add(new JLabel("Year:"));
        yearText = new JTextField();
        p1.add(yearText);

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

    public void openForEdit(int id, String artist, String title, String year) {
        isNewAlbum = false;
        albumID = id;

        setTitle("Edit Album");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2, 2, 2));
        pane.add(p1, BorderLayout.CENTER);

        p1.add(new JLabel("Artist:"));
        artistText = new JTextField(artist);
        artistText.setEnabled(false);
        p1.add(artistText);

        p1.add(new JLabel("Album title:"));
        titleText = new JTextField(title);
        p1.add(titleText);

        p1.add(new JLabel("Year:"));
        yearText = new JTextField(year);
        p1.add(yearText);

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
            if (titleText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a title.",
                        "Error saving album",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if (isNewAlbum) {
//                try {
//                    model.insertAlbum((String)artistCB.getSelectedItem(), titleText.getText(), yearText.getText());
//                    dispose();
//                } catch (SQLException ex) {
//                    JOptionPane.showMessageDialog(this,
//                            "Database error: " + ex.getMessage(),
//                            "Database error",
//                            ERROR_MESSAGE);
//                }
//            } else {
//                try {
//                    model.updateAlbum(albumID, titleText.getText(), yearText.getText());
//                    dispose();
//                } catch (SQLException ex) {
//                    JOptionPane.showMessageDialog(this,
//                            "Database error: " + ex.getMessage(),
//                            "Database error",
//                            ERROR_MESSAGE);
//                }
            }
        }
    }
}
