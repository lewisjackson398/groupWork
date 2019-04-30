import java.awt.*;
import java.util.List;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.io.*;
import java.text.SimpleDateFormat;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableRowSorter;

public class MainMenu extends JFrame implements ActionListener {
	
    private Database database;
    private User currentUser;
    private List<User> users;
    private List<Integer> userIDs;
    private JButton addTaskButton, searchButton, saveButton, deleteButton, refreshButton, clearButton;

    private JFrame frame;
    private JTable myTasksList, regularTasksList, oneOffTasksList;
    private JButton logoutButton;
    private DefaultTableModel myTasksTable, regularTasksTable, oneOffTasksTable;
    private JComboBox regualarTasksComboBox, oneOffTasksComboBox, myTasksStatusComboBox, myTasksExpectedTimeComboBox;
    private JButton adminButton, changePassword;
    private JTextField searchField;
    private JTabbedPane tabbedPane;

    /**
     * This method creates a JFrame menu which
     * displays all the information needed.
     * @param userID This is user ID to be retrieved from database as a currentUser.
     */
    public MainMenu(int userID) {
       
    	database = new Database();
        currentUser = database.getUser(userID);
        users = database.getUsers();
        
		userIDs = new ArrayList<Integer>();
		for(User user: users) {
			userIDs.add(user.getUserID());
		}
       
        frame = new JFrame("Main Menu | Vulture Services");
        frame.setBounds(100, 100, 750, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		logoutButton = new JButton("Logout");
		logoutButton.setBounds(650, 11, 75, 29);
		frame.getContentPane().add(logoutButton);
		logoutButton.addActionListener(this);
		
		changePassword = new JButton("Change password");
		changePassword.addActionListener(this);
		changePassword.setBounds(505, 11, 140, 29);
		frame.getContentPane().add(changePassword);
		
		if(currentUser.accessLevel > 1) {
			adminButton = new JButton("Manage users");
			adminButton.addActionListener(this);
			adminButton.setBounds(375, 11, 125, 29);
			frame.getContentPane().add(adminButton);
			
			addTaskButton = new JButton("Add task");
			frame.getContentPane().add(addTaskButton);
			addTaskButton.addActionListener(this);
			addTaskButton.setBounds(280, 11, 90, 29);
		}
		
		JLabel lblLoggedInAs = new JLabel("Logged in as: " + currentUser.getName());
		lblLoggedInAs.setFont(new Font("Arial", Font.PLAIN, 13));
		lblLoggedInAs.setBounds(10, 14, 315, 20);
		frame.getContentPane().add(lblLoggedInAs);
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 58, 714, 240);
		frame.getContentPane().add(tabbedPane);
		
		addMyTasksTab(tabbedPane);
		addRegularTasks(tabbedPane);
		addOneOffTasks(tabbedPane);
		
		if(currentUser.getAccessLevel() > 1) {
			tabbedPane.setEnabledAt(0, false);
			tabbedPane.setSelectedIndex(1);
		}
		
		searchField = new JTextField();
		searchField.setBounds(10, 330, 150, 20);
		frame.getContentPane().add(searchField);
		searchField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Filter");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNewLabel.setBounds(10, 310, 150, 14);
		frame.getContentPane().add(lblNewLabel);
		
		searchButton = new JButton("Search..");
		searchButton.setFont(new Font("Arial", Font.PLAIN, 11));
		searchButton.setBounds(167, 329, 75, 23);
		frame.getContentPane().add(searchButton);
		searchButton.addActionListener(this);
		
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Arial", Font.PLAIN, 11));
		clearButton.setBounds(247, 329, 75, 23);
		frame.getContentPane().add(clearButton);
		clearButton.addActionListener(this);
		
		refreshButton = new JButton("Refresh");
		refreshButton.setFont(new Font("Arial", Font.BOLD, 11));
		refreshButton.setBounds(644, 324, 81, 29);
		frame.getContentPane().add(refreshButton);
		refreshButton.addActionListener(this);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setFont(new Font("Arial", Font.BOLD, 11));
		saveButton.setBounds(575, 324, 65, 29);
		frame.getContentPane().add(saveButton);
		
		if(currentUser.getAccessLevel() > 1)
		{
			deleteButton = new JButton("Delete");
			deleteButton.addActionListener(this);
			deleteButton.setFont(new Font("Arial", Font.BOLD, 11));
			deleteButton.setBounds(490, 324, 80, 29);
			frame.getContentPane().add(deleteButton);
		}

		frame.setVisible(true);       
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
	
        if (src == saveButton) {
        	switch(tabbedPane.getSelectedIndex()) {
        		case 0: {
                	if (myTasksList.isEditing())
                		myTasksList.getCellEditor().stopCellEditing();
                	
                	if(myTasksList.getSelectedRow() != -1) {
                    	int taskID = (Integer) myTasksList.getValueAt(myTasksList.getSelectedRow(), 0);
                    	
                    	if(myTasksStatusComboBox.getSelectedItem().toString().equals("Done")) {
                        	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        	database.update("tasks", taskID, "status", "1");
                        	database.update("tasks", taskID, "end_date", timeStamp);
                    	}
                    	else {
                    		database.update("tasks", taskID, "status", "0");
                    		database.update("tasks", taskID, "end_date", "No date yet");
                    	}
                    	
                    	int expectedTimeValue = myTasksExpectedTimeComboBox.getSelectedIndex() + 1;
                    	database.update("tasks", taskID, "expected_time", Integer.toString(expectedTimeValue));
                	}
        		}
        		case 1: {
                	if (regularTasksList.isEditing())
                		regularTasksList.getCellEditor().stopCellEditing();
                	
                	if(regularTasksList.getSelectedRow() != -1) {
                    	String newValue = userIDs.get(regualarTasksComboBox.getSelectedIndex()).toString();
                    	int taskID = (Integer) regularTasksList.getValueAt(regularTasksList.getSelectedRow(), 0);
                    	database.update("tasks", taskID, "assigned_to", newValue);
                	}
        		}
        		case 2: {
                	if (oneOffTasksList.isEditing())
                		oneOffTasksList.getCellEditor().stopCellEditing();
                	
                	if(oneOffTasksList.getSelectedRow() != -1) {
                    	String newValue = userIDs.get(oneOffTasksComboBox.getSelectedIndex()).toString();
                    	int taskID = (Integer) oneOffTasksList.getValueAt(oneOffTasksList.getSelectedRow(), 0);
                    	database.update("tasks", taskID, "assigned_to", newValue);
                	}
        		}
        	}
        }
        else if (src == clearButton) {
        	TableRowSorter<TableModel> sorter;
        	switch(tabbedPane.getSelectedIndex()) {
        		case 0: {
                    sorter = new TableRowSorter<>(((DefaultTableModel) myTasksList.getModel()));
                    myTasksList.setRowSorter(null);
                    myTasksList.setAutoCreateRowSorter(true);
                    break;
        		}
        		case 1: {
                    sorter = new TableRowSorter<>(((DefaultTableModel) regularTasksList.getModel()));
                    regularTasksList.setRowSorter(null);
                    regularTasksList.setAutoCreateRowSorter(true);
        			break;
        		}
        		case 2: {
                    sorter = new TableRowSorter<>(((DefaultTableModel) oneOffTasksList.getModel()));
                    oneOffTasksList.setRowSorter(null);
                    oneOffTasksList.setAutoCreateRowSorter(true);
        			break;
        		}
        	}

        }
        else if (src == deleteButton) {
        	switch(tabbedPane.getSelectedIndex()) {
	    		case 1: {
	            	if (regularTasksList.isEditing())
	            		regularTasksList.getCellEditor().stopCellEditing();
	            	
	            	if(regularTasksList.getSelectedRow() != -1) {
	                	String taskID = regularTasksList.getValueAt(regularTasksList.getSelectedRow(), 0).toString();
	                	int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this task?\nTask ID: " + taskID, "Warning!", JOptionPane.YES_NO_OPTION);
	                	if(dialogResult == JOptionPane.YES_OPTION) {
		                	database.delete("tasks", "id", "=", taskID);
		                	refreshButton.doClick();
	                	}
	            	}
	    		}
	    		case 2:
	    		{
	            	if (oneOffTasksList.isEditing())
	            		oneOffTasksList.getCellEditor().stopCellEditing();
	            	
	            	if(oneOffTasksList.getSelectedRow() != -1) {
	                	String taskID = oneOffTasksList.getValueAt(oneOffTasksList.getSelectedRow(), 0).toString();
	                	int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this task?\nTask ID: " + taskID, "Warning!", JOptionPane.YES_NO_OPTION);
	                	if(dialogResult == JOptionPane.YES_OPTION) {
		                	database.delete("tasks", "id", "=", taskID);
		                	refreshButton.doClick();
	                	}
	            	}
	    		}
        	}
        }
        else if (src == refreshButton) {
        	
	        /*	These two are required to update users list so the dropdown wouldn't crash when the new user is added	*/
        	DefaultComboBoxModel regularModel = (DefaultComboBoxModel) regualarTasksComboBox.getModel();
        	regularModel.removeAllElements();
        	
        	DefaultComboBoxModel oneOffModel = (DefaultComboBoxModel) oneOffTasksComboBox.getModel();
        	oneOffModel.removeAllElements();
        	
        	users = database.getUsers();
        	
			for(User user: users) {
				userIDs.clear();
			}
			
			for(User user: users) {
				userIDs.add(user.getUserID());
				regualarTasksComboBox.addItem(user.getName());
				oneOffTasksComboBox.addItem(user.getName());
			}
			
        	switch(tabbedPane.getSelectedIndex()) {
        		case 0: {
        			for(int i = myTasksTable.getRowCount() - 1; i > -1; i--) {
        				myTasksTable.removeRow(i);
        			}
        			
        	        List<Task> tasks = new ArrayList<Task>();
        	        tasks = database.getTasks();
        	        
        	        for(Task task : tasks){
        	        	if(task.getAssignedTo() == currentUser.getUserID()) {
        	        		myTasksTable.addRow(new Object[]{
        	        			task.getId(),
        	        			task.getTitle(), 
        	        			task.getPriority() + "", 
        	        			task.getStatus() ? "Done" : "Not finished",  
        	        			task.getStartDate() + "", 
        	        			task.getEndDate() + "", 
        	        			task.getExpectedTimeTaken() + " days"});
        	        	}
        	    	}
        	        
        			break;
        		}
        		case 1: {
        			for(int i = regularTasksTable.getRowCount() - 1; i > -1; i--) {
        				regularTasksTable.removeRow(i);
        			}
        			
        	        List<Task> tasks = new ArrayList<Task>();
        	        tasks = database.getTasks();
        	        
        	        for(Task task : tasks){
        	        	if(task.getType().equals("regular")) {
        	        		
        	        		String assignedTo = "Unassigned";
        	        		for(User user: users) {
        	        			if(user.getUserID() == task.getAssignedTo())
        	        				assignedTo = user.getName().toString();
        	        		}
        	        		
        	        		regularTasksTable.addRow(new Object[]{
        	        			task.getId(),
    	            			task.getTitle(), 
    	            			task.getPriority() + "", 
    	            			task.getStatus() ? "Done" : "Not finished", 
    	            			assignedTo,
    	            			task.getStartDate() + "", 
    	            			task.getEndDate() + "", 
    	            			task.getExpectedTimeTaken() + " days"
        	            	});
        	        	}
        	    	}
        	        
        			break;
        		}
        		case 2: {
        			for(int i = oneOffTasksTable.getRowCount() - 1; i > -1; i--) {
        				oneOffTasksTable.removeRow(i);
        			}
        			
        	        List<Task> tasks = new ArrayList<Task>();
        	        tasks = database.getTasks();
        	        
        	        for(Task task : tasks){
        	        	if(task.getType().equals("one-off")) {
        	        		
        	        		String assignedTo = "Unassigned";
        	        		for(User user: users) {
        	        			if(user.getUserID() == task.getAssignedTo())
        	        				assignedTo = user.getName().toString();
        	        		}
        	        		
        	        		oneOffTasksTable.addRow(new Object[]{
    	        				task.getId(),
    	            			task.getTitle(), 
    	            			task.getPriority() + "", 
    	            			task.getStatus() ? "Done" : "Not finished", 
    	            			assignedTo,
    	            			task.getStartDate() + "", 
    	            			task.getEndDate() + "", 
    	            			task.getExpectedTimeTaken() + " days"
        	            	});
        	        	}
        	    	}
        	        
        			break;
        		}
        	}
        }
        else if (src == searchButton) {
        	switch(tabbedPane.getSelectedIndex()) {
        		case 0: {
                    TableRowSorter<TableModel> sorter = new TableRowSorter<>(((DefaultTableModel) myTasksList.getModel()));
                    sorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
                    myTasksList.setRowSorter(sorter);
        			break;
        		}
        		case 1 : {
                    TableRowSorter<TableModel> sorter = new TableRowSorter<>(((DefaultTableModel) regularTasksList.getModel()));
                    sorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
                    regularTasksList.setRowSorter(sorter);
        			break;
        		}
        		case 2: {
                    TableRowSorter<TableModel> sorter = new TableRowSorter<>(((DefaultTableModel) oneOffTasksList.getModel()));
                    sorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
                    oneOffTasksList.setRowSorter(sorter);
        			break;
        		}
        	}
        }
        else if (src == addTaskButton) {
        	AddTask task = new AddTask();
        }
        else if (src == logoutButton) {
            System.exit(0); // or this.frame.dispose(); ??
        }
        else if (src == adminButton) {
        	AdminMenu adminMenu = new AdminMenu();
        }
        else if (src == changePassword) {
            ChangePassDlg changePassDlg = new ChangePassDlg(currentUser.getUserID());
        }
    }

    public void addMyTasksTab(JTabbedPane tabbedPane) {
    	
    	JPanel panel = new JPanel();
		panel.setBounds(10, 45, 565, 171);
		frame.getContentPane().add(panel);
		
		myTasksTable = new DefaultTableModel();
		final TableRowSorter<TableModel> sorter = new TableRowSorter<>(myTasksTable);
		
		myTasksTable.addColumn("Task ID");
		myTasksTable.addColumn("Task");
		myTasksTable.addColumn("Priority");
		myTasksTable.addColumn("Status");
		myTasksTable.addColumn("Start Date");
		myTasksTable.addColumn("End Date");
		myTasksTable.addColumn("Expected Time");
		
        myTasksList = new JTable(myTasksTable);
        myTasksList.setRowHeight(25);
        myTasksList.setRowSorter(sorter);
        
        List<Task> tasks = new ArrayList<Task>();
        tasks = database.getTasks();
        
        for(Task task : tasks){
        	if(task.getAssignedTo() == currentUser.getUserID()) {
        		myTasksTable.addRow(new Object[]{
    				task.getId(),
        			task.getTitle(), 
        			task.getPriority() + "", 
        			task.getStatus() ? "Done" : "Not finished",  
        			task.getStartDate() + "", 
        			task.getEndDate() + "", 
        			task.getExpectedTimeTaken() + " days"});
        	}
    	}
		
        myTasksList.setBounds(0, 40, 300, 200);
        
        JScrollPane scrollPanel = new JScrollPane(myTasksList); 
        
        myTasksList.getColumnModel().getColumn(0).setPreferredWidth(10);
        myTasksList.getColumnModel().getColumn(2).setPreferredWidth(25);
        myTasksList.getColumnModel().getColumn(3).setPreferredWidth(25);
        myTasksList.getColumnModel().getColumn(6).setPreferredWidth(25);
        
		TableColumn col = myTasksList.getColumnModel().getColumn(3);
		myTasksStatusComboBox = new JComboBox();
		myTasksStatusComboBox.addItem("Not finished");
		myTasksStatusComboBox.addItem("Done");
		col.setCellEditor(new DefaultCellEditor(myTasksStatusComboBox));
		
		TableColumn expectedTimeCol = myTasksList.getColumnModel().getColumn(6);
		myTasksExpectedTimeComboBox = new JComboBox();
		for(int i = 1; i <= 24; i++) {
			myTasksExpectedTimeComboBox.addItem(i);
		}
		expectedTimeCol.setCellEditor(new DefaultCellEditor(myTasksExpectedTimeComboBox));
        
        tabbedPane.add("My tasks", scrollPanel);
    }
    
    public void addRegularTasks(JTabbedPane tabbedPane) {
    	
    	JPanel panel = new JPanel();
		panel.setBounds(10, 45, 565, 171);
		frame.getContentPane().add(panel);
		
		regularTasksTable = new DefaultTableModel();
		final TableRowSorter<TableModel> sorter = new TableRowSorter<>(regularTasksTable);
		
		regularTasksTable.addColumn("ID");
		regularTasksTable.addColumn("Task");
		regularTasksTable.addColumn("Priority");
		regularTasksTable.addColumn("Status");
		regularTasksTable.addColumn("Assigned To");
		regularTasksTable.addColumn("Start Date");
		regularTasksTable.addColumn("End Date");
		regularTasksTable.addColumn("Expected Time");
		
		regularTasksList = new JTable(regularTasksTable);
		regularTasksList.setRowHeight(25);
		regularTasksList.setRowSorter(sorter);

        List<Task> tasks = new ArrayList<Task>();
        tasks = database.getTasks();
        
        for(Task task : tasks){
        	if(task.getType().equals("regular")) {
        		
        		String assignedTo = "Unassigned";
        		for(User user: users) {
        			if(user.getUserID() == task.getAssignedTo())
        				assignedTo = user.getName().toString();
        		}
        		
        		regularTasksTable.addRow(new Object[]{
        			task.getId(),
        			task.getTitle(), 
        			task.getPriority() + "", 
        			task.getStatus() ? "Done" : "Not finished", 
        			assignedTo,
        			task.getStartDate() + "", 
        			task.getEndDate() + "", 
        			task.getExpectedTimeTaken() + " days"
        		});
        	}
    	}
		
        regularTasksList.setBounds(0, 40, 300, 200);
        
        JScrollPane scrollPanel = new JScrollPane(regularTasksList);
		TableColumn col = regularTasksList.getColumnModel().getColumn(4);
	    
		regualarTasksComboBox = new JComboBox();
		for(User user: users) {
			regualarTasksComboBox.addItem(user.getName());
		}
		
		col.setCellEditor(new DefaultCellEditor(regualarTasksComboBox));
		regularTasksList.setAutoCreateRowSorter(true);
		
        tabbedPane.add("Regular tasks", scrollPanel);
    }
    
    public void addOneOffTasks(JTabbedPane tabbedPane) {
    	
    	JPanel panel = new JPanel();
		panel.setBounds(10, 45, 565, 171);
		frame.getContentPane().add(panel);
		
		oneOffTasksTable = new DefaultTableModel();
		final TableRowSorter<TableModel> sorter = new TableRowSorter<>(oneOffTasksTable);
		
		oneOffTasksTable.addColumn("Task ID");
		oneOffTasksTable.addColumn("Task");
		oneOffTasksTable.addColumn("Priority");
		oneOffTasksTable.addColumn("Status");
		oneOffTasksTable.addColumn("Assigned To");
		oneOffTasksTable.addColumn("Start Date");
		oneOffTasksTable.addColumn("End Date");
		oneOffTasksTable.addColumn("Expected Time");
		
		oneOffTasksList = new JTable(oneOffTasksTable);
		oneOffTasksList.setRowHeight(25);
		oneOffTasksList.setRowSorter(sorter);

        List<Task> tasks = new ArrayList<Task>();
        tasks = database.getTasks();
        
        for(Task task : tasks){
        	if(task.getType().equals("one-off")) {
        		
        		String assignedTo = "Unassigned";
        		for(User user: users) {
        			if(user.getUserID() == task.getAssignedTo())
        				assignedTo = user.getName().toString();
        		}
        		
        		oneOffTasksTable.addRow(new Object[]{
    				task.getId(),
        			task.getTitle(), 
        			task.getPriority() + "", 
        			task.getStatus() ? "Done" : "Not finished",
        			assignedTo,
        			task.getStartDate() + "", 
        			task.getEndDate() + "", 
        			task.getExpectedTimeTaken() + " days"
        		});
        	}
    	}
		
        oneOffTasksList.setBounds(0, 40, 300, 200);
        
        JScrollPane scrollPanel = new JScrollPane(oneOffTasksList);
        
		TableColumn col = oneOffTasksList.getColumnModel().getColumn(4);
		oneOffTasksComboBox = new JComboBox();
		for(User user: users) {
			oneOffTasksComboBox.addItem(user.getName());
		}
		col.setCellEditor(new DefaultCellEditor(oneOffTasksComboBox));
		
		oneOffTasksList.setAutoCreateRowSorter(true);
        tabbedPane.add("One-off tasks", scrollPanel);
    }

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

