/**
* The AddTask class to add tasks by administrators and managers.
* @author	Gustas Kurtkus
* @version	1.0
* @since	2018-05-05
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddTask extends JFrame implements ActionListener {

	private JFrame frame;
	private JTextField title;
	private JButton cancelButton;
	private JButton submitButton;
	private JComboBox<String> priority;
	private JComboBox<String> type;
	private JComboBox<Integer> duration;

	/**
	 * This method is a constructor which creates a JFrame with
	 * a form inside to enter the details of a task.
	 */
	public AddTask() {
		
		frame = new JFrame("Add task");
		frame.setBounds(100, 100, 448, 210);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLoggedInAs = new JLabel("In order to add a task, you have to fill this form and press submit.");
		lblLoggedInAs.setFont(new Font("Arial", Font.BOLD, 13));
		lblLoggedInAs.setBounds(10, 14, 412, 20);
		frame.getContentPane().add(lblLoggedInAs);
		
		JLabel lblTaskTitle = new JLabel("Title");
		lblTaskTitle.setFont(new Font("Arial", Font.PLAIN, 11));
		lblTaskTitle.setBounds(10, 45, 46, 14);
		frame.getContentPane().add(lblTaskTitle);
		
		title = new JTextField();
		lblTaskTitle.setLabelFor(title);
		title.setBounds(10, 60, 200, 20);
		frame.getContentPane().add(title);
		title.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Priority");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNewLabel.setBounds(10, 91, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		priority = new JComboBox();
		lblNewLabel.setLabelFor(priority);
		priority.setBounds(66, 88, 144, 20);
		frame.getContentPane().add(priority);
		priority.addItem("low");
		priority.addItem("medium");
		priority.addItem("high");
		
		JLabel lblNewLabel_1 = new JLabel("Type");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(222, 63, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		type = new JComboBox();
		lblNewLabel_1.setLabelFor(type);
		type.setBounds(278, 60, 144, 20);
		frame.getContentPane().add(type);
		type.addItem("regular");
		type.addItem("one-off");
		
		JLabel lblExpectedDuration = new JLabel("Expected duration (days)");
		lblExpectedDuration.setFont(new Font("Arial", Font.PLAIN, 11));
		lblExpectedDuration.setBounds(222, 91, 130, 14);
		frame.getContentPane().add(lblExpectedDuration);
		
		duration = new JComboBox();
		lblExpectedDuration.setLabelFor(duration);
		duration.setBounds(362, 88, 60, 20);
		frame.getContentPane().add(duration);
		
		for(int i = 1; i <= 24; i++) {
			duration.addItem(i);
		}
		
		submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Arial", Font.PLAIN, 11));
		submitButton.setBounds(121, 137, 89, 23);
		frame.getContentPane().add(submitButton);
		submitButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Arial", Font.PLAIN, 11));
		cancelButton.setBounds(222, 137, 89, 23);
		frame.getContentPane().add(cancelButton);
		cancelButton.addActionListener(this);
		
		frame.setVisible(true);
	}

	/**
	 * This method is to listed for buttons and retrieve the
	 * data from them.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancelButton) {
			this.frame.dispose();
		} else if(e.getSource() == submitButton) {
			if(title.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Title cannot be blank!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		        Database database = new Database();
		        
		        String[] fields = { "title", "start_date", "priority", "expected_time", "type"};
		        String[] values = { 
	        		title.getText(), 
	        		timeStamp, 
	        		priority.getSelectedItem().toString(), 
	        		duration.getSelectedItem().toString(),
	        		type.getSelectedItem().toString()
		        };
		        
		        database.insert("tasks", fields, values);
		        
		        JOptionPane.showMessageDialog(null, "Task was added successfully!");
		        this.frame.dispose();
			}
		}
	}
}
