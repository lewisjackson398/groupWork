import javax.swing.*;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChangePassDlg extends JDialog implements ActionListener
{
    private User currentUser;
    private Database database;
    private List<User> allUsers;
    private JLabel lblOldPassword, lblConfirmNewPass, lblNewPassword;
    private JPasswordField fieldOldPassword, fieldConfirmNewPass, fieldNewPassword;
    private JButton btnConfirm, btnCancel;
    private JFrame frame;

    public ChangePassDlg(int userID) {
    	
        database = new Database();
        currentUser = database.getUser(userID);
        
        frame = new JFrame();
        frame.getContentPane().setBackground(SystemColor.menu);
        frame.setBounds(100, 100, 340, 162);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
		
		lblOldPassword = new JLabel("Current password");
		lblOldPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		lblOldPassword.setBounds(10, 11, 97, 14);
		frame.getContentPane().add(lblOldPassword);
		
		fieldOldPassword = new JPasswordField();
		lblOldPassword.setLabelFor(fieldOldPassword);
		fieldOldPassword.setBounds(142, 8, 172, 20);
		frame.getContentPane().add(fieldOldPassword);
		fieldOldPassword.setColumns(10);
		
		lblNewPassword = new JLabel("New password");
		lblNewPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNewPassword.setBounds(10, 39, 97, 14);
		frame.getContentPane().add(lblNewPassword);
		
		fieldNewPassword = new JPasswordField();
		lblNewPassword.setLabelFor(fieldNewPassword);
		fieldNewPassword.setColumns(10);
		fieldNewPassword.setBounds(142, 36, 172, 20);
		frame.getContentPane().add(fieldNewPassword);
		
		lblConfirmNewPass = new JLabel("Confirm new password");
		lblConfirmNewPass.setFont(new Font("Arial", Font.PLAIN, 11));
		lblConfirmNewPass.setBounds(10, 67, 134, 14);
		frame.getContentPane().add(lblConfirmNewPass);
		
		fieldConfirmNewPass = new JPasswordField();
		lblConfirmNewPass.setLabelFor(fieldConfirmNewPass);
		fieldConfirmNewPass.setColumns(10);
		fieldConfirmNewPass.setBounds(142, 64, 172, 20);
		frame.getContentPane().add(fieldConfirmNewPass);
		
		btnConfirm = new JButton("Submit");
		btnConfirm.setFont(new Font("Arial", Font.PLAIN, 11));
		btnConfirm.setBounds(65, 92, 89, 23);
		frame.getContentPane().add(btnConfirm);
		btnConfirm.addActionListener(this);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Arial", Font.PLAIN, 11));
		btnCancel.setBounds(164, 92, 89, 23);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(this);

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnConfirm) {
            String currentPassword = convertPassword(fieldOldPassword);
            String newPassword = convertPassword(fieldNewPassword);
            String newPasswordConfirm = convertPassword(fieldConfirmNewPass);

            if (verifyPassword(currentPassword)) {

                if (newPassword.equals(newPasswordConfirm)) {
                    if (isPasswordValid(newPassword)) {
                        database.update("users", currentUser.getUserID(), "password", newPassword);
                        JOptionPane.showMessageDialog(null, "Password changed successfully!");
                        this.frame.dispose();
                    }
                    else {

                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
            	JOptionPane.showMessageDialog(null, "Passwords is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (src == btnCancel) {
            this.frame.dispose();
        }
    }

    public boolean verifyPassword(String password) {
    boolean output = false;
    if (password.equals(currentUser.getPassword())) {
        output = true;
    }
    return output;
    }

    public String convertPassword(JPasswordField password) {
        char[] passwordChar = password.getPassword();
        return new String(passwordChar);
    }

    public boolean isPasswordValid(String password) {
        boolean output = false;
        boolean caps = false;
        boolean number = false;
        System.out.print(password);
        System.out.print(" is your new password!");
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password too short");
            output = false;
        }
        else {
            for (char c : password.toCharArray()) {
                //System.out.println(c);
                if (Character.isDigit(c)) {
                   // System.out.print(c);
                    number = true;
                }
                else if(caps && number) {
                    output = true;
                    break;
                }
                if(Character.isUpperCase(c)) {
                   // System.out.print(c);
                    caps = true;
                }
                else if (caps && number) {
                    output = true;
                    break;
                }
            }
        }
        return output;
    }
}

