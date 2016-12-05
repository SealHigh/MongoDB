
package model;

import java.util.ArrayList;

public class Movie {

    private int movieID;
    private ArrayList<String> genres;
    private Director director;
    private String title;   
    private String releaseYear;
    private String length;
    private int  rating = 0;
    private String genreAsString; //Only to feed cell value factory in table view
    private String directorAsString; //Only to feed cell value factory in table view
    private ArrayList<Review> reviews;

    public Movie (String title) { //Just for testing
        this.title = title;
    }
    public Movie (int movieID, String title, Director director, ArrayList<String> genres,
                  String releaseYear, String length, int rating) {
        this.movieID = movieID;
        this.genres = genres;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating =rating;
        this.directorAsString = directorToString();
        this.genreAsString = genreToString();
        reviews = new ArrayList<>();
    }
    public Movie (ArrayList<String> genres, String title, Director director,
            String releaseYear, String length) {
                this.genres = genres;
                this.title = title;
                this.director = director;
                this.releaseYear = releaseYear;
                this.length = length;
                this.directorAsString = directorToString();
                this.genreAsString = genreToString();
                reviews = new ArrayList<>();
    }
    
    public float getRating () {
        
        return rating;
    }

    /**
     *
     * @return the albumID
     */
    public int getMovieID(){return movieID;}

    /**
     * Set albumID
     * @param movieID
     */
    public void setMovieID(int movieID){this.movieID = movieID;}


    /**
     * @return the genre
     */
    public ArrayList<String> getGenre() {
        return genres;
    }

    /**
     * @param genres the genre to set
     */
    public void setGenre(ArrayList<String> genres) {
        this.genres = genres;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the director
     */
    public Director getDirector() {
        return director;
    }

    /**
     * @param artists the artists to set
     */
    public void setDirector(Director director) {
        this.director = director;
    }

    /**
     * @return the releaseYear
     */
    public String getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear the releaseYear to set
     */
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * @return the length
     */
    public String getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(String length) {
        this.length = length;
    }
    
    private String directorToString() {
        String info = director.getName();
        
        return info;
    }
    
    private String genreToString() {
        String info = "";
        for (String genre : genres) {
            info += genre + ", ";
        }
        
        return info.replaceAll(", $", "");
    }

    /**
     * @return the genreAsString
     */
    public String getGenreAsString() {
        return genreAsString;
    }

    /**
     * @return the artistAsString
     */
    public String getDirectorAsString() {
        return directorAsString;
    }

    /**
     * @return the reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }
    
    public void addReview(Review review) {
        reviews.add(review);
    }
    
}

