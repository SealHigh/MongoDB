
package view;

import model.*;

import java.util.ArrayList;

import javafx.scene.control.Alert;


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
                    ac.deleteAlbum(selectedAlbum);
                }catch (DatabaseException de){
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                public void run() {
                                    view.displayError("Could not delete album!");
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
    
    public void handleDeleteMovieEvent(Movie selectedMovie) {
        new Thread() {
            public void run() {
                try {
                    ac.deleteMovie(selectedMovie);
                }
                catch (DatabaseException de){
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                public void run() {
                                    view.displayError("Could not delete movie!");
                                }
                            });
                }
                ArrayList<Movie>  movies = ac.getMovies();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(movies);
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
                    javafx.application.Platform.runLater(
                            new Runnable() {
                                public void run() {
                                    view.displayError("You can only make one review for each album/film ");
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
    
    public void handleReviewMovieEvent(String movieID, int rating, String comment) {
        new Thread() {
            public void run() {
                try {
                    ac.reviewRecord(rating, comment, movieID, "movie");
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
                ArrayList<Movie> movies = ac.getMovies();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(movies);
                            }
                        });
            }
        }.start();
    }


    public void handleGetAllAlbumsEvent() {
    try {    
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
    } catch (Exception e) {
        System.out.println("handleGetAllAlbumsEvent har kastat!");
        throw e;
    }    
    }
    
    public void handleGetAllMoviesEvent() {
    try {    
        new Thread() {
            public void run() {
                ArrayList<Movie> movies = ac.getMovies();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            public void run() {
                                view.updateTextArea(movies);
                            }
                        });
            }
        }.start();
    } catch (Exception e) {
        System.out.println("handleGetAllAlbumsEvent har kastat!");
        throw e;
    }    
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
    
    public void handleQueryMovieEvent(SearchMovieOptions searchMovieOption, String userInput) throws Exception {

        switch (searchMovieOption.toString()){
            case "title":  new Thread() {
                                public void run() {
                                    ArrayList<Movie> movies = ac.searchMovieTitle(userInput);
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(movies);
                                                }
                                            });
                                }
                            }.start();
                            break;
            case "director":  new Thread() {
                                public void run() {
                                    ArrayList<Movie> movies;
                                    try {
                                        movies =ac.searchDirectror(userInput);;
                                    } catch (Exception e) {
                                        throw e;
                                    }
                                    javafx.application.Platform.runLater(
                                        new Runnable() {
                                            public void run() {
                                                view.updateTextArea(movies);
                                            }
                                    });
                                }
                            }.start();
                            break;

            case "genre":  new Thread() {
                                public void run() {
                                    ArrayList<Movie> movies =ac.searchMovieGenre(userInput);;
                                    javafx.application.Platform.runLater(
                                        new Runnable() {
                                            public void run() {
                                                view.updateTextArea(movies);
                                            }
                                    });
                                }
                            }.start();
                            break;
            case "rating":  int userRating = Integer.parseInt(userInput);
                        new Thread() {
                                public void run() {
                                    ArrayList<Movie> movies =ac.searchMovieRating(userRating);
                                    javafx.application.Platform.runLater(
                                            new Runnable() {
                                                public void run() {
                                                    view.updateTextArea(movies);
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
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Could not add album!");
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.showAndWait();
                                }
                            });
                }

                ArrayList<Album> albums =  ac.getAlbums();
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

    public void handleAddMovieEvent(String title, String director, String releaseYear,
                                    String length, String genres){

        //This is temporary for a quick test
        ArrayList<String> genre = new ArrayList<>();
        genre.add(genres);
        Director dir = new Director(director, "British");
        /////////////////////////////////////////////

        new Thread() {
            public void run(){      
                try {
                    ac.insertMovie(new Movie(genres,title,dir,releaseYear,length,0));
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

                ArrayList<Movie> movies =  ac.getMovies();
                javafx.application.Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                view.updateTextArea(movies);
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
