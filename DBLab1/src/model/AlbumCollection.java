
package model;

//import com.sun.deploy.security.ValidationState;

import com.mysql.jdbc.exceptions.MySQLDataException;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AlbumCollection implements DBQueries {

    private Connection conn = null;
    private LoggedInUser loggedInUser;

    public AlbumCollection () {
        conn =  ConnectionConfiguration.getConnection(); //star connection for session
    }

    @Override
    public boolean userLogIn(String userName, String password) {
        
        int number = 0;
        ResultSet rs = null;
        PreparedStatement checkLogin = null;
        PreparedStatement getLoginInfo = null;
        try {
            String sql = "SELECT COUNT(*) AS number FROM t_user WHERE userName = ? AND password = ?";
            checkLogin = conn.prepareStatement(sql);
            checkLogin.setString(1, userName);
            checkLogin.setString(2, password);            
            checkLogin.execute();
            rs = checkLogin.getResultSet();
            
            while (rs.next()) {
                number = rs.getInt("number");
            }
            
        }
        catch (Exception e){
        }
        finally {
            try { if (checkLogin != null) checkLogin.close(); } catch (Exception e) {}
        }
        
        if (number > 0) {
            rs = null;           
            try {
                String sql = "SELECT userId, userName FROM t_user WHERE userName = ? AND password = ?";
                getLoginInfo = conn.prepareStatement(sql);
                getLoginInfo.setString(1, userName);
                getLoginInfo.setString(2, password);
                getLoginInfo.execute();
                rs = getLoginInfo.getResultSet();
                
                while (rs.next()) {
                    LoggedInUser user = new LoggedInUser(rs.getString("userName"), rs.getInt("userId"));
                    setLoggedInUser(user);
                }                          
            }
            catch (Exception e){
            }
            finally {
                try { if (getLoginInfo != null) getLoginInfo.close(); } catch (Exception e) {}
            }
        }
        return number > 0;
    }

    private Album createAlbumFromResultSet(ResultSet album){
        Album tempAlbum = null;
        try {
            ArrayList<Artist> artists = getArtists(album.getInt("albumId"));
            ArrayList<String> genres = getGenres(album.getInt("albumId"));
            
            tempAlbum = new Album(album.getInt("albumId"), album.getString("title"), artists, genres,
                    album.getString("releaseDate"),  album.getString("lengthMinutes"),  album.getInt("nrOfSongs"), getAlbumRating(album.getInt("albumId")));
        }
        catch (Exception e){

        }
        return tempAlbum;

    }

    public void setAlbumRating(int rating, String comment, int albumID) throws Exception{

            String sql = "INSERT INTO t_review(rating, userComment, albumID, userID) VALUES(?,?,?,?)";
            PreparedStatement addAlbumRating = conn.prepareStatement(sql);
            addAlbumRating.setInt(1,rating);
            addAlbumRating.setString(2, comment);
            addAlbumRating.setInt(3, albumID);
            addAlbumRating.setInt(4, loggedInUser.getUserId());
            System.out.println(albumID + "  " + loggedInUser.getUserId() + "  "+ rating);
            addAlbumRating.execute();

    }


    @Override
    public void insertRecord(Object o) throws ParseException {
        Album album = (Album)o;
        try {

            conn.setAutoCommit(false);//Transaction, dont want to add album if anything goes wrong with artist or genre and viceversa

            String call = "{call insertAlbum(?,?,?,?,?)}";
            CallableStatement insertAlbum = conn.prepareCall(call);
            insertAlbum.registerOutParameter(1, Types.INTEGER);
            insertAlbum.setString(2, album.getTitle());
            insertAlbum.setInt(3, album.getNumberOfSongs());
            insertAlbum.setInt(4, Integer.parseInt(album.getLength()));
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            insertAlbum.setDate(5, new Date(format.parse(album.getReleaseDate()).getTime()));
            insertAlbum.execute();
            int ID = insertAlbum.getInt(1); //The id of the new album for adding artist and genre

            //Adds artist to t_artits and connects artist ID with album ID in connection table
            call = "{call insertArtist(?,?,?)}";
            CallableStatement insertArtist = conn.prepareCall(call);
            for (Artist artist: album.getArtists()
                 ) {
                insertArtist.setInt(1, ID);
                insertArtist.setString(2, artist.getName());
                insertArtist.setString(3, artist.getNationality());
                insertArtist.execute();
            }

            String sql = "INSERT INTO t_genre VALUES(?,?)";
            PreparedStatement addAlbumGenre = conn.prepareStatement(sql);
            addAlbumGenre.setInt(1, ID);
            addAlbumGenre.setString(2, album.getGenreAsString());
            addAlbumGenre.execute();

            conn.commit();

        } catch (Exception e) {
            try {if(conn != null) conn.rollback();} catch (Exception ex){}
        }
        finally {
            try {conn.setAutoCommit(true);} catch (Exception e){}
        }
    }

    @Override
    public void deleteRecord(Object o) {
        Album album = (Album)o;
        try {
            String sql = "DELETE FROM t_album WHERE albumID = ?";
            PreparedStatement deleteAlbum = conn.prepareStatement(sql);
            deleteAlbum.setInt(1, album.getAlbumID());
            deleteAlbum.execute();


        } catch (Exception e) {

        }
    }

    @Override
    public int getAlbumRating(int albumId){
        int rating= 0;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "select AVG(rating) from t_review where albumId = ?";
            PreparedStatement getRating = conn.prepareStatement(sql);
            getRating.setInt(1, albumId);
            getRating.execute();
            rs = getRating.getResultSet();

            if(rs.next())
                rating = rs.getInt("AVG(rating)");

        }
        catch (Exception e){

        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return rating;
    }
    
    @Override
    public int getMovieRating(int albumId){
        int rating= 0;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select AVG(rating) from t_review where albumId = '"+ albumId +"'");

            if(rs.next())
                rating = rs.getInt("AVG(rating)");

        }
        catch (Exception e){

        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return rating;
    }

    @Override
    public void rateAlbum(Object o){

    }

    @Override
    public  ArrayList<String> getGenres(int albumID){
        ArrayList<String> genres = new ArrayList<>();
        ResultSet genre = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt = conn.createStatement();
            String sql = "SELECT genre FROM t_genre WHERE albumID = ?";
            PreparedStatement getGenre = conn.prepareStatement(sql);
            getGenre.setInt(1, albumID);
            getGenre.execute();
            genre = getGenre.getResultSet();

            while (genre.next()) {
                genres.add(genre.getString("genre"));
            }
        }
        catch (Exception e){
        }
        finally {
            try { if (genre != null) genre.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return genres;
    }

    @Override
    public ArrayList<Review> getReviews(int albumID){

        ArrayList<Review> reviews = new ArrayList<>();
        ResultSet review = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT userName, rating, userComment FROM t_review t1\n" +
                    "INNER JOIN t_user t2 ON t1.userId = t2.userId\n" +
                    "WHERE albumId = ?";
            PreparedStatement getReviews = conn.prepareStatement(sql);
            getReviews.setInt(1, albumID);
            getReviews.execute();
            review = getReviews.getResultSet();

            while (review.next()) {
                Review temp = new Review(review.getInt("rating"), review.getString("userComment"), review.getString("userName"));
                reviews.add(temp);
            }
        }
        catch (Exception e){

        }
        finally {
            try { if (review != null) review.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return reviews;
    }

    @Override
    public ArrayList<Artist> getArtists(int albumID){
        
        ArrayList<Artist> artists = new ArrayList<>();
        ResultSet artist = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            String sql = "SELECT artistName, nationality FROM t_artist WHERE artistID IN " +
                    "(SELECT  artistID FROM ct_album_artist WHERE albumID = ?)";
            PreparedStatement getArtist = conn.prepareStatement(sql);
            getArtist.setInt(1, albumID);
            getArtist.execute();
            artist = getArtist.getResultSet();
            while (artist.next()) {
                Artist temp = new Artist(artist.getString("artistName"), artist.getString("nationality"));
                artists.add(temp);
            }
        }
        catch (Exception e){

        }
        finally {
            try { if (artist != null) artist.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return artists;
    }

    @Override
    public Director getDirector(int albumID){
        
        Director director= null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT artistName, nationality FROM t_artist WHERE artistID IN " +
                    "(SELECT  artistID FROM ct_album_artist WHERE albumID ='" + albumID +"')");                  
            while (rs.next()) {
                Director temp = new Director(rs.getString("artistName"), rs.getString("nationality"));
                director = temp;
            }
        }
        catch (Exception e){

        }
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return director;
    }

    @Override
    public ArrayList<Album> getAllRecords() {

        ArrayList<Album> albums = new ArrayList<>();
        ResultSet album = null;
        Statement stmt = null;
        try {
            //We dont need transation here since its only select calls
            stmt = conn.createStatement();
            album = stmt.executeQuery("SELECT * FROM t_album");

            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
    }

    @Override
    public ArrayList<Album>  searchTitle(String title){
        ArrayList<Album> albums = new ArrayList<>();
        Statement stmt = null;
        ResultSet album = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM t_album WHERE title LIKE ?";
            PreparedStatement getAlbums = conn.prepareStatement(sql);
            getAlbums.setString(1, "%"+title+"%");
            getAlbums.execute();
            album = getAlbums.getResultSet();
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
    }


    @Override
    public ArrayList<Album>  searchGenre(String genre){
        ArrayList<Album> albums = new ArrayList<>();
        Statement stmt = null;
        ResultSet album = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * from t_album where albumID In\n" +
                    "(SELECT albumID FROM t_genre WHERE genre LIKE ?)";
            PreparedStatement getAlbums = conn.prepareStatement(sql);
            getAlbums.setString(1, "%"+genre+"%");
            getAlbums.execute();
            album = getAlbums.getResultSet();
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
    }
    @Override//a
    public ArrayList<Album>  searchRating(String rating){
        ArrayList<Album> albums = new ArrayList<>();
        Statement stmt = null;
        ResultSet album = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * from t_album where albumID In\n" +
                    "(SELECT albumID FROM t_review WHERE rating = ?)";
            PreparedStatement getAlbums = conn.prepareStatement(sql);
            getAlbums.setString(1, rating);
            getAlbums.execute();
            album = getAlbums.getResultSet();
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
    }
    @Override
    public ArrayList<Album>  searchArtist(String artist){
        ArrayList<Album> albums = new ArrayList<>();
        Statement stmt = null;
        ResultSet album = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * from t_album where albumID IN\n" +
                    "(SELECT albumID from ct_album_artist where artistID IN\n" +
                    "(SELECT artistID FROM t_artist WHERE artistName LIKE ?))";
            PreparedStatement getAlbums = conn.prepareStatement(sql);
            getAlbums.setString(1, "%"+artist+"%");
            getAlbums.execute();
            album = getAlbums.getResultSet();
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
    }
    
    @Override
    public ArrayList<Movie> getAllMovies() {

        ArrayList<Movie> movies = new ArrayList<>();
        ResultSet movie = null;
        Statement stmt = null;
        try {
            //We dont need transation here since its only select calls
            stmt = conn.createStatement();
            movie = stmt.executeQuery("SELECT * FROM t_movie");

            while(movie.next()){
                movies.add(createMovieFromResultSet(movie));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (movie != null) movie.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return movies;
    }

    /**
     * @return the loggedInUser
     */
    public LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
