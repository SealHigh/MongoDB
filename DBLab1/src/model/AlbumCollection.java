
package model;

//import com.sun.deploy.security.ValidationState;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class AlbumCollection implements DBQueries {

    private Connection conn = null;
    private LoggedInUser loggedInUser;

    public AlbumCollection () {
        conn =  ConnectionConfiguration.getConnection(); //star connection for session
    }

    public boolean userLogIn(String userName, String password) {
        
        int number = 0;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS number FROM t_user WHERE userName = '"+ userName + "' AND password = '" + password + "';");
            
            while (rs.next()) {
                number = rs.getInt("number");
            }
            
        }
        catch (Exception e){
        }
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        
        if (number > 0) {
            rs = null;           
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT userId, userName FROM t_user WHERE userName = '"+ userName + "' AND password = '" + password + "';");
                
                /*if (rs == null) {
                    throw new Exception e;
                }*/
                
                while (rs.next()) {
                    LoggedInUser user = new LoggedInUser(rs.getString("userName"), rs.getInt("userId"));
                    setLoggedInUser(user);
                }                          
            }
            catch (Exception e){
            }
            finally {
                try { if (stmt != null) stmt.close(); } catch (Exception e) {}
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

    @Override
    public void insertRecord(Object o) {
        Album album = (Album)o;
        try {


            DatabaseMetaData meta = conn.getMetaData();

// Listing all stored procedures
//            ResultSet res = meta.getProcedures(null, "HERONG", "%");
//            System.out.println("Stored procedures:");
//            while (res.next()) {
//                System.out.println(
//                        "   "+res.getString("PROCEDURE_CAT")
//                                + ", "+res.getString("PROCEDURE_SCHEM")
//                                + ", "+res.getString("PROCEDURE_NAME"));
//            }
//            res.close();
            conn.setAutoCommit(false);
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
            System.out.println(ID);

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
            e.printStackTrace();
            //conn.rollback(); ?
        }
        finally {
            try {conn.setAutoCommit(true);} catch (Exception e){}
        }
    }

    @Override
    public void deleteRecord(Object o) {
        try {
            Album album = (Album)o;
           updateDB("DELETE FROM t_album WHERE albumID =" + album.getAlbumID());
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
            rs = stmt.executeQuery("select AVG(rating) from t_rating where albumId = '"+ albumId +"'");

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
            genre = stmt.executeQuery("SELECT genre FROM t_genre WHERE albumID = '"+ albumID +"'");
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
    public ArrayList<Artist> getArtists(int albumID){
        
        ArrayList<Artist> artists = new ArrayList<>();
        ResultSet artist = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            artist = stmt.executeQuery("SELECT artistName, nationality FROM t_artist WHERE artistID IN " +
                    "(SELECT  artistID FROM ct_album_artist WHERE albumID ='" + albumID +"')");                  
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
    public void updateDB(String statement){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(statement);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
    }

    @Override
    public ArrayList<Album> getAllRecords() {
        
        ArrayList<Album> albums = new ArrayList<>();
        ResultSet album = null;
        Statement stmt = null;
        try {
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
    public ArrayList<Album> searchRecord(SearchOptions option, String query) {
        ArrayList<Album> albums = new ArrayList<>();
        ResultSet album = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            album = stmt.executeQuery("SELECT * FROM t_album WHERE " + option.toString() +" LIKE '%" + query+ "%'");
            while(album.next()){
                albums.add(createAlbumFromResultSet(album));
            }
        } catch (Exception e) {
        }
        finally {
            try { if (album != null) album.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
        }
        return albums;
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
