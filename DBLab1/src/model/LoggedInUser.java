
package model;


public class LoggedInUser extends User {
    
    private final int userId;
    
    public LoggedInUser (String userName, int userId) {
        super(userName);
        this.userId = userId;
    }
    
    public int getUserId() {
        return userId;
    }
}
