
package view;

import model.Album;
import model.AlbumCollection;

import java.util.Optional;
import javafx.scene.control.Alert;


public class Controller {
    
    private AlbumCollection ac;
    private View view;
    
    public Controller (AlbumCollection ac, View view) {
        this.ac = ac;
        this.view = view;
    }
    
    public void handleQueryEvent(String searchItem, String userInput) {
        String message = searchItem + " " + userInput;
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
    public void handleAddAlbumEvent(String title, String artists, String releaseDate, 
            String nrOfSongs, String length, String genres) {
        //ac.addAlbum(new Album(Name)); //INte klar - fortsätt!!
        view.updateTextArea(ac.getCurrentAlbums());

    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
