
package model;

import java.util.ArrayList;

public class Movie {

    private String movieID;
    private String genres;
    private Director director;
    private String title;   
    private String releaseYear;
    private String length;
    private double  rating = 0;
    private String genreAsString; //Only to feed cell value factory in table view
    private String directorAsString; //Only to feed cell value factory in table view
    private ArrayList<Review> reviews;
    private String nrOfReviews;

    public Movie (String title) { //Just for testing
        this.title = title;
    }
    public Movie (String movieID, String title, Director director, String genres,
                  String releaseYear, String length, double rating,  String nrOfReviews) {
        this.movieID = movieID;
        this.genres = genres;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.length = length;
        this.rating =rating;
        this.directorAsString = directorToString();
        this.genreAsString = genreToString();
        this.nrOfReviews = nrOfReviews;
        reviews = new ArrayList<>();
    }
    
    public double getRating () {
        
        return rating;
    }

    public String getReviews() {
        return nrOfReviews;
    }
    /**
     *
     * @return the albumID
     */
    public String getMovieID(){return movieID;}

    /**
     * Set albumID
     * @param movieID
     */
    public void setMovieID(String movieID){this.movieID = movieID;}


    /**
     * @return the genre
     */
    public String getGenre() {
        return genres;
    }

    /**
     * @param genres the genre to set
     */
    public void setGenre(String genres) {
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

        
        return genres;
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

    
    public void addReview(Review review) {
        reviews.add(review);
    }
    
}

