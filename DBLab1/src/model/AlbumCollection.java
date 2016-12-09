
package model;

//import com.sun.deploy.security.ValidationState;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

public class AlbumCollection implements DBQueries {

    private Connection conn = null;
    private LoggedInUser loggedInUser;
    private MongoDatabase db = null;
    private MongoCollection albumCollection = null;
    private MongoCollection artistCollection = null;

    public AlbumCollection () {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase("Labb2");//start connection for session
        albumCollection = db.getCollection("Album");
        artistCollection = db.getCollection("Artist");
    }

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

    private ArrayList<Album> docToAlbums(MongoCursor<Document> cursor){
        ArrayList<Album> albums = new ArrayList<>();
        while(cursor.hasNext()){
            ArrayList<Artist> artists = new ArrayList<>();
            ArrayList<String> genre = new ArrayList<>();
            Document cur = cursor.next();

            try {((List<String>) cur.get("artist")).forEach(a -> artists.add(new Artist(a, "Brit")));}catch (Exception e){}
            try {genre.addAll((List<String>)cur.get("genre"));}catch (Exception e){}

            Album album = new Album(cur.get("_id").toString(),cur.get("title").toString(),artists, genre,
                    cur.get("releaseDate").toString(), cur.get("length").toString(), cur.get("nrOfSongs").toString(), "3");
            albums.add(album);
        }

        return albums;
    }


    @Override
    public ArrayList<Album> getAllRecords() {


        MongoCursor<Document> cursor = albumCollection.find().iterator();
        return docToAlbums(cursor);
    }

    @Override
    public void insertRecord(Object o) throws ParseException {
        Album album = (Album)o;
        ArrayList<String> artists = new ArrayList<>();

        //insert Artist into artist collection in database and get ID so album has refrence ID to artist
        for (Artist a: album.getArtists()
                ) {
            Document artistDoc =  new Document("name", a.getName()).append("nationality", a.getNationality());
            artistCollection.insertOne(artistDoc);
            artists.add(artistDoc.getObjectId("_id").toString());
        }

        //Let genre be an array of album not its own collection
        ArrayList<String> genre = album.getGenre();

        Document albumDocument = new Document("title", album.getTitle()).append("nrOfSongs",album.getNumberOfSongs()).append(
                "releaseDate", album.getReleaseDate()).append("length",album.getLength()).append("artist", artists).append("genre",genre );


        albumCollection.insertOne(albumDocument);
        ObjectId id = albumDocument.getObjectId("_id");
        System.out.println(id);
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
        MongoCursor<Document> cursor = albumCollection.find(new Document("title", Pattern.compile(title))).iterator();
        return docToAlbums(cursor);
    }


    @Override
    public ArrayList<Album>  searchGenre(String genre){
        MongoCursor<Document> cursor = albumCollection.find(new Document("genre", Pattern.compile(genre))).iterator();
        return docToAlbums(cursor);
    }
    @Override//a
    public ArrayList<Album>  searchRating(String rating){
        MongoCursor<Document> cursor = albumCollection.find(new Document("rating", Pattern.compile(rating))).iterator();
        return docToAlbums(cursor);
    }
    @Override
    public ArrayList<Album>  searchArtist(String artist){
        MongoCursor<Document> cursor = albumCollection.find(new Document("artist", Pattern.compile(artist))).iterator();
        return docToAlbums(cursor);
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
