/*
   Copyright 2008 Christopher Painter-Wakefield

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class GuiApplication implements ActionListener, ListSelectionListener {
    // Model
    GuiModel model;

    FilterBox filterBox;
    // UI components
    JFrame 		frame;

    
    JPanel		searchPane;
    JLabel      searchLabel;
    JTextField  searchText;
    JButton		searchButton;

    JPanel      resultsPane;
    JTable      searchResultsTable;

    JPanel      buttonPane;
    JButton     addStrainButton;
    JButton		filterButton;
    JButton		moreInfoButton;
    JButton     editStrainButton;
    JButton     deleteStrainButton;
    JButton     quitButton;

    public static void main(String[] args) {
        GuiApplication app = new GuiApplication();
        app.open();
    }

    void open() {
        model = new GuiModel();
        filterBox = new FilterBox();
        GuiLoginDialog loginDialog = new GuiLoginDialog(model, filterBox);
        loginDialog.open();
        try {
			filterBox.setUp();
		} catch (SQLException ex) {
			sqlExceptionHandler(ex);
		}
        
        frame = new JFrame("CannaSearch");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cp = frame.getContentPane();

        searchPane = new JPanel();
        searchPane.setLayout(new FlowLayout());
        searchPane.setBorder(new EtchedBorder());
        cp.add(searchPane, BorderLayout.NORTH);
        
        
        
        filterButton = new JButton("FILTERS");
        filterButton.addActionListener(this);
        searchPane.add(filterButton);
        

       

        searchText = new JTextField(30);
        searchPane.add(searchText);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPane.add(searchButton);

        resultsPane = new JPanel();
        resultsPane.setLayout(new FlowLayout());
        cp.add(resultsPane, BorderLayout.CENTER);
        
        
        
       

        searchResultsTable = new JTable();
        searchResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsTable.setShowGrid(false);
        searchResultsTable.getSelectionModel().addListSelectionListener(this);
        searchResultsTable.setModel(model);
        JScrollPane scroller = new JScrollPane(searchResultsTable);
        scroller.setPreferredSize(new Dimension(600, 350));
        resultsPane.add(scroller);

        buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        cp.add(buttonPane, BorderLayout.SOUTH);

        addStrainButton = new JButton("Add Strain");
        addStrainButton.addActionListener(this);
        editStrainButton = new JButton("Edit Strain");
        editStrainButton.setEnabled(false);
        editStrainButton.addActionListener(this);
        deleteStrainButton = new JButton("Delete Album");
        deleteStrainButton.setEnabled(false);
        deleteStrainButton.addActionListener(this);
        moreInfoButton = new JButton("More Info");
        moreInfoButton.setEnabled(false);
        moreInfoButton.addActionListener(this);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        buttonPane.add(addStrainButton);
        buttonPane.add(editStrainButton);
        buttonPane.add(moreInfoButton);
        buttonPane.add(deleteStrainButton);
        buttonPane.add(quitButton);

        frame.pack();
        frame.setVisible(true);

    }

     // process action events
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.searchButton) {
            search();
        }
        else if (e.getSource() == this.editStrainButton) {
            editStrain();
        }
        else if (e.getSource() == this.deleteStrainButton) {
            deleteStrain();
        }
        else if (e.getSource() == this.quitButton) {
            System.exit(0);
        }
        else if (e.getSource() == this.filterButton) {
        	filterBox.setVisible(true);
        }
        else if (e.getSource() == this.moreInfoButton) {
        	moreInfo();
        }
     }

//    private void deleteAlbum() {
//        if (JOptionPane.showConfirmDialog(frame,
//                "Are you sure?",
//                "Delete Album",
//                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//            try {
//                model.deleteAlbum(model.getAlbumID());
//                model.removeSelectedRow();
//            } catch (SQLException e) {
//                sqlExceptionHandler(e);
//            }
//        }
//    }

    private void editStrain() {
        GuiEditStrainDialog dlg = new GuiEditStrainDialog(model);
        dlg.openForEdit(model.getStrainID(), model.getStrain(), model.getStrainType(), model.getStrainRating());
    }

    private void addStrain() {
        GuiEditStrainDialog dlg = new GuiEditStrainDialog(model);
        dlg.openForInsert();
    }
    private void deleteStrain() {
    	try {
			model.deleteStrain(searchResultsTable.getSelectedRow());
		} catch (SQLException e) {
			sqlExceptionHandler(e);
		}
    }
    private void moreInfo() {
    	
    	try {
			JOptionPane.showMessageDialog(frame, model.getStrainInfo());
		} catch (SQLException e) {
			sqlExceptionHandler(e);
		}
    }
    
    private void search() {
        try {
            model.search(filterBox, searchText.getText());
        } catch (SQLException ex) {
            sqlExceptionHandler(ex);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (searchResultsTable.getSelectedRow() == -1) {
            editStrainButton.setEnabled(false);
            deleteStrainButton.setEnabled(false);
            moreInfoButton.setEnabled(false);
        }
        else {
            editStrainButton.setEnabled(true);
            deleteStrainButton.setEnabled(true);
            moreInfoButton.setEnabled(true);
        }
        model.setSelectedRow(searchResultsTable.getSelectedRow());
    }

  
    
    private void sqlExceptionHandler(SQLException e) {
        JOptionPane.showMessageDialog(frame,
                "Database error: " + e.getMessage(),
                "Database error",
                ERROR_MESSAGE);
    }

}