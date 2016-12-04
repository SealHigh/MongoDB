
package model;

import java.util.ArrayList;

public class Album {

    private int albumID;
    private ArrayList<String> genres;
    private ArrayList<Artist> artists;
    private String title;   
    private String releaseDate;
    private String length;
    private int numberOfSongs;
    private String genreAsString; //Only to feed cell value factory in table view
    private String artistAsString; //Only to feed cell value factory in table view
    private ArrayList<Review> reviews;

    public Album (String title) { //Just for testing
        this.title = title;
    }
    public Album (int albumID, String title, ArrayList<Artist> artists, ArrayList<String> genres,
                  String releaseDate, String length, int numberOfSongs) {
        this.albumID = albumID;
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
    public Album (ArrayList<String> genres, String title, ArrayList<Artist> artists,
            String releaseDate, String length, int numberOfSongs) {
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
    
    public float getRating () {
        
        int total = 0;
        
        for (Review review : getReviews()) {
            total += review.getRating();
        }
        
        return ((float)total / getReviews().size());
    }

    /**
     *
     * @return the albumID
     */
    public int getAlbumID(){return albumID;}

    /**
     * Set albumID
     * @param albumID
     */
    public void setAlbumID(int albumID){this.albumID = albumID;}


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
    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    /**
     * @param numberOfSongs the numberOfSongs to set
     */
    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }
    
    private String artistToString() {
        String info = "";
        for (Artist artist : artists) {
            info += artist.getName() + ", ";
        }
        
        return info.replaceAll(", $", "");
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
