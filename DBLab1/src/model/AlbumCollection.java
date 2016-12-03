
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Album;


public class AlbumCollection implements DBQueries {
    
    private ArrayList<Album> queriedAlbums;
    private ArrayList<Artist> queriedArtists;
    private ArrayList<User> queriedUsers;
    private ArrayList<Review> queriedReviews;
    private Connection connection;
    
    public AlbumCollection (ArrayList<Album> listOfAlbums) {
        //Constructor purely for testing
        this.queriedAlbums = listOfAlbums;

        this.queriedArtists = queriedArtists;
        this.queriedUsers = queriedUsers;
        this.queriedReviews = queriedReviews;
        
    }
    
    public AlbumCollection (Connection connection) {
        this.connection = connection;
        this.queriedAlbums = new ArrayList<>();
        this.queriedArtists = new ArrayList<>();
        this.queriedUsers = new ArrayList<>();
        this.queriedReviews = new ArrayList<>();
        
    }

    public void addAlbum(Album album){
        queriedAlbums.add(album);
    }
    
    /*public ArrayList<Album> getQueriedAlbums() {
        
        ArrayList<Album> queriedAlbumsCopy = new ArrayList<>(queriedAlbums);
        return queriedAlbumsCopy;        
    }*/
    
    @Override
    public ArrayList<Album> getCurrentAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT title, releaseDate, lengthMinutes, nrOfSongs FROM t_album");

            //Temporary, this will come from database later
            ArrayList<String> genres = new ArrayList<>();
            genres.add("Rock");
            genres.add("Pop");
            Artist a1 = new Artist("Sting", "Brittish");
            Artist a2 = new Artist("Prince", "American");
            ArrayList<Artist> artists = new ArrayList<>();
            artists.add(a1);
            artists.add(a2);

            while(rs.next()){
                Album temp = new Album(genres, rs.getString("title"), artists,
                        rs.getString("releaseDate"),  rs.getString("lengthMinutes"),  rs.getInt("nrOfSongs"));
                albums.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        ArrayList<Album> queriedAlbumsCopy = albums;
        return queriedAlbumsCopy;  
    }

    @Override
    public void addRecord(Object o) {
        try {
            Statement stmt = connection.createStatement();
            Album album = (Album)o;
            stmt.executeUpdate("INSERT INTO t_album (title, nrOfSongs, lengthMinutes, releaseDate) VALUES ('" + album.getTitle() +
                    "','" + album.getNumberOfSongs() + "','" + album.getLength() + "','" + album.getReleaseDate() + "')");

        } catch (Exception e) {

        }
    }

    @Override
    public void deleteRecord(Object o) {
        try {
            Statement stmt = connection.createStatement();
            Album album = (Album)o;
            stmt.executeUpdate("DELETE FROM t_album WHERE albumID =" + album.getAlbumID());

        } catch (Exception e) {

        }
    }
    @Override
    public ArrayList<Album> getSelection(String query) {
        
        ArrayList<Album> albumsInDB = new ArrayList<>();
        
        //Some code to query DB
        
        return albumsInDB;
    }
    
    @Override
    public <Album> void updateDB(ArrayList<Album> listOfAlbums) {

        //Loops through the list and inserts them into the database
        try {
            Statement stmt = connection.createStatement();
            for (Album album: listOfAlbums
                    ) {

                model.Album a = (model.Album)album;
                stmt.executeUpdate("INSERT INTO t_album (title, nrOfSongs, lengthMinutes, releaseDate) VALUES ('"+ a.getTitle() +
                        "','" + a.getNumberOfSongs() + "','" + a.getLength() + "','" + a.getReleaseDate()+"')");

                //Get the albumID of the album
                ResultSet albumID = stmt.executeQuery("SELECT albumId FROM t_album");
                for (String genre: a.getGenre()
                     ) {
                    try {
                        stmt.executeUpdate("INSERT INTO t_genre (albumId, genre) VALUES ('"+ albumID.getInt("albumId") +"','" + genre+"')");
                    }
                    catch (Exception e){

                    }
                }

                ResultSet rs = stmt.executeQuery("SELECT title FROM t_album");
                while (rs.next()) {
                    System.out.println(rs.getString("title"));

                }
            }
        }
        catch (Exception e){
            //Handle exception
        }


        }
        //Code for inserting current listOfAlbums to DB
        
    }
    

