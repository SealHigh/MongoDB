
package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.ReadOnlyStringWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

import javax.swing.*;


public class View {

    private AlbumCollection ac;
    private QueryDialog queryDialog;
    private AddDialog addDialog;
    private Stage primaryStage;
    private Scene scene;
    private TableView<Album> mainTable;
    private TableView<Review> reviewTable;
    private BorderPane border;
    private Scene singleAlbumScene;
    
    public View(AlbumCollection ac, Stage primaryStage) throws IOException, ClassNotFoundException {
        
        this.ac = ac;
        this.primaryStage = primaryStage;
        Controller con = new Controller(ac, this);
        queryDialog = new QueryDialog();
        addDialog = new AddDialog();
        //sbd = new SearchBooksDialog(primaryStage);
        
        border = new BorderPane();
        
        initReviewTableView();
        
        //Create buttons 'Add Album', 'Search Albums' and 'View All' at bottom
        Button addAlbumButton = new Button("Add Album");
        addAlbumButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Optional<AddInfo> result
                        = addDialog.showAndWait();
                if (result.isPresent()) {
                    AddInfo info = result.get();
                    try {
                        con.handleAddAlbumEvent(info.getTitle(), info.getArtists(),
                                info.getReleaseDate(), info.getNrOfSongs(), info.getLength(), info.getGenres());
                    }
                    catch (NumberFormatException e){
                        //Error dialog here "Fill all fields"
                    }
                    addDialog.clearFields();
                }

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
                    SearchOptions searchOption = qi.getSearchOption();
                    con.handleQueryEvent(searchOption, userInput); //try-catch här för om inget resultat?
                    queryDialog.clearFields();
                }
            }
        });
        
        Button viewAllAlbumsButton = new Button("View All Albums");
        viewAllAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                con.handleGetAllAlbumsEvent();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Album slectedAlbum = mainTable.getSelectionModel().getSelectedItem();
                con.handleDeleteAlbumEvent(slectedAlbum);
            }
        });

        Button rateButton = new Button("Rate");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Album slectedAlbum = mainTable.getSelectionModel().getSelectedItem();
                con.handleDeleteAlbumEvent(slectedAlbum);
            }
        });
               
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
        bottomPane.getChildren().addAll(addAlbumButton, searchAlbumsButton, viewAllAlbumsButton,rateButton, deleteButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPrefHeight(50);
        
        //Set FlowPane holding bottom buttons to bottom of BorderPane
        border.setBottom(bottomPane);
        
        //Create TableView
        mainTable = new TableView<>();
        ObservableList<Album> albums = FXCollections.observableArrayList(ac.getAllRecords());
        
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

        TableColumn ratingCol = new TableColumn("Rating");
        ratingCol.setMinWidth(100);
        ratingCol.setCellValueFactory(
                new PropertyValueFactory<>("rating")
        );
        
        TableColumn<Album, String > nrOfReviewsCol = new TableColumn<>("Nr Of Reviews");
        nrOfReviewsCol.setMinWidth(100);
        nrOfReviewsCol.setCellValueFactory(cellData -> {
            ArrayList<Review> reviews = cellData.getValue().getReviews();
            String nrOfReviewsString = Integer.toString(reviews.size());

            return new ReadOnlyStringWrapper(nrOfReviewsString);
        });
        
        titleCol.prefWidthProperty().bind(mainTable.widthProperty().divide(6.02)); //divide(5.03) instead of 5 to avoid scrollbar at bottom
        titleCol.setStyle("-fx-alignment: CENTER;");
        artistCol.prefWidthProperty().bind(mainTable.widthProperty().divide(6.02));
        artistCol.setStyle("-fx-alignment: CENTER;");
        releaseDateCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        releaseDateCol.setStyle("-fx-alignment: CENTER;");
        genreCol.prefWidthProperty().bind(mainTable.widthProperty().divide(6.02));
        genreCol.setStyle("-fx-alignment: CENTER;");
        lengthCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        lengthCol.setStyle("-fx-alignment: CENTER;");
        nrOfSongsCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        nrOfSongsCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        ratingCol.setStyle("-fx-alignment: CENTER;");
        nrOfReviewsCol.prefWidthProperty().bind(mainTable.widthProperty().divide(10.03));
        nrOfReviewsCol.setStyle("-fx-alignment: CENTER;");
        
        //Makes rows clickable
        mainTable.setRowFactory( tv -> {
            TableRow<Album> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Album rowData = row.getItem();
                    if(rowData.getReviews() != null) {
                        ArrayList<Review> reviewData = rowData.getReviews();
                        reviewTable.setItems(FXCollections.observableArrayList(reviewData));
                        border.setCenter(reviewTable);
                    }
                }
            });
            return row ;
        });
        
        mainTable.setItems(albums);
        mainTable.getColumns().addAll(titleCol, artistCol, releaseDateCol, 
                genreCol, lengthCol, nrOfSongsCol, nrOfReviewsCol, ratingCol);
        
        //Set TextArea to center of BorderPane        
        //border.setCenter(centerText);
        border.setCenter(mainTable);
        
        //Add BorderPane to scene
        scene = new Scene(border, 1400, 600);

        updateTextArea(ac.getAllRecords()); //den gör inget - den laddar alla 
                                            //album i db vid start och annars
                                            //byter den border till att text där
                                            //det står att den är tom
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
    
    public void initReviewTableView() {

        //Create TableView
        reviewTable = new TableView<>();
        ArrayList<Review> emptyList = new ArrayList<>();
        ObservableList<Review> reviews = FXCollections.observableArrayList(emptyList);
        
        TableColumn<Review, String > userCol = new TableColumn<>("User");
        userCol.setMinWidth(100);
        userCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            String userAsString = user.getUserName();

            return new ReadOnlyStringWrapper(userAsString);
        });
        
        TableColumn ratingCol = new TableColumn("Rating");
        ratingCol.setMinWidth(100);
        ratingCol.setCellValueFactory(
            new PropertyValueFactory<>("rating")
        );       
        
        TableColumn commentCol = new TableColumn("Comment");
        commentCol.setMinWidth(100);
        commentCol.setCellValueFactory(
            new PropertyValueFactory<>("comment")
        );
        
        //3.03 needs to be changed 
        userCol.prefWidthProperty().bind(reviewTable.widthProperty().divide(3.03)); //divide(5.03) instead of 5 to avoid scrollbar at bottom
        userCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.prefWidthProperty().bind(reviewTable.widthProperty().divide(3.03));
        ratingCol.setStyle("-fx-alignment: CENTER;");
        commentCol.prefWidthProperty().bind(reviewTable.widthProperty().divide(3.03));
        commentCol.setStyle("-fx-alignment: CENTER;");
        
        reviewTable.setOnMouseClicked(event -> {
                border.setCenter(mainTable);
            });
        
        reviewTable.setItems(reviews);
        reviewTable.getColumns().addAll(userCol, ratingCol, commentCol);
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

