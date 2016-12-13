package model;

//import org.jetbrains.annotations.Contract;

/**
 * Created by Martin on 2016-12-04.
 */
public enum SearchMovieOptions {
    TITLE("title"), DIRECTOR("director"), GENRE("genre"), Rating("rating");
    private final String searchQuery;

    SearchMovieOptions(String s) {
        searchQuery = s;
    }

    @Override
    public String toString(){
        return this.searchQuery;
    }
}
