
package model;

import java.util.ArrayList;


public class AlbumCollection implements DBQueries<Album> {
    
    private ArrayList<Album> listOfAlbums;
    
    public AlbumCollection (ArrayList<Album> listOfAlbums) {
        
        this.listOfAlbums = listOfAlbums;
        
    }
    
    public ArrayList<Album> getAlbumsCopy() {
        
        ArrayList<Album> listOfAlbumsCopy = new ArrayList<>(listOfAlbums);
        return listOfAlbumsCopy;        
    }
    
    @Override
    public ArrayList<Album> getSelection(String query) {
        
        ArrayList<Album> albumsInDB = new ArrayList<>();
        
        //Some code to query DB
        
        return albumsInDB;
    }
    
    @Override
    public void updateDB(ArrayList<Album> listOfAlbums) {
        
        //Code for inserting current listOfAlbums to DB
        
    }
    
}
