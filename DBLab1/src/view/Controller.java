
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
        new Thread() {
            public void run() {
                ac.deleteRecord(selectedAlbum);
                ArrayList<Album>  albums = ac.getAllRecords();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();

    }
    public void handleReviewAlbumEvent(int albumID, int rating, String comment) {
        new Thread() {
            public void run() {
                ac.setAlbumRating(rating,comment,albumID);
                ArrayList<Album> albums = ac.getAllRecords();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();
    }


    public void handleGetAllAlbumsEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "handleGetAllAlbumsEvent körs");
                alert.setTitle("");
                alert.setHeaderText(null);
        new Thread() {
            public void run() {
                ArrayList<Album> albums = ac.getAllRecords();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();
        
    }

    public void handleQueryEvent(SearchOptions searchOption, String userInput) {
        System.out.println(searchOption.toString());

        switch (searchOption.toString()){
            case "title":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums = ac.searchTitle(userInput);
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(albums);
                                                }
                                            });
                                }
                            }.start();
                            break;
            case "artist":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums =ac.searchArtist(userInput);;
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(albums);
                                                }
                                            });
                                }
                            }.start();
                            break;

            case "genre":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums =ac.searchGenre(userInput);;
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(albums);
                                                }
                                            });
                                }
                            }.start();
                            break;
            case "rating":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums =ac.searchRating(userInput);
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(albums);
                                                }
                                            });
                                }
                            }.start();
                            break;
        }
         //Searchoption.TITLE is hardcoded temporary since i coded using Enum so gotta change one


//        Alert alert = new Alert(Alert.AlertType.WARNING, searchOption.toString());
//        alert.showAndWait();
    }
    
    public void handleAddAlbumEvent(String title, String artists, String releaseDate,
                                    String nrOfSongs, String length, String genres) throws  NumberFormatException{

        //This is temporary for a quick test
        ArrayList<String> genre = new ArrayList<>();
        ArrayList<Artist> artist = new ArrayList<>();
        genre.add(genres);
        Artist a1 = new Artist(artists, "British");
        artist.add(a1);
        /////////////////////////////////////////////

        new Thread() {
            public void run() {
                ac.insertRecord(new Album(genre,title,artist,releaseDate,length,Integer.parseInt(nrOfSongs)));
                ArrayList<Album> albums =  ac.getAllRecords();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();
    }



    public boolean handleLogInEvent(String userName, String password) {
        return ac.userLogIn(userName,password); //Maybe run this in thread aswell?
    }
    
    public void handleLogOutEvent() {
        ac.setLoggedInUser(null);//Maybe run this in thread aswell?
    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
