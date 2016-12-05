
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
    public void handleReviewAlbumEvent(int albumID, int rating, String comment) {
        ac.setAlbumRating(rating,comment,albumID);
        view.updateTextArea(ac.getAllRecords());
    }


    public void handleGetAllAlbumsEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "handleGetAllAlbumsEvent körs");
                alert.setTitle("");
                alert.setHeaderText(null);      
        view.updateTextArea(ac.getAllRecords());
        
    }

    public void handleQueryEvent(SearchOptions searchOption, String userInput) {
        System.out.println(searchOption.toString());

        switch (searchOption.toString()){
            case "title":   view.updateTextArea(ac.searchTitle(userInput));
                            break;
            case "artist":  view.updateTextArea(ac.searchArtist(userInput));
                            break;
            case "genre":   view.updateTextArea(ac.searchGenre(userInput));
                            break;
            case "rating":  view.updateTextArea(ac.searchRating(userInput));
                            break;
        }
         //Searchoption.TITLE is hardcoded temporary since i coded using Enum so gotta change one


//        Alert alert = new Alert(Alert.AlertType.WARNING, searchOption.toString());
//        alert.showAndWait();
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
    
    public void handleAddReviewEvent(Album slectedAlbum, String rating, String comment) {
        //Parse to int
        //add -> ac.getLoggedInUser().getUserId() and send to modell 
    }
    
    public boolean handleLogInEvent(String userName, String password) {
        return ac.userLogIn(userName,password);
    }
    
    public void handleLogOutEvent() {
        ac.setLoggedInUser(null);
    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
