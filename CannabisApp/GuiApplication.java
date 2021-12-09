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


/*
 *   REPURPOSED BY AUSTIN SLADE GETZ
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
    JButton		filterButton;
    JButton		moreInfoButton;
    
    JButton     quitButton;

    public static void main(String[] args) {
        GuiApplication app = new GuiApplication();
        app.open();
    }

    void open() {
        model = new GuiModel();
        filterBox = new FilterBox();
        filterBox.effectButton.addActionListener(this);
        filterBox.typeButton.addActionListener(this);
        filterBox.flavorButton.addActionListener(this);
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
        
        moreInfoButton = new JButton("More Info");
        moreInfoButton.setEnabled(false);
        moreInfoButton.addActionListener(this);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        buttonPane.add(moreInfoButton);
        buttonPane.add(quitButton);

        frame.pack();
        frame.setVisible(true);

    }

     // process action events
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.searchButton) {
            search();
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
        else if (e.getSource() == this.filterBox.effectButton) {
        	filterBox.switchEffects();
        }
        else if (e.getSource() == this.filterBox.flavorButton) {
        	filterBox.switchFlavors();
        }
		else if (e.getSource() == this.filterBox.typeButton) {
			filterBox.switchTypes();
		}
     }

    private void moreInfo() {
    	
    	try {
			JOptionPane.showMessageDialog(frame.getContentPane(), model.getStrainInfo(),  "INFO", JOptionPane.PLAIN_MESSAGE);
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
            moreInfoButton.setEnabled(false);
        }
        else {
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