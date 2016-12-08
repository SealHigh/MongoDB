
package model;

//import com.sun.deploy.security.ValidationState;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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


    private void addToDatabase(MongoDatabase db){
        Document albumDocument = new Document("title", "Test").append("nrOfSongs",34).append("releaseDate", 19940722).append("length",120);
        ArrayList<Artist> artists = new ArrayList<>();

        ArrayList<String> aritst = new ArrayList<>();
        aritst.add("test");
        aritst.add("test4");

        albumDocument.append("artist", aritst);
        MongoCollection<Document> collection = db.getCollection("Album");
        collection.insertOne(albumDocument);
    }
    private void firstRunDatabase(MongoDatabase db){
        db.createCollection("Album");
        Document albumDocument = new Document("title", "Test").append("nrOfSongs",34).append("releaseDate", 19940722).append("length",120);
        ArrayList<Artist> artists = new ArrayList<>();

        ArrayList<String> aritst = new ArrayList<>();
        aritst.add("test");
        aritst.add("test4");

        albumDocument.append("artist", aritst);
        MongoCollection<Document> collection = db.getCollection("Album");
        collection.insertOne(albumDocument);
    }

    public AlbumCollection () {
        conn =  ConnectionConfiguration.getConnection(); //star connection for session
    }

    @Override
    public ArrayList<Album> getAllRecords() {
        ArrayList<Album> albums = new ArrayList<>();

        //java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("Labb2");

        //  run this to create a database
        //firstRunDatabase(db);

        MongoCollection<Document> collection = db.getCollection("Album");
        MongoCursor<Document> cursor = collection.find().iterator();

        while(cursor.hasNext()){
            ArrayList<Artist> artists = new ArrayList<>();
            Document cur = cursor.next();
            try {((List<String>) cur.get("artist")).forEach(a -> artists.add(new Artist(a, "Brit")));}catch (Exception e){}


            Album album = new Album(cur.get("_id").toString(),cur.get("title").toString(),artists, new ArrayList<String>(),
                    cur.get("releaseDate").toString(), cur.get("length").toString(), cur.get("nrOfSongs").toString(), "3");
            albums.add(album);
        }

        return albums;
    }

    @Override
    public void insertRecord(Object o) throws ParseException {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("Labb2");
        Album album = (Album)o;
        ArrayList<String> artists = new ArrayList<>();
        album.getArtists().forEach(artist -> artists.add(artist.getName()));
        ArrayList<String> genre = album.getGenre();

        Document albumDocument = new Document("title", album.getTitle()).append("nrOfSongs",album.getNumberOfSongs()).append(
                "releaseDate", album.getReleaseDate()).append("length",album.getLength()).append("artist", artists).append("genre",genre );


        MongoCollection<Document> collection = db.getCollection("Album");
        collection.insertOne(albumDocument);
    }

    @Override
    public boolean userLogIn(String userName, String password) {
        
        int number = 1;
        return number > 0;
    }



    public void setAlbumRating(int rating, String comment, String albumID) throws Exception{
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
