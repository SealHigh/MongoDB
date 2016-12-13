package model;


import org.bson.Document;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

public interface DBQueries {


    boolean userLogIn(String userName, String password);

    /**
     * Retrivies all records in the database
     * @param <T>
     * @return a list of all the records
     */
    <T> ArrayList<T> getAlbums();

    <T> ArrayList<T> getMovies();

    void insertAlbum(Object o) throws DatabaseException;

    void insertMovie(Object o) throws DatabaseException;

    void deleteAlbum(Object o) throws DatabaseException;

    void deleteMovie(Object o) throws DatabaseException;

    void reviewRecord(int rating, String comment, String ID, String type) throws Exception;

    ArrayList<Review> getReviews(String albumID, String type);

    User getUser(String albumID);

    double avgRatingFromDoc(Document cur, int rating);

    <T> ArrayList<T> searchAlbumTitle(String title) ;

    <T> ArrayList<T> searchArtist(String artist) ;

    <T> ArrayList<T> searchAlbumGenre(String genre) ;

    <T> ArrayList<T> searchAlbumRating(int rating) ;

    <T> ArrayList<T> searchMovieTitle(String title);

    <T> ArrayList<T> searchDirectror(String director);

    <T> ArrayList<T> searchMovieGenre(String genre);

    <T> ArrayList<T> searchMovieRating(int rating);
    
}
