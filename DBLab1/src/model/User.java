
package model;


public class User {
    
    private String userName;
    
    public User (String userName) {
        
        this.userName = userName;
        
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
