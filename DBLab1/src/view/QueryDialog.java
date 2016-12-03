package view;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class QueryDialog extends Dialog<QueryInfo> {
    
    private TextField userInput;
    private ComboBox searchComboBox;

    QueryDialog() {
        super(); // super constructor, modal by default

        userInput = new TextField();
        
        ObservableList<String> options = 
        FXCollections.observableArrayList(
            "Album",
            "Artist",
            "Genre"
        );
        searchComboBox = new ComboBox(options);
        searchComboBox.setValue("Album"); //Preset Album option
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Search"), 1, 1);
        grid.add(searchComboBox, 2, 1);
        //grid.add(new Label("Search term"), 3, 1);
        grid.add(userInput, 3, 1);
        this.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType(
                "Search", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);

        this.setResultConverter(new Callback<ButtonType, QueryInfo>() {
            @Override
            public QueryInfo call(ButtonType b) {
                if (b == buttonTypeOk) {

                    QueryInfo qInfo = new QueryInfo(
                            userInput.getText(), (String) searchComboBox.getValue());
                    QueryDialog.this.clearFields();
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
        userInput.setText("");
        searchComboBox.setValue("Album");
    }

    void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING, message);
        alert.showAndWait();
    }
    
}

class QueryInfo {

    private final String userInput;
    private final String searchItem;

    QueryInfo(String userInput, String searchItem) {
        this.userInput = userInput;
        this.searchItem = searchItem;
    }

    String getUserInput() {
        return userInput;
    }
    
    String getSearchItem() {
        return searchItem;
    }
}
