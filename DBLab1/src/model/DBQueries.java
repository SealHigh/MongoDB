package model;


import java.util.ArrayList;

public interface DBQueries {
    
    <T> ArrayList<T> getSelection(String query);

    /**
     * Adds all albums in @param arrayList
     * to the database
     * @param <T>
     */
    <T> void updateDB(ArrayList<T> arrayList);
    ArrayList<Album> getCurrentAlbums();
    void addRecord(Object o);
    void deleteRecord(Object o);
    ArrayList<Album> searchRecord(SearchOptions option, String query);
    
}