
package view;

import model.Album;
import model.AlbumCollection;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Artist;
import model.SearchOptions;


public class Controller {
    
    private AlbumCollection ac;
    private View view;
    
    public Controller (AlbumCollection ac, View view) {
        this.ac = ac;
        this.view = view;
    }

    public void handleDeleteAlbumEvent(Album selectedAlbum) {
        ac.deleteRecord(selectedAlbum);
        view.updateTextArea(ac.getAllRecords());
    }

    public void handleGetAllAlbumsEvent() {
        view.updateTextArea(ac.getAllRecords());
    }

    public void handleQueryEvent(String searchItem, String userInput) {
        view.updateTextArea(ac.searchRecord(SearchOptions.TITLE, userInput)); //Searchoption.TITLE is hardcoded temporary since i coded using Enum so gotta change one
    }
    public void handleAddAlbumEvent(String title, String artists, String releaseDate, 
            String nrOfSongs, String length, String genres) throws  NumberFormatException{
        //ac.addAlbum(new Album(Name)); //INte klar - fortsätt!!

        //This is temporary for a quick test
        ArrayList<String> genre = new ArrayList<>();
        ArrayList<Artist> artist = new ArrayList<>();
        genre.add(genres);
        Artist a1 = new Artist(artists, "British");
        artist.add(a1);
        /////////////////////////////////////////////

        ac.insertRecord(new Album(genre,title,artist,releaseDate,length,Integer.parseInt(nrOfSongs))); //This adds it directly to the database
        view.updateTextArea(ac.getAllRecords());

    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
