import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Random;


public class AdminMenu extends JFrame implements ActionListener {
    private Database database;
    private List<User> allUsers;
    private JFrame f;
    private JTabbedPane tp;
    private JComboBox<String> roleComboBox;
    private JButton btnExit;
    private String[] roles = {"Select one...", "Technician", "Administrator", "Manager"};

    public AdminMenu() {
        database = new Database();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 100, 400, 300);

        f = new JFrame("Admin | Vulture Services"); //creates new frame
        tp = new JTabbedPane(); //creates tabbed pane
        tp.setBounds(50, 50, 200, 200); //sets size of tabbed pane
        setupAddUser(tp);
        setupEditUser(tp);
        setupDeleteUser(tp);
        JPanel pnlMenuExit = new JPanel();
        btnExit = new JButton("Exit");
        btnExit.addActionListener(this);
        pnlMenuExit.add(btnExit);
        f.add(tp, BorderLayout.CENTER); //add pane to frame
        f.add(pnlMenuExit, BorderLayout.PAGE_END);
        f.setSize(600, 300); //set size of frame
        f.setVisible(true); //make frame visible
    }

    public void setupAddUser(JTabbedPane tp) {
        //create swing components
        JPanel tabAddUser = new JPanel();
        JLabel lblName = new JLabel("Name: ");
        JTextField fieldName = new JTextField(20);
        JLabel lblRole = new JLabel("Role: ");
        lblRole.setBounds(50, 50, 50, 50);
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JLabel lblTempPass = new JLabel("Temporary password: ");
        JTextField fieldTempPass = new JTextField("Generate temporary password");
        JButton btnGenerateTempPass = new JButton("Generate temporary password");
        JButton btnConfirm = new JButton("Confirm");
        JButton btnReset = new JButton("Reset");

        //alter properties of components
        fieldTempPass.setEditable(false);
        btnConfirm.setEnabled(false);

        //add action listeners
        //generate temporary password button functionality
        btnGenerateTempPass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fieldTempPass.setText(generateTempPass());
                btnGenerateTempPass.setEnabled(false);
                btnConfirm.setEnabled(true);
            }
        });

        //confirm button functionality
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allUsers = database.getUsers();
                String name = fieldName.getText().trim();
                String role = (String) roleComboBox.getSelectedItem();
                String tempPass = fieldTempPass.getText();

                if (name.equals("") || role.equals("Select one...") || tempPass.equals("")) { //if fields are empty
                    JOptionPane.showMessageDialog(null, "Missing fields");
                } else {
                    if (JOptionPane.showConfirmDialog(null, "Add user?", "Confirm add user", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (role.equals("Technician")) {
                            role = "1";
                        } else if (role.equals("Administrator")) {
                            role = "2";
                        } else {
                            role = "3";
                        }
                        allUsers  = database.getUsers();
                        boolean nameTaken = false;
                        for (User user : allUsers) {
                            if (name.equals(user.getName())) {
                                nameTaken = true;
                                break;
                            }
                        }
                        if (!nameTaken) {
                            String[] fields = {"name", "password", "permissions"};
                            String[] values = {name, tempPass, role};
                            database.insert("users", fields, values);
                            JOptionPane.showMessageDialog(null,"User added successfully.");
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"That username has already been taken.");
                        }
                    }
                }
            }
        });

        //reset button functionality
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //reset all fields and buttons to default
                fieldName.setText("");
                roleComboBox.setSelectedIndex(0);
                fieldTempPass.setText("Generate a temporary password");
                btnGenerateTempPass.setEnabled(true);
                btnConfirm.setEnabled(false);
            }
        });

        //add components to tab
        tabAddUser.add(lblName);
        tabAddUser.add(fieldName);
        tabAddUser.add(lblRole);
        tabAddUser.add(roleComboBox);
        tabAddUser.add(lblTempPass);
        tabAddUser.add(fieldTempPass);
        tabAddUser.add(btnGenerateTempPass);
        tabAddUser.add(btnConfirm);
        tabAddUser.add(btnReset);

        //add tab to tabbed pane
        tp.add("Add user", tabAddUser);
    } //end of setupAddUser()

    public void setupEditUser(JTabbedPane tp) {
        allUsers = database.getUsers();
        JPanel tbEU = new JPanel(); //creates 'Edit user' tab
        JLabel lblselectUser = new JLabel("Select user: ");
        JComboBox<String> comboUsers = new JComboBox<String>();
        comboUsers.addItem("Select one...");
        for (User user : allUsers) {
            comboUsers.addItem(user.getName());
        }
        JLabel lblID = new JLabel("ID: ");
        JTextField fieldID = new JTextField("Update values to view");
        fieldID.setEditable(false);
        JLabel lblEUForename = new JLabel("Forename: ");
        JTextField fieldEUOldForename = new JTextField("Update values to view");
        fieldEUOldForename.setEditable(false);
        JTextField fieldEUNewForename = new JTextField(20);
        JLabel lblAccessLevel = new JLabel("Access level: ");
        JTextField fieldOldRole = new JTextField("Update values to view");
        fieldOldRole.setEditable(false);
        JButton btnEUUpdate = new JButton("Update values");
        btnEUUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allUsers = database.getUsers();
                String selected = comboUsers.getSelectedItem().toString();
                for (User user : allUsers) {
                    if (selected.equals(user.getName())) {
                        fieldID.setText(String.valueOf(user.getUserID()));
                        fieldEUOldForename.setText(user.getName());
                        fieldOldRole.setText(String.valueOf(user.getAccessLevel()));
                        btnEUUpdate.setEnabled(false);
                        break;
                    }
                }
            }
        });
        roleComboBox = new JComboBox<>(roles);
        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fieldEUNewForename.getText().equals("") || roleComboBox.getSelectedItem().equals("Select one...")) {
                    JOptionPane.showMessageDialog(null,"Missing fields");
                }
                else {
                    database.update("users", Integer.parseInt(fieldID.getText()), "name", fieldEUNewForename.getText());
                    String accessLevel;
                    String selectedRole = roleComboBox.getSelectedItem().toString();
                    if (selectedRole.equals("Technician")) {
                        accessLevel = "1";
                    } else if (selectedRole.equals("Administrator")) {
                        accessLevel = "2";
                    } else if (selectedRole.equals("Manager")) {
                        accessLevel = "3";
                    } else {
                        accessLevel = "0";
                    }
                    database.update("users", Integer.parseInt(fieldID.getText()), "permissions", accessLevel);
                    JOptionPane.showMessageDialog(null, "User edited successfully.");
                    btnEUUpdate.setEnabled(true);
                }
            }
        });
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comboUsers.setSelectedIndex(0);
                fieldID.setText("Update values to view");
                fieldEUNewForename.setText("");
                fieldEUOldForename.setText("Update values to view");
                fieldOldRole.setText("Update values to view");
                btnEUUpdate.setEnabled(true);
            }
        });

        tbEU.add(lblselectUser);
        tbEU.add(comboUsers);
        tbEU.add(btnEUUpdate);
        tbEU.add(lblID);
        tbEU.add(fieldID);
        tbEU.add(lblEUForename);
        tbEU.add(fieldEUOldForename);
        tbEU.add(fieldEUNewForename);
        tbEU.add(lblAccessLevel);
        tbEU.add(fieldOldRole);
        tbEU.add(roleComboBox);
        tbEU.add(btnConfirm);
        tbEU.add(btnReset);
        tp.add("Edit user", tbEU);
    }

    public void setupDeleteUser(JTabbedPane tp) {
        JPanel tbDU = new JPanel(); //creates 'Delete user' tab
        JLabel lblSelectUser = new JLabel("Select user: ");
        JComboBox<String> comboUsers = new JComboBox<String>();
        JButton btnUpdate = new JButton("Update");
        JLabel lblID = new JLabel("ID: ");
        JLabel lblName = new JLabel("Name: ");
        JTextField fieldID = new JTextField("Update values to view");
        JTextField fieldName = new JTextField("Update values to view");
        JButton btnConfirm = new JButton("Confirm");
        JButton btnReset = new JButton("Reset");

        comboUsers.addItem("Select one...");
        allUsers = database.getUsers();
        for (User user : allUsers) {
            comboUsers.addItem(user.getName());
        }

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allUsers = database.getUsers();
                String selected = comboUsers.getSelectedItem().toString();
                boolean found = false;

                for (User user : allUsers) {
                    if (selected.equals(user.getName())) {
                        fieldID.setText(String.valueOf(user.getUserID()));
                        fieldName.setText(user.getName());
                        btnUpdate.setEnabled(false);
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(null, "User not found");
                }
            }
        });

        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboUsers.getSelectedItem().toString().equals("Select one...")) {
                    JOptionPane.showMessageDialog(null,"You have not selected a user.");
                }
                    else {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete user?", "Delete user | Vulture Services", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    	database.delete("users", "id", "=", fieldID.getText());
                        JOptionPane.showMessageDialog(null, fieldName.getText() + " has been successfully deleted.");
                    }
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                comboUsers.setSelectedIndex(0);
                fieldID.setText("Update values to view");
                fieldName.setText("Update values to view");
                btnUpdate.setEnabled(true);

            }
        });

        tbDU.add(lblSelectUser);
        tbDU.add(comboUsers);
        tbDU.add(btnUpdate);
        tbDU.add(lblID);
        tbDU.add(fieldID);
        tbDU.add(lblName);
        tbDU.add(fieldName);
        tbDU.add(btnConfirm);
        tbDU.add(btnReset);
        tp.add("Delete user", tbDU);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == btnExit) {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Admin Menu | Vulture Services", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                f.dispose();
            }
        }
    }

    public String generateTempPass() {
        char[] alphaNum = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz012345789".toCharArray();
        Random random = new Random();
        StringBuilder string = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            char c = alphaNum[random.nextInt(alphaNum.length)];
            string.append(c);
        }
        return string.toString();
    }
}
