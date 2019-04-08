/* Displaying, inserting into, updating, deleting from a database table.
*
* This subclass provides a method to insert a record with values specified on
* the command line.  It inherits all the JDBC connection functionality from
* BorrowerDisplay class.
*
* The updated table is displayed. (Display method inherited from superclass)
 */

import java.sql.*;
import java.util.Scanner;

public class BorrowerInsert extends BorrowerDisplay {

    private PreparedStatement sqlInsert = null;

    public boolean connect() {
        boolean ok = super.connect(); //superclass's connect () loads the JDBC
        try { //driver & connects to the DB engine.
            sqlInsert = connection.prepareStatement("INSERT INTO data (ID, username, password) VALUES (?, ?, ?)");
        } catch (SQLException sqlex) {
            System.err.println("SQL Exception");
            sqlex.printStackTrace();
            ok = false;
        }
        return ok;
    }

    /* The helper method for the insertion …
     */
    public boolean addEntry() throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter new ID: ");
        int id = scan.nextInt();
        System.out.print("Enter username (NO spaces): ");
        String username = scan.next();
        System.out.print("Enter password (NO spaces): ");
        String password = scan.next();
        sqlInsert.setInt(1, id);
        sqlInsert.setString(2, username);
        sqlInsert.setString(3, password);
        int rslt = sqlInsert.executeUpdate();
        System.err.println("Result code from Insert: " + rslt);
        if (rslt == 0) {
            // connection.rollback ();
            return false;
        } else {
            // connection.commit ();
            return true;
        }
    }

    public static void main(String[] args) {
        BorrowerInsert app = new BorrowerInsert();
        boolean connOK = app.connect();
        if (connOK) {
            try {
                app.addEntry();
                app.displayAll(); //from superclass
                app.connection.close();
                System.out.println("\nConnection closed. Goodbye.");
            } catch (SQLException ex) {
                System.err.println("There was a problem with the DB.");
            }
        }
    }
}
