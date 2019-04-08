/* Displaying, inserting into, updating, deleting from a database table.
* This subclass provides a method to update a record with values specified on
* the command line.  It inherits all the JDBC connection functionality from
* BorrowerDisplay class.
* The updated table is displayed. (Display method inherited from superclass)
 */

import java.sql.*;
import java.util.Scanner;

public class BorrowerUpdate extends BorrowerDisplay {

    private PreparedStatement sqlUpdate = null;

    public boolean connect() {
        boolean ok = super.connect(); //superclass's connect () loads the JDBC
        try { //driver & connects to the DB engine.
            sqlUpdate = connection.prepareStatement("UPDATE mock_data SET id = ?, username = ?, password = ?"
                    + " WHERE id = ? AND username = ? AND password = ?");
        } catch (SQLException sqlex) {
            System.err.println("SQL Exception");
            sqlex.printStackTrace();
            ok = false;
        }
        return ok;
    }

    /* The Helper method */
    public boolean updateName() throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter ID to change: ");
        int id = scan.nextInt();
        System.out.print("Enter old username – NO spaces: ");
        String username = scan.next();
        System.out.print("Enter new username – NO spaces: ");
        String newUsername = scan.next();

        System.out.print("Enter old password – NO spaces: ");
        String password = scan.next();
        System.out.print("Enter new password – NO spaces: ");
        String newPassword = scan.next();

        sqlUpdate.setInt(1, id);
        sqlUpdate.setString(2, newUsername);
        sqlUpdate.setInt(3, id);
        sqlUpdate.setString(4, username);

        sqlUpdate.setInt(5, id);
        sqlUpdate.setString(6, newPassword);
        sqlUpdate.setInt(7, id);
        sqlUpdate.setString(8, password);

        int rslt = sqlUpdate.executeUpdate();
        System.err.println("Result code from Update: " + rslt);
        if (rslt == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        BorrowerUpdate app = new BorrowerUpdate();
        boolean connOK = app.connect();
        if (connOK) {
            try {
                app.updateName();
                app.displayAll(); //from superclass
                app.connection.close();
                System.out.println("\nConnection closed.  Goodbye.");
            } catch (SQLException ex) {
                System.err.println("There was a problem with the DB.");
            }
        }
    }
}
