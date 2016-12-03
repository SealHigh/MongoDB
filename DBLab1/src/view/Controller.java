
package view;

import model.Album;
import model.AlbumCollection;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.Alert;
import model.Artist;


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

        //This is temporary for a quick test
        ArrayList<String> genre = new ArrayList<>();
        ArrayList<Artist> artist = new ArrayList<>();
        genre.add(genres);
        Artist a1 = new Artist(artists, "British");
        artist.add(a1);
        /////////////////////////////////////////////

        ac.addRecord(new Album(genre,title,artist,releaseDate,length,Integer.parseInt(nrOfSongs))); //This adds it directly to the database
        view.updateTextArea(ac.getCurrentAlbums());

    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
