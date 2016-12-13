
package view;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import model.Album;
import model.AlbumCollection;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
                try {
                    ac.deleteRecord(selectedAlbum);
                } catch (Exception e) {
                    throw e;
                }
                
                ArrayList<Album>  albums;
                try {
                    albums = ac.getAllRecords();
                } catch (Exception e) {
                    throw e;
                }
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();

    }
    public void handleReviewAlbumEvent(String albumID, int rating, String comment) {
        new Thread() {
            public void run() {
                try {
                    ac.setAlbumRating(rating, comment, albumID);
                }
                catch (Exception e){
                    e.printStackTrace();
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "You can only make one review for each album/film ");
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.showAndWait();
                                }
                            });
                }
                
                ArrayList<Album> albums;
                try {
                    albums = ac.getAllRecords();
                } catch (Exception e) {
                    throw e;
                }
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
    try {    
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
    } catch (Exception e) {
        System.out.println("handleGetAllAlbumsEvent har kastat!");
        throw e;
    }    
    }

    public void handleQueryEvent(SearchOptions searchOption, String userInput) {
        System.out.println(searchOption.toString());

        switch (searchOption.toString()){
            case "title":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums;
                                    try {
                                        albums = ac.searchTitle(userInput);
                                    } catch (Exception e) {
                                        throw e;                   
                                    }
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
                                    ArrayList<Album> albums;
                                    try {
                                        albums =ac.searchArtist(userInput);;
                                    } catch (Exception e) {
                                        throw e;
                                    }
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
                                    ArrayList<Album> albums;
                                    try {
                                        albums =ac.searchGenre(userInput);;
                                    } catch (Exception e) {
                                        throw e;
                                    }
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
                                    ArrayList<Album> albums;
                                    try {
                                        albums =ac.searchRating(userInput);
                                    } catch (Exception e) {
                                        throw e;
                                    }
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
    }
    
    public void handleAddAlbumEvent(String title, String artists, String releaseDate,
                                    String nrOfSongs, String length, String genres){

        //This is temporary for a quick test
        ArrayList<String> genre = new ArrayList<>();
        ArrayList<Artist> artist = new ArrayList<>();
        genre.add(genres);
        Artist a1 = new Artist(artists, "British");
        artist.add(a1);
        /////////////////////////////////////////////

        new Thread() {
            public void run(){
                try {
                    ac.insertRecord(new Album(genre,title,artist,releaseDate,length,nrOfSongs));
                }
                catch (Exception e){
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Wrong format. Example: ('title', 'name', 'yyyy-mm-dd', 10, 30, 'Rock'  ");
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.showAndWait();
                                }
                            });
                }
                
                ArrayList<Album> albums;
                try {
                    albums =  ac.getAllRecords();
                } catch (Exception e) {
                    throw e;
                }
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();
    }



    public boolean handleLogInEvent(String userName, String password) {
        try {
            return ac.userLogIn(userName,password); //Maybe run this in thread aswell?
        } catch (Exception e) {
            throw e;          
        }
    }
    
    public void handleLogOutEvent() {
        ac.setLoggedInUser(null);//Maybe run this in thread aswell?
    }

    
    public String validateQueryDialogInput() {
        
        return "";
    }
}
