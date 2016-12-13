
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class AddMovieReviewDialog extends Dialog<ReviewMovieInfo> {
    
    private TextField rating;
    private TextArea comment;

    AddMovieReviewDialog() {
        super(); // super constructor, modal by default
        
        rating = new TextField();
        comment = new TextArea();
        
        comment.setPrefRowCount(5);
        comment.setPrefColumnCount(100);
        comment.setWrapText(true);
        comment.setPrefWidth(150);
        //GridPane.setHalignment(cssEditorFld, HPos.CENTER);
        //gridpane.add(cssEditorFld, 0, 1);
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Rating"), 1, 1);
        grid.add(rating, 2, 1);
        grid.add(new Label("Comment"), 1, 2);
        grid.add(comment, 2, 2);
        
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

        this.setResultConverter(new Callback<ButtonType, ReviewMovieInfo>() {
            @Override
            public ReviewMovieInfo call(ButtonType b) {
                if (b == buttonTypeOk) {

                    ReviewMovieInfo reviewMovieInfo = new ReviewMovieInfo(
                            rating.getText(),
                            comment.getText());
                            
                    AddMovieReviewDialog.this.clearFields();
                    return reviewMovieInfo;
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
        rating.setText("");
        comment.setText("");
    }

    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
    
}

class ReviewMovieInfo {

    private final String rating;
    private final String comment;

    ReviewMovieInfo(String userName, String password) {
        this.rating = userName;
        this.comment = password;
    }

    String getRating() {
        return rating;
    }
    
    String getComment() {
        return comment;
    }

}
