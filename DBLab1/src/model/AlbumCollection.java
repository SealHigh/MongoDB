
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class AlbumCollection implements DBQueries {
    
    private ArrayList<Album> queriedAlbums;
    private ArrayList<Artist> queriedArtists;
    private ArrayList<User> queriedUsers;
    private ArrayList<Review> queriedReviews;
    private Connection connection;
    
    public AlbumCollection (ArrayList<Album> listOfAlbums) {
        //Constructor purely for testing
        this.queriedAlbums = listOfAlbums;

        this.queriedArtists = queriedArtists;
        this.queriedUsers = queriedUsers;
        this.queriedReviews = queriedReviews;
        
    }
    
    public AlbumCollection (Connection connection) {
        this.connection = connection;
        this.queriedAlbums = new ArrayList<>();
        this.queriedArtists = new ArrayList<>();
        this.queriedUsers = new ArrayList<>();
        this.queriedReviews = new ArrayList<>();
        
    }


    private ArrayList<String> getGenres(int albumID){
        ArrayList<String> genres = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet genre = stmt.executeQuery("SELECT genre FROM t_genre WHERE albumID = '"+ albumID +"'");
            while (genre.next()) {
                genres.add(genre.getString("genre"));
            }
        }
        catch (Exception e){

        }
        return genres;
    }

    private ArrayList<Artist> getArtists(int albumID){
        ArrayList<Artist> artists = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet artist = stmt.executeQuery("SELECT artistName, nationality FROM t_artist WHERE artistID IN " +
                    "(SELECT  artistID FROM ct_album_artist WHERE albumID ='" + albumID +"')");
            while (artist.next()) {
                Artist temp = new Artist(artist.getString("artistName"), artist.getString("nationality"));
                artists.add(temp);
            }
        }
        catch (Exception e){

        }
        return artists;
    }

    public void addAlbum(Album album){
        queriedAlbums.add(album);
    }

    
    /*public ArrayList<Album> getQueriedAlbums() {
        
        ArrayList<Album> queriedAlbumsCopy = new ArrayList<>(queriedAlbums);
        return queriedAlbumsCopy;        
    }*/

    private Album createAlbumFromResultSet(ResultSet album){
        Album tempAlbum = null;
        try {
            ArrayList<Artist> artists = getArtists(album.getInt("albumId"));
            ArrayList<String> genres = getGenres(album.getInt("albumId"));

            tempAlbum = new Album(album.getInt("albumId"), album.getString("title"), artists, genres,
                    album.getString("releaseDate"),  album.getString("lengthMinutes"),  album.getInt("nrOfSongs"));
        }
        catch (Exception e){

        }
        return tempAlbum;

    }
    @Override
    public ArrayList<Album> getAllRecords() {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet album = stmt.executeQuery("SELECT * FROM t_album");
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<Album> queriedAlbumsCopy = albums;
        return queriedAlbumsCopy;  
    }

    @Override
    public ArrayList<Album> searchRecord(SearchOptions option, String query) {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet album = stmt.executeQuery("SELECT * FROM t_album WHERE " + option.toString() +" LIKE '%" + query+ "%'");
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        } catch (Exception e) {

        }
        return albums;
    }
    @Override
    public void addRecord(Object o) {
        try {
            Statement stmt = connection.createStatement();
            Album album = (Album)o;
            stmt.executeUpdate("INSERT INTO t_album (title, nrOfSongs, lengthMinutes, releaseDate) VALUES ('" + album.getTitle() +
                    "','" + album.getNumberOfSongs() + "','" + album.getLength() + "','" + album.getReleaseDate() + "')");

        } catch (Exception e) {

        }
    }

    @Override
    public void deleteRecord(Object o) {
        try {
            Statement stmt = connection.createStatement();
            Album album = (Album)o;
            stmt.executeUpdate("DELETE FROM t_album WHERE albumID =" + album.getAlbumID());

        } catch (Exception e) {

        }
    }



    @Override
    public ArrayList<Album> getSelection(String query) {
        
        ArrayList<Album> albumsInDB = new ArrayList<>();
        
        //Some code to query DB
        
        return albumsInDB;
    }


    @Override
    public <Album> void updateDB(ArrayList<Album> listOfAlbums) {

        //Loops through the list and inserts them into the database
        try {
            Statement stmt = connection.createStatement();
            for (Album album: listOfAlbums
                    ) {

                model.Album a = (model.Album)album;
                stmt.executeUpdate("INSERT INTO t_album (title, nrOfSongs, lengthMinutes, releaseDate) VALUES ('"+ a.getTitle() +
                        "','" + a.getNumberOfSongs() + "','" + a.getLength() + "','" + a.getReleaseDate()+"')");

                //Get the albumID of the album
                ResultSet albumID = stmt.executeQuery("SELECT albumId FROM t_album");
                for (String genre: a.getGenre()
                     ) {
                    try {
                        stmt.executeUpdate("INSERT INTO t_genre (albumId, genre) VALUES ('"+ albumID.getInt("albumId") +"','" + genre+"')");
                    }
                    catch (Exception e){

                    }
                }

                ResultSet rs = stmt.executeQuery("SELECT title FROM t_album");
                while (rs.next()) {
                    System.out.println(rs.getString("title"));

                }
            }
        }
        catch (Exception e){
            //Handle exception
        }


        }
        //Code for inserting current listOfAlbums to DB
        
    }
    

