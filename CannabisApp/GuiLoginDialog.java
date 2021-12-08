import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class GuiLoginDialog extends JDialog implements ActionListener {
    JButton        loginButton;
    JButton        cancelButton;
    JTextField     usernameText;
    JPasswordField passwordField;

    GuiModel model;
    FilterBox filterBox;

    public GuiLoginDialog(GuiModel m, FilterBox b) {
        model = m;
        filterBox = b;
    }

    public void open() {
       setTitle("Login");

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(new JLabel("Username:"));
        usernameText = new JTextField(25);
        p1.add(usernameText);
        pane.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(new JLabel("Password:"));
        passwordField = new JPasswordField(25);
        p2.add(passwordField);
        pane.add(p2, BorderLayout.CENTER);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        p3.add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        p3.add(cancelButton);

        pane.add(p3, BorderLayout.SOUTH);

        // exit when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        getRootPane().setDefaultButton(loginButton);

        pack();
        setModal(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loginButton) {
            login();
        }
        else if (e.getSource() == this.cancelButton) {
            System.exit(0);
        }
    }

    private void login() {
        try {
        	model.login(usernameText.getText(), String.valueOf(passwordField.getPassword()));
        	filterBox.login(usernameText.getText(), String.valueOf(passwordField.getPassword()));
        	dispose();
        }
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "PostgreSQL driver not found, exiting.",
                    "Login error",
                    ERROR_MESSAGE);
            System.exit(1);
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Login error: " + e.getMessage(),
                    "Login error",
                    ERROR_MESSAGE);
        }
    }
}
