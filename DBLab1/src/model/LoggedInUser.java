
package model;


public class LoggedInUser extends User {
    
    private final String userId;
    
    public LoggedInUser (String userName, String userId) {
        super(userName);
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
}
