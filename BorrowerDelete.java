/* Displaying, inserting into, updating, deleting from a database table.
*
* This subclass provides a method to delete a record with values specified on
* the command line.  It inherits all the JDBC connection functionality from
* BorrowerDisplay class.
*
* The updated table is displayed. (Display method inherited from superclass)
 */

import java.sql.*;
import java.util.Scanner;

public class BorrowerDelete extends BorrowerDisplay {

    private PreparedStatement sqlDelete = null;

    public boolean connect() {
        boolean ok = super.connect(); //superclass's connect () loads the JDBC
        try { //driver & connects to the DB engine.
            sqlDelete
                    = connection.prepareStatement("DELETE FROM data WHERE ID = ? AND username = ? AND password");
        } catch (SQLException sqlex) {
            System.err.println("SQL Exception");
            sqlex.printStackTrace();
            ok = false;
        }
        return ok;
    }

    public boolean deleteEntry() throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter ID for deletion: ");
        int id = scan.nextInt();
        System.out.print("Enter name for deletion (NO spaces): ");
        String username = scan.next();
        System.out.print("Enter name for deletion (NO spaces): ");
        String password = scan.next();
        sqlDelete.setInt(1, id);
        sqlDelete.setString(2, username);
        sqlDelete.setString(3, password);
        int rslt = sqlDelete.executeUpdate();
        System.err.println("Result code from Delete: " + rslt);
        if (rslt == 0) { // No rows affected – nothing happened
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        BorrowerDelete app = new BorrowerDelete();
        boolean connOK = app.connect();
        if (connOK) {
            try {
                app.deleteEntry();
                app.displayAll(); //from superclass
                app.connection.close();
                System.out.println("\nConnection closed.  Goodbye.");
            } catch (SQLException ex) {
                System.err.println("There was a problem with the DB.");
            }
        }
    }
}
