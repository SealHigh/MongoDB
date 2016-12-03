
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
    
    public AlbumCollection (ArrayList<Album> listOfAlbums, Connection connection) {
        //Constructor purely for testing
        this.queriedAlbums = listOfAlbums;
        this.connection = connection;
        this.queriedArtists = queriedArtists;
        this.queriedUsers = queriedUsers;
        this.queriedReviews = queriedReviews;
        
    }
    
    public AlbumCollection () {
        
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
        ArrayList<Album> queriedAlbumsCopy = queriedAlbums;
        return queriedAlbumsCopy;  
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
    

