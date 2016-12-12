
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
                ac.deleteAlbum(selectedAlbum);
                ArrayList<Album>  albums = ac.getAlbums();
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
                    ac.reviewRecord(rating, comment, albumID, "album");
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
                ArrayList<Album> albums = ac.getAlbums();
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
        new Thread() {
            public void run() {
                ArrayList<Album> albums = ac.getAlbums();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(albums);
                            }
                        });
            }
        }.start();
        
    }

    public void handleQueryEvent(SearchOptions searchOption, String userInput) throws Exception {

        switch (searchOption.toString()){
            case "title":  new Thread() {
                                public void run() {
                                    ArrayList<Album> albums = ac.searchAlbumTitle(userInput);
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
                                    ArrayList<Album> albums =ac.searchAlbumGenre(userInput);;
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(albums);
                                                }
                                            });
                                }
                            }.start();
                            break;
            case "rating":  int userRating = Integer.parseInt(userInput);
                        new Thread() {
                                public void run() {
                                    ArrayList<Album> albums =ac.searchAlbumRating(userRating);
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
                    ac.insertAlbum(new Album(genre,title,artist,releaseDate,length,nrOfSongs,0));
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

                ArrayList<Album> albums =  ac.getAlbums();
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
