
package model;

import java.util.ArrayList;

public class Album {

    private String albumID;
    private ArrayList<String> genres;
    private ArrayList<Artist> artists;
    private String title;   
    private String releaseDate;
    private String length;
    private String  rating;
    private String numberOfSongs;
    private String genreAsString; //Only to feed cell value factory in table view
    private String artistAsString; //Only to feed cell value factory in table view
    private ArrayList<Review> reviews;

    public Album (String title) { //Just for testing
        this.title = title;
    }
    public Album (String albumID, String title, ArrayList<Artist> artists, ArrayList<String> genres,
                  String releaseDate, String length, String numberOfSongs, String rating) {
        this.albumID = albumID;
        this.genres = genres;
        this.title = title;
        this.artists = artists;
        this.releaseDate = releaseDate;
        this.length = length;
        this.numberOfSongs = numberOfSongs;
        this.rating =rating;
        this.artistAsString = artistToString();
        this.genreAsString = genreToString();
        reviews = new ArrayList<>();
    }
    public Album (ArrayList<String> genres, String title, ArrayList<Artist> artists,
            String releaseDate, String length, String numberOfSongs) {
                this.genres = genres;
                this.title = title;
                this.artists = artists;
                this.releaseDate = releaseDate;
                this.length = length;
                this.numberOfSongs = numberOfSongs;
                this.artistAsString = artistToString();
                this.genreAsString = genreToString();
                reviews = new ArrayList<>();
    }


    public String getRating () {
        
        return rating;
    }

    /**
     *
     * @return the albumID
     */
    public String getAlbumID(){return albumID;}

    /**
     * Set albumID
     * @param albumID
     */
    public void setAlbumID(String albumID){this.albumID = albumID;}


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
     * @return the artists
     */
    public ArrayList<Artist> getArtists() {
        return artists;
    }

    /**
     * @param artists the artists to set
     */
    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    /**
     * @return the releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    /**
     * @return the numberOfSongs
     */
    public String getNumberOfSongs() {
        return numberOfSongs;
    }

    /**
     * @param numberOfSongs the numberOfSongs to set
     */
    public void setNumberOfSongs(String numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
    
    private String artistToString() {
        String info = "";
        for (Artist artist : this.artists) {
            info += artist.getName() + ", ";
        }
        
        return info.replaceAll(", $", "");
    }
    
    /**
     * 
     * @return genres as a string
     */
    private String genreToString() {
        String info = "";
        for (String genre : genres) {
            info += genre + ", ";
        }
        
        return info.replaceAll(", $", "");
    }

    /**
     * @return genres as a string for cellValueFactory
     */
    public String getGenreAsString() {
        return genreAsString;
    }

    /**
     * @return the artistAsString
     */
    public String getArtistAsString() {
        return artistAsString;
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
