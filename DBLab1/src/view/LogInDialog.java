
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class LogInDialog extends Dialog<LogInInfo> {
    
    private TextField userNameField;
    private PasswordField passwordField;

    LogInDialog() {
        super(); // super constructor, modal by default
        
        userNameField = new TextField();
        passwordField = new PasswordField();
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Username"), 1, 1);
        grid.add(userNameField, 2, 1);
        grid.add(new Label("Password"), 1, 2);
        grid.add(passwordField, 2, 2);
        
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

        this.setResultConverter(new Callback<ButtonType, LogInInfo>() {
            @Override
            public LogInInfo call(ButtonType b) {
                if (b == buttonTypeOk) {

                    LogInInfo logInInfo = new LogInInfo(
                            userNameField.getText(),
                            passwordField.getText());
                            
                    LogInDialog.this.clearFields();
                    return logInInfo;
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
        userNameField.setText("");
        passwordField.setText("");
    }

    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }
    
}

class LogInInfo {

    private final String userName;
    private final String password;

    LogInInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    String getUserName() {
        return userName;
    }
    
    String getPassword() {
        return password;
    }

}
