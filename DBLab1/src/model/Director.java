
package model;


public class Director {
    
    private String name;
    private String nationality;
    
    public Director (String name, String nationality) {
        
        this.name = name;
        this.nationality = nationality;
        
    }
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
}
