package model;


import java.util.ArrayList;

public interface DBQueries {
    
    public abstract <T> ArrayList<T> getSelection(String query);

    /**
     * Adds all albums in @param arrayList
     * to the database
     * @param <T>
     */
    public abstract <T> void updateDB(ArrayList<T> arrayList);
    public abstract ArrayList<Album> getCurrentAlbums();
    
}