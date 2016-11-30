
package model;

import java.util.ArrayList;

public class Album {
    
    private ArrayList<String> genre;
    private String title;
    private ArrayList<Artist> artists;
    private String releaseDate;
    private String length;
    private int numberOfSongs;
    private ArrayList<Review> reviews;
    
    public Album (ArrayList<String> genre, String title, ArrayList<Artist> artists, 
            String releaseDate, String length, int numberOfSongs) {
                this.genre = genre;
                this.title = title;
                this.artists = artists;
                this.releaseDate = releaseDate;
                this.length = length;
                this.numberOfSongs = numberOfSongs;
    }
    
    public float getRating () {
        
        int total = 0;
        
        for (Review review : reviews) {
            total += review.getRating();
        }
        
        return ((float)total / reviews.size());
    }
    
    /**
     * @return the genre
     */
    public ArrayList<String> getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
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
    
    
}
