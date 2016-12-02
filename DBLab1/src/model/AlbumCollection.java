
package model;

import java.util.ArrayList;


public class AlbumCollection implements DBQueries {
    
    private ArrayList<Album> queriedAlbums;
    private ArrayList<Artist> queriedArtists;
    private ArrayList<User> queriedUsers;
    private ArrayList<Review> queriedReviews;
    
    public AlbumCollection (ArrayList<Album> listOfAlbums) {
        //Constructor purely for testing
        this.queriedAlbums = listOfAlbums;
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
    
    /*public ArrayList<Album> getQueriedAlbums() {
        
        ArrayList<Album> queriedAlbumsCopy = new ArrayList<>(queriedAlbums);
        return queriedAlbumsCopy;        
    }*/
    
    public ArrayList<Album> getCurrentAlbums() {
        ArrayList<Album> queriedAlbumsCopy = new ArrayList<>(queriedAlbums);
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
        
        //Code for inserting current listOfAlbums to DB
        
    }
    
}
