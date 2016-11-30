package model;


import java.util.ArrayList;


public interface DBQueries <E> {
    
    public abstract ArrayList<E> getSelection(String query);
    public abstract void updateDB(ArrayList<E> arrayList);
    
}
