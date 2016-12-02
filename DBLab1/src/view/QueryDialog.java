package view;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class QueryDialog extends Dialog<QueryInfo> {
    
    private TextField userInput;

    QueryDialog() {
        super(); // super constructor, modal by default

        userInput = new TextField();
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Search term"), 1, 1);
        grid.add(userInput, 2, 1);
        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType(
                "Search", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);

        this.setResultConverter(new Callback<ButtonType, QueryInfo>() {
            @Override
            public QueryInfo call(ButtonType b) {
                if (b == buttonTypeOk) {

                    QueryInfo qInfo = new QueryInfo(
                            userInput.getText());
                    QueryDialog.this.clearField();
                    return qInfo;
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
                String queryErrors = con.validateQueryDialogInput();
                if (queryErrors = "") { //send checking of data to controller?
                    ae.consume(); //not valid
                    showAlert(queryErrors);
                }
            }
        });
    }

    public void clearField() {
        userInput.setText("");
    }

    void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING, message);
        alert.showAndWait();
    }
    
}

class QueryInfo {

    private final String userInput;

    QueryInfo(String userInput) {
        this.userInput = userInput;     
    }

    String getUserInput() {
        return userInput;
    }

}
