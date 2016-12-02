
package view;

import model.AlbumCollection;


public class Controller {
    
    private AlbumCollection ac;
    private View view;
    
    public Controller (AlbumCollection ac, View view) {
        this.ac = ac;
        this.view = view;
    }
    
    public void handleQueryEvent(String query) {
        
    }
    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
