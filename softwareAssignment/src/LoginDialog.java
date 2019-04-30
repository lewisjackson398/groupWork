import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import java.util.List;

public class LoginDialog extends JDialog implements ActionListener {

	Database database;
    List<User> allUsers;
    private int currentUserID;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JButton btnSubmit, btnCancel;
	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signIn;

    public LoginDialog()
    {
		frame = new JFrame("Vulture Services");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		usernameField = new JTextField();
		usernameField.setBounds(113, 69, 196, 29);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(113, 52, 196, 14);
		frame.getContentPane().add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(113, 109, 196, 14);
		frame.getContentPane().add(passwordLabel);
		
		signIn = new JButton("Sign In");
		signIn.setBounds(113, 171, 196, 29);
		signIn.addActionListener(this);
		frame.getContentPane().add(signIn);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(113, 125, 196, 29);
		frame.getContentPane().add(passwordField);
		frame.setVisible(true);
    }
  
    public boolean verifyCredentials(String username, String password) {
        boolean output = false;
        database = new Database();
        allUsers = database.getUsers();
        for (User user : allUsers) {
            if (username.equals(user.getName()) && password.equals(user.getPassword())) {
                output = true;
                setCurrentUser(user.getUserID());
                break;
            } else {
                output = false;
            }
        }
        return output;
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == signIn) {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            if (verifyCredentials(username, password)) { 
            	System.out.println("Success! User " + username + " has logged in.");
            	this.frame.dispose();
            	MainMenu menu = new MainMenu(getCurrentUser());
            }
            else {
                JOptionPane.showMessageDialog(null, "Incorrect username/password.");
            }
        }
    } 
    
    public int getCurrentUser() {
        return currentUserID;
    }

    public void setCurrentUser(int id) {
    	currentUserID = id;
    }

    public void resetLogin() {
        fieldUsername.setText("");
        fieldPassword.setText("");
    }
}