/**
* The database class to communicate with a database.
* @author	Gustas Kurtkus
* @version	1.0
* @since	2018-05-05
*/

import java.sql.*;
import java.util.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
	
	/**
	 * This method is used to retrieve all the tasks from database 
	 * and return them as an ArrayList.
	 * @return This returns all the tasks from SQLite database.
	 */
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static Connection con;
	public List<Task> getTasks()
	{
		List<Task> allTasks = new ArrayList<Task>();
		
		try (Connection connection = this.connect()) {
			
			Statement statement = connection.createStatement();
			String SQL = "SELECT * FROM tasks";
			ResultSet result;
			
			result =  statement.executeQuery(SQL);
			while(result.next()) {
				int id = result.getInt("id");
				String title = result.getString("title");
				String type = result.getString("type");
				String priority = result.getString("priority");
				boolean status = result.getBoolean("status");
				int assignedTo = result.getInt("assigned_to");
				String startDate = result.getString("start_date");
				String endDate = result.getString("end_date");	
				int expectedTimeTaken = result.getInt("expected_time");
				Task task = new Task(id, title, type, priority, status, assignedTo, startDate, endDate, expectedTimeTaken);
				allTasks.add(task);
			}
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return allTasks;
	}
	
	/**
	 * This method is used to retrieve all the users from database 
	 * and return them as an ArrayList.
	 * @return This returns all the users from SQLite database.
	 */
	public List<User> getUsers()
	{
		List<User> allUsers = new ArrayList<User>();
		
		try (Connection connection = this.connect()) {
			
			Statement statement = connection.createStatement();
			String SQL = "SELECT * FROM users";
			ResultSet result;
			
			result =  statement.executeQuery(SQL);
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String password = result.getString("password");
				int permissions = result.getInt("permissions");
				
				User user;
				switch(permissions) {
					case 1: 
						user = new Technician(id, name, password, permissions);
						break;
					case 2:
						user = new Administrator(id, name, password, permissions);
						break;
					case 3:
						user = new Manager(id, name, password, permissions);
						break;
					default:
						user = new Technician(id, name, password, permissions);
						break;
				}
				
				allUsers.add(user);
			}
			
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return allUsers;
	}
	
	/**
	 * This method is used to get an object of a user by the id provided.
	 * @param id This is an id of a user to be retrieved from a database.
	 * @return User This returns an object of a user selected from database by id mentioned above.
	 */
	public User getUser(int id)
	{	
		User user = null;
		String SQL = "SELECT * FROM users WHERE id = ?";
		try(Connection connection = this.connect()) {
		
			PreparedStatement statement = connection.prepareStatement(SQL);
			
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				String name = result.getString("name");
				String password = result.getString("password");
				int permissions = result.getInt("permissions");
				
				switch(permissions) {
					case 1: 
						user = new Technician(id, name, password, permissions);
						break;
					case 2:
						user = new Administrator(id, name, password, permissions);
						break;
					case 3:
						user = new Manager(id, name, password, permissions);
						break;
					default:
						user = new Technician(id, name, password, permissions);
						break;
				}
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return user;
	}
	
	/**
	 * This method is used to update data inside SQLite database.
	 * @param table This is a table name from a database.
	 * @param id This is an ID of a task or a user to be updated.
	 * @param field A field to be updated.
	 * @param value A value to be updated to.
	 */
	public void update(String table, int id, String field, String value)
	{
		String SQL = "UPDATE " + table + " SET " + field + " = ? WHERE id = ?";
		try (Connection connection = this.connect()) {
			
			PreparedStatement statement = connection.prepareStatement(SQL);
				
			if (field.equals("permissions")) {
				statement.setInt(1, Integer.parseInt(value));
				statement.setInt(2, id);
			}
			else {
				statement.setString(1, value);
				statement.setInt(2,id);
			}
				
			statement.executeUpdate();
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is used to delete data from SQLite database.
	 * @param table This is a table name from a database.
	 * @param field This is a field inside a table mentioned above.
	 * @param operator This is an operator (>, <, =) that is used to compare a field with a value.
	 * @param value This is a value that is passed for a field.
	 */
	public void delete(String table, String field, String operator, String value)
	{
		String SQL = "DELETE FROM " + table + " WHERE " + field + " " + operator + " ?";
		try (Connection connection = this.connect()) {
			
			PreparedStatement statement = connection.prepareStatement(SQL);
			
			statement.setString(1, value);
			
			statement.executeUpdate();
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This method is used to insert data into a SQLite database.
	 * It is executed only if the same amount of fields and values are passed.
	 * @param table This is a table name from a database.
	 * @param fields An array of string values that are used as fields from database.
	 * @param values An array of string values that are used as values for each of the fields.
	 */
	public void insert(String table, String[] fields, String[] values)
	{
		if(fields.length == values.length)
		{
			String joinedFields = String.join(", ", fields);
			String joinedValues = String.join("', '", values);

			
			try (Connection connection = this.connect()) {
				
				Statement statement = connection.createStatement();
				String SQL = "INSERT INTO " + table + "(" + joinedFields + ") " + "VALUES('" + joinedValues + "')";

				statement.executeUpdate(SQL);
				statement.close();
				connection.close();
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * This method is used to connect to SQLite database.
	 * @return Connection This returns an instance of a connection
	 */
    public static Connection connect() {
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(Keys.databaseURL, Keys.databaseUser, Keys.databasePassword);
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        catch (SQLException ex) {
            System.out.println("Failed to create the database connection.");
        }
        return con;
    }

}
