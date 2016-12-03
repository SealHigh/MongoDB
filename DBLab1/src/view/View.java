
package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.User;
import model.AlbumCollection;
import model.Review;
import model.Artist;


public class View {

    private AlbumCollection ac;
    private QueryDialog queryDialog;
    private Stage primaryStage;
    private Scene scene;
    private TableView<Album> mainTable;
    private BorderPane border;
    
    public View(AlbumCollection ac, Stage primaryStage) throws IOException, ClassNotFoundException {
        
        this.ac = ac;
        this.primaryStage = primaryStage;
        Controller con = new Controller(ac, this);
        queryDialog = new QueryDialog();
        //sbd = new SearchBooksDialog(primaryStage);
        
        border = new BorderPane();
        
        //Create buttons 'Add Book', 'Search Books' and 'Remove Book' at bottom
        Button addAlbumButton = new Button("Add Album");
        addAlbumButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                //con.handleAddAlbumEvent() ?
                
            }
        });
        
        Button searchAlbumsButton = new Button("Search Albums");
        searchAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {
                
            @Override
            public void handle(ActionEvent event) {
                Optional<QueryInfo> result
                        = queryDialog.showAndWait();
                if (result.isPresent()) {
                    QueryInfo qi = result.get();
                    String userInput = qi.getUserInput();
                    con.handleQueryEvent(userInput); //try-catch här för om inget resultat?
                    queryDialog.clearField();
                }
            }
        });
        
        Button viewAllAlbumsButton = new Button("View All Albums");
        viewAllAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //updateTextArea(cob.getBooks()); ?
            }
        });
        
        //Create FileChooser for 'Open File' and 'Save File' sub menus
        FileChooser fileChooser = new FileChooser();
        
        //Create menu option 'File' and sub menus 'Open File', 'Save File'
        //and 'Exit'
        Menu fileMenu = new Menu("File");
        
        MenuItem saveAlbumsItem = new MenuItem("Save Albums");
        saveAlbumsItem.setOnAction(new EventHandler<ActionEvent>() {
                
            @Override
            public void handle(ActionEvent event) {
                //Save? Ie write to db?
            }
        });
        
        MenuItem exitApplicationItem = new MenuItem("Exit");
        exitApplicationItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit?");
                alert.setTitle("Exit");
                alert.setHeaderText(null);
                
                Optional<ButtonType> result = alert.showAndWait();               
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //Save? Ie write to db?
                    primaryStage.close();
                }
            }
        });
        
        //Add sub menus to menu option 'File'
        fileMenu.getItems().addAll(saveAlbumsItem, exitApplicationItem);
        
        //Create menu bar and add menu option 'File'
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        
        //Set menu bar to top of BorderPane
        border.setTop(menuBar);
        
        //Create FlowPane to hold buttons at bottom and add buttons
        FlowPane bottomPane = new FlowPane();
        bottomPane.setHgap(20);
        bottomPane.getChildren().addAll(addAlbumButton, searchAlbumsButton, viewAllAlbumsButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPrefHeight(50);
        
        //Set FlowPane holding bottom buttons to bottom of BorderPane
        border.setBottom(bottomPane);
        
        //Create TableView
        mainTable = new TableView<>();
        ObservableList<Album> albums = FXCollections.observableArrayList(ac.getCurrentAlbums());       
        
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(
            new PropertyValueFactory<>("title")
        );
        
        TableColumn artistCol = new TableColumn<>("Artist");
        artistCol.setMinWidth(100);
        artistCol.setCellValueFactory(
            new PropertyValueFactory<>("artistAsString")
        );
        
        TableColumn releaseDateCol = new TableColumn("Release Date");
        releaseDateCol.setMinWidth(100);
        releaseDateCol.setCellValueFactory(
            new PropertyValueFactory<>("releaseDate")
        );
        
        TableColumn genreCol = new TableColumn("Genre");
        genreCol.setMinWidth(100);
        genreCol.setCellValueFactory(
            new PropertyValueFactory<>("genreAsString")
        );
        
        TableColumn lengthCol = new TableColumn("Length");
        lengthCol.setMinWidth(100);
        lengthCol.setCellValueFactory(
            new PropertyValueFactory<>("length")
        );
        
        TableColumn nrOfSongsCol = new TableColumn("Nr Of Songs");
        nrOfSongsCol.setMinWidth(100);
        nrOfSongsCol.setCellValueFactory(
            new PropertyValueFactory<>("numberOfSongs")
        );
        
        titleCol.prefWidthProperty().bind(mainTable.widthProperty().divide(4.02)); //divide(5.03) instead of 5 to avoid scrollbar at bottom
        titleCol.setStyle("-fx-alignment: CENTER;");
        artistCol.prefWidthProperty().bind(mainTable.widthProperty().divide(4.02));
        artistCol.setStyle("-fx-alignment: CENTER;");
        releaseDateCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        releaseDateCol.setStyle("-fx-alignment: CENTER;");
        genreCol.prefWidthProperty().bind(mainTable.widthProperty().divide(5.02));
        genreCol.setStyle("-fx-alignment: CENTER;");
        lengthCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        lengthCol.setStyle("-fx-alignment: CENTER;");
        nrOfSongsCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        nrOfSongsCol.setStyle("-fx-alignment: CENTER;");
        
        mainTable.setItems(albums);
        mainTable.getColumns().addAll(titleCol, artistCol, releaseDateCol, genreCol, lengthCol, nrOfSongsCol);       
        /*******************************************/
        
        //Set TextArea to center of BorderPane        
        //border.setCenter(centerText);
        border.setCenter(mainTable);
        
        //Add BorderPane to scene
        scene = new Scene(border, 1100, 600);
        
        updateTextArea(ac.getCurrentAlbums());
    }

    public void updateTextArea(ArrayList<Album> albumList) {
        if (albumList.isEmpty()) {
            Text emptyText = new Text("No albums currently in collection.");
            Font emptyFont = new Font("Courier", 16);
            emptyText.fontProperty().setValue(emptyFont);
            
            double widthOfText = emptyText.getLayoutBounds().getWidth();
            double heightOfText = emptyText.getLayoutBounds().getHeight();
        
            emptyText.setLayoutX((800 - widthOfText)/2.0);
            emptyText.setLayoutY((500 - heightOfText)/2.0);
            
            border.setCenter(emptyText);
        } else {
            getMainTable().setItems(FXCollections.observableArrayList(albumList));
            border.setCenter(mainTable);
        }
    }

    /**
     * @return the primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @return the mainTable
     */
    public TableView<Album> getMainTable() {
        return mainTable;
    }

    /**
     * @return the border
     */
    public BorderPane getBorder() {
        return border;
    }
    
}

