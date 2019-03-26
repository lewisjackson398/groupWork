package softwareengineeringpractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class TaskGUI extends JFrame
{
    //An array to hold all courses
    String roles[];
    // All modules
    String jobs[][];
	
    public TaskGUI() 
    {
	//hard code the values for now as we are not using database
	jobs = new String[][] 
        {
            {"#1", "Job 1"},
            {"#2", "Job 2"},
            {"#3", "Job 3"},
            {"#4", "Job 4"},
            {"#5", "Job 5"},
            {"#6", "Job 6"},
            {"#7", "Job 7"},			
	};

	setResizable(false);
	setBounds(new Rectangle(200, 200, 600, 400));
	getContentPane().setLayout(new BorderLayout(0, 0));
		
	//Auto generated elements here		
	JPanel pnlTop = new JPanel();
	getContentPane().add(pnlTop, BorderLayout.NORTH);
	pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
	JLabel lblCourses = new JLabel("Jobs");
	pnlTop.add(lblCourses);
		
	JPanel pnlBody = new JPanel();
	getContentPane().add(pnlBody, BorderLayout.CENTER);
	pnlBody.setLayout(null);
		
	JPanel pnlLeft = new JPanel();
	pnlLeft.setBounds(10, 11, 237, 320);
	pnlBody.add(pnlLeft);
	pnlLeft.setLayout(null);
		
	JLabel lblAllJobs = new JLabel("All Jobs");
	lblAllJobs.setHorizontalAlignment(SwingConstants.CENTER);
	lblAllJobs.setBounds(65, 11, 103, 23);
	pnlLeft.add(lblAllJobs);
		
	// Add table for all modules
	JTable tblAllJobs = new JTable();
		
	// Create a string array to hold column names
	String tableHeader[] = new String[] {"Job Code", "Job Name"};	
		
	//Create a table model for combo box to hold modules data	
	DefaultTableModel mdlAllJobs = new DefaultTableModel(jobs, tableHeader);
		
	//Make the table use the model we created
	tblAllJobs.setModel(mdlAllJobs);
		
	JScrollPane scrLeft = new JScrollPane(tblAllJobs);
	scrLeft.setBounds(10, 45, 217, 264);
	pnlLeft.add(scrLeft);
		
	JPanel pnlRight = new JPanel();
	pnlRight.setBounds(347, 11, 237, 320);
	pnlBody.add(pnlRight);
	pnlRight.setLayout(null);
		
	JLabel lblAssignedJobs = new JLabel("Assigned Jobs");
	lblAssignedJobs.setHorizontalAlignment(SwingConstants.CENTER);
	lblAssignedJobs.setBounds(76, 11, 103, 23);
	pnlRight.add(lblAssignedJobs);

	// Do similarly
	//Create a table model for combo box to hold assigned data	
	DefaultTableModel mdlAssigned = new DefaultTableModel(null, tableHeader);
		
	/// Add table for assigned modules
	JTable tblAssignedJobs = new JTable();
		
	//Make the table use the model we created
	tblAssignedJobs.setModel(mdlAssigned);
		
	JScrollPane scrRight = new JScrollPane(tblAssignedJobs);
	scrRight.setBounds(10, 45, 217, 264);
	pnlRight.add(scrRight);
		
	JButton btnMoveRight = new JButton("=>");
	btnMoveRight.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {
		// get the selected row number
		int selectedRow = tblAllJobs.getSelectedRow();				
		if (selectedRow <0) 
                {
                    // Show error message if button is clicked before selecting a row				
                    JOptionPane.showMessageDialog(getContentPane(), "Select a row from left first", "Problem", JOptionPane.ERROR_MESSAGE);
                }
		else
		{
                    // Move selected row to the other side by changing corresponding models					
                    //Take data from previous model 
                    String tempCode = mdlAllJobs.getValueAt(selectedRow, 0).toString();
                    String tempname = mdlAllJobs.getValueAt(selectedRow, 1).toString();
					
                    //Remove the corresponding row
                    mdlAllJobs.removeRow(selectedRow);
					
                    //Add the data to the other table
                    mdlAssigned.addRow(new String[] {tempCode,tempname});
                }		
            }
        });
                
	btnMoveRight.setBounds(257, 122, 80, 23);
	pnlBody.add(btnMoveRight);
		
	JButton btnMoveLeft = new JButton("<=");
	btnMoveLeft.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {
		// Similar implementation
		// get the selected row number
		int selectedRow = tblAssignedJobs.getSelectedRow();				
		if (selectedRow <0) 
                {
                    // Show error message if button is clicked before selecting a row				
                    JOptionPane.showMessageDialog(getContentPane(), "Select a row from right first", "Problem", JOptionPane.ERROR_MESSAGE);
                }
		else
		{
                    // Move selected row to the other side by changing corresponding models					
                    //Take data from previous model 
                    String tempCode = mdlAssigned.getValueAt(selectedRow, 0).toString();
                    String tempname = mdlAssigned.getValueAt(selectedRow, 1).toString();
					
                    //Remove the corresponding row
                    mdlAssigned.removeRow(selectedRow);
					
                    //Add the data to the other table
                    mdlAllJobs.addRow(new String[] {tempCode,tempname});
		}		
            }
	});
                
        btnMoveLeft.setBounds(257, 156, 80, 23);
        pnlBody.add(btnMoveLeft);
		
	JButton btnAdd = new JButton("Add Job");
	btnAdd.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {				
		// Add a module
		// You need to ask user for input (module code and module name)
		/// Make a custom dialog? 
		JLabel lblCode = new JLabel("Job Code");
		JTextField txtCode = new JTextField();
		JLabel lblName = new JLabel("Job Name");
		JTextField txtName = new JTextField();
				
		// Create array of components
		JComponent inputs[] = new JComponent[] { lblCode, txtCode, lblName, txtName };
				
		// Create input dialog using the component array
		int result = JOptionPane.showConfirmDialog(getContentPane(), inputs, "Enter job details", JOptionPane.PLAIN_MESSAGE);
				
		// Check if pressed OK 
		if (result==0) 
                {
                    //Add the corresponding row to table model
                    //Add the data to the other table
                    mdlAllJobs.addRow(new String[] {txtCode.getText(),txtName.getText()});
		}		
				
            }
	});
                
        btnAdd.setBounds(257, 242, 80, 23);
	pnlBody.add(btnAdd);
		
	JButton btnDelete = new JButton("Delete Job");
	btnDelete.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                //Delete a module from the all module table
		// get the selected row number
		int selectedRow = tblAllJobs.getSelectedRow();				
		if (selectedRow <0) 
                {
                    // Show error message if button is clicked before selecting a row				
                    JOptionPane.showMessageDialog(getContentPane(), "Select a row from left first", "Problem", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
                    //Remove the corresponding row
                    mdlAllJobs.removeRow(selectedRow);					
		}		
            }
	});
                
	btnDelete.setBounds(257, 276, 80, 23);
	pnlBody.add(btnDelete);
		
	// Just for looks 
	try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
        catch (Exception e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
    public static void main(String[] args) 
    {
        TaskGUI gui = new TaskGUI();
        gui.setVisible(true);
    }
}