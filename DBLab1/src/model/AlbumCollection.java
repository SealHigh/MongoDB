
package model;

//import com.sun.deploy.security.ValidationState;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

public class AlbumCollection implements DBQueries {

    private Connection conn = null;
    private LoggedInUser loggedInUser;


    private void firstRunDatabase(MongoDatabase db){
        db.createCollection("Album");
        Document albumDocument = new Document("title", "Test");
        MongoCollection<Document> collection = db.getCollection("Album");
        collection.insertOne(albumDocument);
    }

    public AlbumCollection () {
        conn =  ConnectionConfiguration.getConnection(); //star connection for session
    }

    @Override
    public ArrayList<Album> getAllRecords() {
        ArrayList<Album> albums = new ArrayList<>();

        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("Labb2");

        //  run this to create a database
        //firstRunDatabase(db);

        MongoCollection<Document> collection = db.getCollection("Album");

        collection.find().forEach((Block<Document>) document -> {
            //TODO: replace everything with document.get()
            Album album = new Album(document.get("_id").toString(),document.get("title").toString(), new ArrayList<Artist>(), new ArrayList<String>(),
                    "releaseDate", "length", 33, 3);
            albums.add(album);
        });

        return albums;
    }

    @Override
    public boolean userLogIn(String userName, String password) {
        
        int number = 0;
        return number > 0;
    }



    public void setAlbumRating(int rating, String comment, String albumID) throws Exception{
    }


    @Override
    public void insertRecord(Object o) throws ParseException {

    }

    @Override
    public void deleteRecord(Object o) {

    }

    @Override
    public int getAlbumRating(String albumId){
        return 0;
    }
    


    @Override
    public void rateAlbum(Object o){

    }

    @Override
    public  ArrayList<String> getGenres(String albumID){
        ArrayList<String> genres = new ArrayList<>();
        return genres;
    }

    @Override
    public ArrayList<Review> getReviews(String albumID){

        ArrayList<Review> reviews = new ArrayList<>();
        return reviews;
    }

    @Override
    public ArrayList<Artist> getArtists(String albumID){
        
        ArrayList<Artist> artists = new ArrayList<>();
        return artists;
    }

    @Override
    public Director getDirector(String albumID){
        
        Director director= null;
        return director;
    }


    @Override
    public ArrayList<Album>  searchTitle(String title){
        ArrayList<Album> albums = new ArrayList<>();
        return albums;
    }


    @Override
    public ArrayList<Album>  searchGenre(String genre){
        ArrayList<Album> albums = new ArrayList<>();
        return albums;
    }
    @Override//a
    public ArrayList<Album>  searchRating(String rating){
        ArrayList<Album> albums = new ArrayList<>();
        return albums;
    }
    @Override
    public ArrayList<Album>  searchArtist(String artist){
        ArrayList<Album> albums = new ArrayList<>();
        return albums;
    }
    

    /**
     * @return the loggedInUser
     */
    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
