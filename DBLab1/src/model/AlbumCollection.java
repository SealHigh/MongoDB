
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
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;


public class AlbumCollection implements DBQueries {

    private Connection conn = null;
    private LoggedInUser loggedInUser;
    private MongoDatabase db = null;
    private MongoCollection albumCollection = null;
    private MongoCollection userCollection = null;
    private MongoCollection reviewCollection = null;

    public AlbumCollection () {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase("Labb2");//start connection for session
        albumCollection = db.getCollection("Album");
        userCollection = db.getCollection("Users");
        reviewCollection = db.getCollection("Reviews");
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



    /**
     * Used for all album retrival from the database by taking in a cursor of the collection to make into an album
     * @param cursor of the collection to make into an album
     * @return  list of albums
     */
    private ArrayList<Album> docToAlbums(MongoCursor<Document> cursor) {
        ArrayList<Album> albums = new ArrayList<>();
        while (cursor.hasNext()) {
            ArrayList<Artist> artists = new ArrayList<>();
            ArrayList<String> genre = new ArrayList<>();
            Document cur = cursor.next();
            try {
                Document review = (Document) cur.get("review");
                ((List<Document>) cur.get("artist")).forEach(a -> artists.add(new Artist(a.getString("name"), "Brit")));
                genre.addAll((List<String>) cur.get("genre"));
                Album album = new Album(cur.get("_id").toString(), cur.getString("title"), artists, genre,
                        cur.get("releaseDate").toString(), cur.get("length").toString(), cur.get("nrOfSongs").toString(),
                        review.getDouble("avgRating"), review.getInteger("count").toString());


                albums.add(album);
            } catch (Exception e) {
                //Throw exception could not load document
            }
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

        ArrayList<Document> artists = new ArrayList<>();
        album.getArtists().forEach(artist -> artists.add(new Document("name",artist.getName()).append("nationality", artist.getNationality())));

        Document albumDocument = new Document("title", album.getTitle())
                .append("nrOfSongs",album.getNumberOfSongs()).append(
                "releaseDate", album.getReleaseDate())
                .append("length",album.getLength())
                .append("artist", artists)
                .append("genre",album.getGenre())
                .append("review", new Document("avgRating", 0.0).append("count", 0));

        albumCollection.insertOne(albumDocument);

    }

    @Override
    public boolean userLogIn(String userName, String password){

        MongoCursor<Document> cursor = userCollection.find(new Document("userName", userName).append("password", password)).iterator();
        if(cursor.hasNext()) {
            Document cur = cursor.next();
            this.loggedInUser = new LoggedInUser(cur.getString("userName"), cur.get("_id").toString());
        }
        return loggedInUser != null;
    }


    /**
     * Creates a new review of the album aswell as changes the avgRating of the album and nr of reviews
     * @param rating from the user
     * @param comment from the user
     * @param albumID album to which the review belongs to
     * @throws Exception
     */
    public void setAlbumRating(int rating, String comment, String albumID) throws Exception{

        reviewCollection.insertOne(new Document("rating", rating).append("comment", comment)
                .append("userID", new String(loggedInUser.getUserId()))
                .append("albumID", albumID));
        MongoCursor<Document> cursor = albumCollection.find(eq("_id", new ObjectId(albumID))).iterator();
        Document cur = cursor.next();

        Document reviewCur = (Document)cur.get("review");
        double newRating = avgRatingFromDoc(reviewCur, rating);
        albumCollection.updateOne(eq("_id", new ObjectId(albumID)), set("review.avgRating", newRating));
        albumCollection.updateOne(eq("_id", new ObjectId(albumID)), inc("review.count", 1));


    }

    @Override
    public double avgRatingFromDoc(Document cur, int rating){
        double totalRating = cur.getDouble("avgRating");
        int nrOfRatings = cur.getInteger("count");

        double newRatintg = (totalRating*nrOfRatings+rating)/(nrOfRatings+1);
        return newRatintg;
    }


    @Override
    public void deleteRecord(Object o) {
        Album album = (Album) o;
        albumCollection.findOneAndDelete(new Document("_id" ,new ObjectId(album.getAlbumID())));
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
    public User getUser(String albumID){
        MongoCursor<Document> userCur = userCollection.find(new Document("_id", new ObjectId(albumID))).iterator();
        Document userDoc = userCur.next();
        User user = new User(userDoc.getString("userName"));
        return user;

    }

    @Override
    public ArrayList<Review> getReviews(String albumID){
        MongoCursor<Document> cursor = reviewCollection.find(new Document("albumID", albumID)).iterator();
        ArrayList<Review> reviews = new ArrayList<>();
        while(cursor.hasNext()) {
            Document cur = cursor.next();
            User user = getUser(cur.getString("userID"));
            reviews.add(new Review(cur.getInteger("rating"), cur.getString("comment"), user.getUserName()));
        }

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
