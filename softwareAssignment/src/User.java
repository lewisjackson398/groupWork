import java.lang.String;

public abstract class User {
    int userID;
    String name;

    String password;
    int accessLevel;

    public User(int userID, String name, String password, int accessLevel) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.accessLevel = accessLevel;
        }


    public int getUserID() {
        return userID;
    }

    public String getName() { return name;}

    public String getPassword() {
        return password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
