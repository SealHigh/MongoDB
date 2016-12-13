
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

    private LoggedInUser loggedInUser;
    private MongoDatabase db = null;
    private MongoCollection albumCollection = null;
    private MongoCollection userCollection = null;
    private MongoCollection reviewCollection = null;
    private MongoCollection movieCollection = null;

    public AlbumCollection () {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase("Labb2");//start connection for session
        albumCollection = db.getCollection("Album");
        userCollection = db.getCollection("Users");
        reviewCollection = db.getCollection("Reviews");
        movieCollection = db.getCollection("Movie");
    }

    /**
     * Used for all album retrival from the database by taking in a cursor of the collection to make into an album
     * @param cursor of the collection to make into an album
     * @return  list of albums
     */
    private ArrayList<Album> curToAlbums(MongoCursor<Document> cursor) {
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

    private ArrayList<Movie> curToMovies(MongoCursor<Document> cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.hasNext()) {
            Director director;
            Document cur = cursor.next();
            try {

                Document review = (Document) cur.get("review");
                Document dir = (Document) cur.get("director");
                director = new Director(dir.getString("name"), dir.getString("nationality"));

                Movie movie = new Movie(cur.get("_id").toString(),
                        cur.get("title").toString(), director,  cur.get("genre").toString(),
                        cur.get("releaseYear").toString(),
                        cur.get("length").toString(),
                        (Double)review.get("avgRating"),
                        review.get("count").toString());

                movies.add(movie);
            } catch (Exception e) {
                //Throw exception could not load document
            }
        }
        return movies;
    }


    @Override
    public ArrayList<Album> getAlbums() {
        MongoCursor<Document> cursor = albumCollection.find().iterator();
        return curToAlbums(cursor);
    }

    @Override
    public ArrayList<Movie> getMovies() {
        MongoCursor<Document> cursor = movieCollection.find().iterator();
        return curToMovies(cursor);
    }

    @Override
    public void insertAlbum(Object o) throws ParseException {
        Album album = (Album)o;

        ArrayList<Document> artists = new ArrayList<>();
        album.getArtists().forEach(artist -> artists.add(new Document("name",artist.getName()).append("nationality", artist.getNationality())));

        Document albumDocument = new Document("title", album.getTitle())
                .append("nrOfSongs",album.getNumberOfSongs()).append(
                "releaseDate", album.getReleaseDate())
                .append("length",album.getLength())
                .append("artist", artists)
                .append("genre",album.getGenre())
                .append("review", new Document("avgRating", 0.0).append("count", 0))
                .append("addedBy", loggedInUser.getUserId());

        albumCollection.insertOne(albumDocument);

    }

    @Override
    public void insertMovie(Object o) throws ParseException {
        Movie movie = (Movie) o;

        Document movieDocument = new Document("title", movie.getTitle())
                .append("releaseYear", movie.getReleaseYear())
                .append("length",movie.getLength())
                .append("director", new Document("name", movie.getDirector().getName()).append("nationality",movie.getDirector().getNationality()))
                .append("genre",movie.getGenre())
                .append("review", new Document("avgRating", 0.0).append("count", 0))
                .append("addedBy", loggedInUser.getUserId());

        movieCollection.insertOne(movieDocument);

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
     * @param ID record to which the review belongs to
     * @param type of object the review belongs to
     * @throws Exception
     */
    @Override
    public void reviewRecord(int rating, String comment, String ID, String type) throws Exception{
    if(!reviewCollection.find(new Document("userID", loggedInUser.getUserId()).append(type+"ID", ID)).iterator().hasNext()) {
        reviewCollection.insertOne(new Document("rating", rating).append("comment", comment)
                .append("userID", new String(loggedInUser.getUserId()))
                .append(type+"ID", ID));
        
        MongoCursor<Document> cursor;
        
        if(type == "album") {
            cursor = albumCollection.find(eq("_id", new ObjectId(ID))).iterator();
        }
        else if(type == "movie"){
            cursor = movieCollection.find(eq("_id", new ObjectId(ID))).iterator();
        }
        else {
            cursor = null;
        }      
        
        Document cur = cursor.next();

        Document reviewCur = (Document)cur.get("review");
        double newRating = avgRatingFromDoc(reviewCur, rating);
        if(type == "album") {
            albumCollection.updateOne(eq("_id", new ObjectId(ID)), set("review.avgRating", newRating));
            albumCollection.updateOne(eq("_id", new ObjectId(ID)), inc("review.count", 1));
        }
        else if(type == "movie"){
            movieCollection.updateOne(eq("_id", new ObjectId(ID)), set("review.avgRating", newRating));
            movieCollection.updateOne(eq("_id", new ObjectId(ID)), inc("review.count", 1));
        }

    }
        else
            System.out.println("User has left review, THROW EXCEPTION INSTEAD");
    }


    @Override
    public double avgRatingFromDoc(Document cur, int rating){
        double totalRating = cur.getDouble("avgRating");
        int nrOfRatings = cur.getInteger("count");

        double newRatintg = (totalRating*nrOfRatings+rating)/(nrOfRatings+1);
        return newRatintg;
    }

    @Override
    public void deleteAlbum(Object o) {
        Album album = (Album) o;
        try {
            albumCollection.findOneAndDelete(new Document("_id" ,new ObjectId(album.getAlbumID())));
        } catch (MongoException me) {
            throw me;
        }
    }

    @Override
    public void deleteMovie(Object o) {
        Movie movie = (Movie) o;
        movieCollection.findOneAndDelete(new Document("_id" ,new ObjectId(movie.getMovieID())));
    }

    @Override
    public User getUser(String albumID){
        MongoCursor<Document> userCur;
        try {
            userCur = userCollection.find(new Document("_id", new ObjectId(albumID))).iterator();
        } catch (MongoException me) {
            userCur = null;
            throw me;
        }
        Document userDoc = userCur.next();
        User user = new User(userDoc.getString("userName"));
        return user;

    }

    @Override
    public ArrayList<Review> getReviews(String albumID, String type){
        MongoCursor<Document> cursor;
        try {
            cursor = reviewCollection.find(new Document(type + "ID", albumID)).iterator();
        } catch (MongoException me) {
            cursor = null;
            throw me;
        }
        ArrayList<Review> reviews = new ArrayList<>();
        while(cursor.hasNext()) {
            Document cur = cursor.next();
            User user = getUser(cur.getString("userID"));
            reviews.add(new Review(cur.getInteger("rating"), cur.getString("comment"), user.getUserName()));
        }

        return reviews;
    }



    @Override
    public ArrayList<Album> searchAlbumTitle(String title){
        MongoCursor<Document> cursor = albumCollection.find(eq("title", Pattern.compile(title))).iterator();
        return curToAlbums(cursor);
    }


    @Override
    public ArrayList<Album> searchAlbumGenre(String genre){
        MongoCursor<Document> cursor = albumCollection.find(eq("genre", Pattern.compile(genre))).iterator();
        return curToAlbums(cursor);
    }
    @Override//a
    public ArrayList<Album> searchAlbumRating(int rating){
        MongoCursor<Document> cursor = albumCollection.find(gt("review.avgRating", rating)).iterator();
        return curToAlbums(cursor);
    }
    @Override
    public ArrayList<Album>  searchArtist(String artist){
        MongoCursor<Document> cursor = albumCollection.find(eq("artist.name", Pattern.compile(artist))).iterator();
        return curToAlbums(cursor);
    }

    @Override
    public  ArrayList<Movie>  searchMovieTitle(String title) {
        MongoCursor<Document> cursor = movieCollection.find(eq("title", Pattern.compile(title))).iterator();
        return curToMovies(cursor);
    }

    @Override
    public  ArrayList<Movie> searchDirectror(String director) {
        MongoCursor<Document> cursor = movieCollection.find(eq("director.name", Pattern.compile(director))).iterator();
        return curToMovies(cursor);
    }

    @Override
    public  ArrayList<Movie> searchMovieGenre(String genre) {
        MongoCursor<Document> cursor = movieCollection.find(eq("genre", Pattern.compile(genre))).iterator();
        return curToMovies(cursor);
    }

    @Override
    public  ArrayList<Movie> searchMovieRating(int rating) {
        MongoCursor<Document> cursor = movieCollection.find(gt("review.avgRating", rating)).iterator();
        return curToMovies(cursor);
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
