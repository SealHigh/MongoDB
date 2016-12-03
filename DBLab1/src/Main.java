

import model.ConnectionConfiguration;
import view.View;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.animation.Transition;
import javafx.application.Application;
import static javafx.application.Application.launch;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
import javafx.stage.Stage;
//import javafx.util.Duration;
import model.AlbumCollection;
import model.Album;
import model.Artist;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {

        //Setup connection to database, not sure if it should be done here
        Connection connection = null;
        try {
            connection =  ConnectionConfiguration.getConnection();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }



        // Some hard coded values for testing ///////////
        ArrayList<String> genres = new ArrayList<>();
        genres.add("Rock");
        genres.add("Pop");
        Artist a1 = new Artist("Sting", "Brittish");
        Artist a2 = new Artist("Prince", "American");
        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(a1);
        artists.add(a2);

        Album test1 = new Album(genres, "The Score", artists,
                "1990-08-12", "58", 12);
        test1.setAlbumID(1538);
        Album test2 = new Album(genres, "The Heist", artists,
                "2003-01-28", "33", 5);
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(test1);
        albums.add(test2);
        ////////////////////////////////////////////

        AlbumCollection ac = new AlbumCollection(connection);
        ac.deleteRecord(test1);
        View view = new View(ac, primaryStage);
        primaryStage.setTitle("Album Collection");

        /*
        //Create Pane to hold animation intro
        Pane pane = new Pane();

        //Text introText to be written in animation intro
        String content = "My Collection Of Books.";
        Text introText = new Text(10, 20, "");
        Font introFont = new Font("Courier", 18);
        introText.fontProperty().setValue(introFont);

        //Identical Text object to finished Text introText, never shown to user
        //Is needed to calculate width and height of finished Text introText
        Text checkStringDimensions = new Text(10, 20, content);
        checkStringDimensions.fontProperty().setValue(introFont);

        //Calculate what will be width and height of finished Text introText
        double widthOfText = checkStringDimensions.getLayoutBounds().getWidth();
        double heightOfText = checkStringDimensions.getLayoutBounds().getHeight();

        //Set placement of Text introText to center of scene - update later to
        //automatically fit screen?
        introText.setLayoutX((800 - widthOfText)/2.0);
        introText.setLayoutY((500 - heightOfText)/2.0);

        //Add Text introText to Pane
        pane.getChildren().add(introText);

        //Create scene introScene containing Pane
        Scene introScene = new Scene(pane, 800, 500);

        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
            }

            //Method interpolate is taken from example on
            //https://docs.oracle.com/javafx/2/api/javafx/animation/Transition.html
            @Override
            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                introText.setText(content.substring(0, n));
            }

        };
        animation.play();

        //Simple delay timer set to last same duration as animation intro
        //then sets stage to main scene
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(3000),
            ae -> primaryStage.setScene(view.getScene())
            )
        );
        timeline.play();

        //Set stage to introScene and show stage
        primaryStage.setScene(introScene);*/
        primaryStage.setScene(view.getScene()); //remove if introscene is added
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
