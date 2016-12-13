
package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class AddMovieDialog extends Dialog<AddMovieInfo> {
    
    private TextField title;
    private TextField releaseYear;
    private TextField length;
    private TextField genres;
    private TextField director;
    
    AddMovieDialog() {
        super(); // super constructor, modal by default
        
        title = new TextField();
        releaseYear = new TextField();
        length = new TextField();
        genres = new TextField();
        director = new TextField();
        
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Title"), 1, 1);
        grid.add(title, 2, 1);
        grid.add(new Label("Director"), 1, 2);
        grid.add(director, 2, 2);
        grid.add(new Label("Release year(yyyy)"), 1, 3);
        grid.add(releaseYear, 2, 3);
        grid.add(new Label("Length"), 1, 4);
        grid.add(length, 2, 4);
        grid.add(new Label("Genre"), 1, 5);
        grid.add(genres, 2, 5);
        
        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType(
                "Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        ButtonType buttonTypeCancel = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
        final Button cancelButton
                = (Button) this.getDialogPane().lookupButton(buttonTypeCancel);
        cancelButton.addEventFilter(
                ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                clearFields();
            }
        });

        this.setResultConverter(new Callback<ButtonType, AddMovieInfo>() {
            @Override
            public AddMovieInfo call(ButtonType b) {
                if (b == buttonTypeOk) {

                    AddMovieInfo addMovieInfo = new AddMovieInfo(
                            title.getText(),
                            releaseYear.getText(),
                            length.getText(),
                            genres.getText(),
                            director.getText());
                            
                    AddMovieDialog.this.clearFields();
                    return addMovieInfo;
                }
                return null;
            }
        });

        // If input in the form is not valid, consume the event (without
        // executing method "call".
        final Button okButton
                = (Button) this.getDialogPane().lookupButton(buttonTypeOk);
        okButton.addEventFilter(
                ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                //validationfunction doesn´t exist yet
                //send checking of data to controller or validate in view?
                /*String queryErrors = con.validateQueryDialogInput();
                if (queryErrors = "") { //send checking of data to controller?
                    ae.consume(); //not valid
                    showAlert(queryErrors);
                }*/
            }
        });
    }

    public void clearFields() {
        title.setText("");
        releaseYear.setText("");
        length.setText("");
        genres.setText("");
        director.setText("");
    }

    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
    
}

class AddMovieInfo {

    private final String title;
    private final String releaseYear;
    private final String length;
    private final String genres;
    private final String director;


    AddMovieInfo(String title, String releaseYear, String length,
            String genres, String director) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.length = length;
        this.genres = genres;
        this.director = director;
    }

    String getTitle() {
        return title;
    }
    
    String getReleaseYear() {
        return releaseYear;
    }
    
    String getLength() {
        return length;
    }
    
    String getGenres() {
        return genres;
    }
    
    String getDirector() {
        return director;
    }

}
