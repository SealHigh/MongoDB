
package view;

import model.Album;
import model.AlbumCollection;

import java.util.Optional;


public class Controller {
    
    private AlbumCollection ac;
    private View view;
    
    public Controller (AlbumCollection ac, View view) {
        this.ac = ac;
        this.view = view;
    }
    
    public void handleQueryEvent(String query) {
        
    }
    public void handleAddAlbumEvent(String Name) {
        ac.addAlbum(new Album(Name));
        view.updateTextArea(ac.getCurrentAlbums());

    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
