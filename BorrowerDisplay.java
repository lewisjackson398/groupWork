/* Displaying, inserting into, updating, deleting from a database table.
* This basic example connects via JDBC to a DB Engine, does a SELECT *
* and presents the result set on the console.
 */
import java.sql.*;
import java.util.*;
import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BorrowerDisplay {

    // used for local server
/*private String driver = "jdbc:ucanaccess://";
private String dBase = "C:\\Users\\Lewis's Laptop\\Desktop\\database.accdb";
		// replace this by your own database location
  private String url = driver + dBase;*/
    // used for remote server
    public Connection connection;

    public boolean connect() {	// returns true if connection made
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
            // unless you’ve set up a username or password…
            System.err.println("Connection successful\n");
        } catch (SQLException sqlex) {
            System.err.println("Connection unsuccessful\n" + sqlex.toString());
            return false;
        } catch (Exception ex) {//remaining exceptions
            System.err.println(ex.toString());
            return false;
        }
        return true;
    }

    public void displayAll() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "select * from mock_data order by id";
        ResultSet rs = statement.executeQuery(query);
        // position to first record
        boolean moreRecords = rs.next();
        // If there are no records, display a message
        if (!moreRecords) {
            System.out.println("ResultSet contained no records");
            return;
        }
        // display column headings
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            System.out.print(rsmd.getColumnName(i) + "\t");
        }
        System.out.println();
        // display rows of data ....
        do {
            displayRow(rs, rsmd); //helper method below
        } while (rs.next());
    }

    private void displayRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            switch (rsmd.getColumnType(i)) {
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    System.out.print(rs.getString(i) + "\t");
                    break;
                case Types.INTEGER:
                case Types.NUMERIC:
                    System.out.print("" + rs.getInt(i) + "\t");
                    break;
                default:	// do nothing – print nothing
            }
        }
        System.out.println();
    }

    public static void main(String args[]) {
        BorrowerDisplay app = new BorrowerDisplay();
        boolean connOK = app.connect();
        if (connOK) {
            try {
                app.displayAll();
                app.connection.close();
                System.out.println("\nConnection closed. Goodbye.");
            } catch (SQLException ex) {
                System.err.println("There was a problem with the DB.");
            }
        }
    }
}
