package model;


import java.util.ArrayList;

public interface DBQueries {
    
    public abstract <T> ArrayList<T> getSelection(String query);
    public abstract <T> void updateDB(ArrayList<T> arrayList);
    
}