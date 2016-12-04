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

    /**
     * Retrivies all records in the database
     * @param <T>
     * @return a list of all the records
     */
    <T> ArrayList<T> getAllRecords();

    /**
     * Adds a record of the object to the database
     * @param o
     */
    void addRecord(Object o);

    /**
     * Deletes given record from the database
     * @param o
     */
    void deleteRecord(Object o);

    /**
     * Searches through a table records with given value
     * @param option the column to search in
     * @param query the value to search for
     * @return a list of the records with the value
     */
    <T> ArrayList<T> searchRecord(SearchOptions option, String query);

    
}