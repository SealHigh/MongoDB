
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

public class View {

    private AlbumCollection ac;
    private LogInDialog logInDialog;
    private AddDialog addDialog;
    private QueryDialog queryDialog;     
    private AddReviewDialog addReviewDialog;
    private AddMovieDialog addMovieDialog;
    private QueryMovieDialog queryMovieDialog;
    private AddMovieReviewDialog addMovieReviewDialog;
    private boolean usingAlbums;
    private Stage primaryStage;
    private Scene scene;
    private TableView<Album> mainTable;
    private TableView<Review> reviewTable;
    private TableView<Movie> movieTable;
    private TableView<Review> reviewMovieTable;
    private BorderPane border;
    private FlowPane bottomPane;
    private FlowPane loggedInPane;
    private MenuBar menuBar;
    private MenuBar lImenuBar;
    
    public View(AlbumCollection ac, Stage primaryStage) throws IOException, ClassNotFoundException {
        
        this.ac = ac;
        this.primaryStage = primaryStage;
        Controller con = new Controller(ac, this);
        queryDialog = new QueryDialog();
        addDialog = new AddDialog();
        logInDialog = new LogInDialog();
        addReviewDialog = new AddReviewDialog();
        addMovieDialog = new AddMovieDialog();
        queryMovieDialog = new QueryMovieDialog();
        addMovieReviewDialog = new AddMovieReviewDialog();
        usingAlbums = true;
        
        border = new BorderPane();
        
        initReviewTableView();
        initMovieTableView();
        initMovieReviewTableView();
        initLoggedInPaneAndMenu(con);

        
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
                    try {
                        con.handleQueryEvent(searchOption, userInput); //try-catch här för om inget resultat?
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Could not search albums!");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                    queryDialog.clearFields();
                }
            }
        });
        
        Button viewAllAlbumsButton = new Button("View All Albums");
        viewAllAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{
                    con.handleGetAllAlbumsEvent();
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR, "Could not load albums!");
                    alert.setTitle("");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
            }
        });

        
        Button switchViewButton = new Button("Switch Albums/Movies");
        switchViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (border.getCenter() != mainTable) {
                    usingAlbums = true;
                    border.setCenter(mainTable);
                } else {
                    usingAlbums = false;
                    border.setCenter(movieTable);
                }
            }
        });

        //Create menu option 'File' and sub menus 'Open File', 'Save File'
        //and 'Exit'
        Menu fileMenu = new Menu("File");
        
        MenuItem logInItem = new MenuItem("Log in");
        logInItem.setOnAction(new EventHandler<ActionEvent>() {
                
            @Override
            public void handle(ActionEvent event) {
                Optional<LogInInfo> result
                        = logInDialog.showAndWait();
                if (result.isPresent()) {
                    LogInInfo li = result.get();
                    String userName = li.getUserName();
                    String password = li.getPassword();
                    if(con.handleLogInEvent(userName, password)) {
                        usingAlbums = true;
                        border.setCenter(mainTable);
                        border.setBottom(loggedInPane);
                        border.setTop(lImenuBar);
                    }
                    else {
                        Alert alert = new Alert(AlertType.INFORMATION, "Login failed! Username or password invalid.");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                    queryDialog.clearFields();
                }
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
        fileMenu.getItems().addAll(logInItem, exitApplicationItem);
        
        //Create menu bar and add menu option 'File'
        menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        
        //Set menu bar to top of BorderPane
        border.setTop(menuBar);
        
        //Create FlowPane to hold buttons at bottom and add buttons
        bottomPane = new FlowPane();
        bottomPane.setHgap(20);
        bottomPane.getChildren().addAll(searchAlbumsButton, viewAllAlbumsButton, switchViewButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPrefHeight(50);
        
        //Set FlowPane holding bottom buttons to bottom of BorderPane
        border.setBottom(bottomPane);
        
        //Create TableView
        mainTable = new TableView<>();
        ArrayList<Album> arrayListAlbums;
        try {
            arrayListAlbums = ac.getAlbums();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Could not load albums!");
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.showAndWait();
                arrayListAlbums = new ArrayList<>();
        }
        ObservableList<Album> albums = FXCollections.observableArrayList(arrayListAlbums);
        
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
        nrOfReviewsCol.setCellValueFactory(
                new PropertyValueFactory<>("reviews")
        );
        
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
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Album rowData = row.getItem();
                    if(Integer.parseInt(rowData.getReviews()) != 0) {
                        ArrayList<Review> reviewData = ac.getReviews(rowData.getAlbumID(), "album");
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
        try {
            updateTextArea(ac.getAlbums());
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Could not load albums!");
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.showAndWait();
        }        
    }

    public <T> void updateTextArea(ArrayList<T> list) {
        if(usingAlbums) {
            
            if (list.isEmpty()) {
                Text emptyText = new Text("No albums currently in collection.");
                Font emptyFont = new Font("Courier", 16);
                emptyText.fontProperty().setValue(emptyFont);
            
                double widthOfText = emptyText.getLayoutBounds().getWidth();
                double heightOfText = emptyText.getLayoutBounds().getHeight();
        
                emptyText.setLayoutX((800 - widthOfText)/2.0);
                emptyText.setLayoutY((500 - heightOfText)/2.0);
            
                border.setCenter(emptyText);
            } else {
                getMainTable().setItems(FXCollections.observableArrayList((ArrayList<Album>) list));
                border.setCenter(mainTable);
            }
        } else {
            if (list.isEmpty()) {
                Text emptyText = new Text("No movies currently in collection.");
                Font emptyFont = new Font("Courier", 16);
                emptyText.fontProperty().setValue(emptyFont);
            
                double widthOfText = emptyText.getLayoutBounds().getWidth();
                double heightOfText = emptyText.getLayoutBounds().getHeight();
        
                emptyText.setLayoutX((800 - widthOfText)/2.0);
                emptyText.setLayoutY((500 - heightOfText)/2.0);
            
                border.setCenter(emptyText);
            } else {
                getMovieTable().setItems(FXCollections.observableArrayList((ArrayList<Movie>) list));
                border.setCenter(movieTable);
            }
        }
    }
    
    public void initReviewTableView() {

        //Create TableView
        reviewTable = new TableView<>();
        ArrayList<Review> emptyList = new ArrayList<>();


        ObservableList<Review> reviews = FXCollections.observableArrayList(emptyList);
        
        TableColumn<Review, String > userCol = new TableColumn<>("User");
        userCol.setMinWidth(100);
        userCol.setCellValueFactory(
            new PropertyValueFactory<>("user")
        );
        
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
    
    public void initMovieReviewTableView() {

        //Create TableView
        reviewMovieTable = new TableView<>();
        ArrayList<Review> emptyMovieList = new ArrayList<>();


        ObservableList<Review> movieReviews = FXCollections.observableArrayList(emptyMovieList);
        
        TableColumn<Review, String > userMovieCol = new TableColumn<>("User");
        userMovieCol.setMinWidth(100);
        userMovieCol.setCellValueFactory(
            new PropertyValueFactory<>("user")
        );
        
        TableColumn ratingMovieCol = new TableColumn("Rating");
        ratingMovieCol.setMinWidth(100);
        ratingMovieCol.setCellValueFactory(
            new PropertyValueFactory<>("rating")
        );       
        
        TableColumn commentMovieCol = new TableColumn("Comment");
        commentMovieCol.setMinWidth(100);
        commentMovieCol.setCellValueFactory(
            new PropertyValueFactory<>("comment")
        );
        
        //3.03 needs to be changed 
        userMovieCol.prefWidthProperty().bind(reviewMovieTable.widthProperty().divide(3.03)); //divide(5.03) instead of 5 to avoid scrollbar at bottom
        userMovieCol.setStyle("-fx-alignment: CENTER;");
        ratingMovieCol.prefWidthProperty().bind(reviewMovieTable.widthProperty().divide(3.03));
        ratingMovieCol.setStyle("-fx-alignment: CENTER;");
        commentMovieCol.prefWidthProperty().bind(reviewMovieTable.widthProperty().divide(3.03));
        commentMovieCol.setStyle("-fx-alignment: CENTER;");
        
        reviewMovieTable.setOnMouseClicked(event -> {
                border.setCenter(movieTable);
            });
        
        reviewMovieTable.setItems(movieReviews);
        reviewMovieTable.getColumns().addAll(userMovieCol, ratingMovieCol, commentMovieCol);
    }
    
    public void initLoggedInPaneAndMenu(Controller con) {
        //Create buttons 'Add Album', 'Search Albums' and 'View All' at bottom
        Button lIaddAlbumButton = new Button("Add Album");
        lIaddAlbumButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (usingAlbums) {
                        Optional<AddInfo> result = addDialog.showAndWait();
                        if (result.isPresent()) {
                            AddInfo info = result.get();
                        
                            try {
                            con.handleAddAlbumEvent(info.getTitle(), info.getArtists(),
                                    info.getReleaseDate(), info.getNrOfSongs(), info.getLength(), info.getGenres());
                            } catch (Exception e) {
                                Alert alert = new Alert(AlertType.ERROR, "Could not add album!");
                                alert.setTitle("");
                                alert.setHeaderText(null);
                                alert.showAndWait();
                            }

                            addDialog.clearFields();
                        }
                } else {
                        Optional<AddMovieInfo> result = addMovieDialog.showAndWait();
                        if (result.isPresent()) {
                            AddMovieInfo info = result.get();
                        
                            try {
                                con.handleAddMovieEvent(info.getTitle(), info.getDirector(),
                                info.getReleaseYear(), info.getLength(), info.getGenres());
                            } catch (Exception e) {
                                Alert alert = new Alert(AlertType.ERROR, "Could not add movie!");
                                alert.setTitle("");
                                alert.setHeaderText(null);
                                alert.showAndWait();
                            }

                            addMovieDialog.clearFields();
                        }
                }        
            }
        });
        
        Button lIsearchAlbumsButton = new Button("Search Albums");
        lIsearchAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {
                
            @Override
            public void handle(ActionEvent event) {
                if (usingAlbums) {
                    Optional<QueryInfo> result = queryDialog.showAndWait();
                    
                    if (result.isPresent()) {
                        QueryInfo qi = result.get();
                        String userInput = qi.getUserInput();
                        SearchOptions searchOption = qi.getSearchOption();
                    
                        try {
                            con.handleQueryEvent(searchOption, userInput); //try-catch här för om inget resultat?
                        } catch (Exception e) {
                            Alert alert = new Alert(AlertType.INFORMATION, "No results found");
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    
                        queryDialog.clearFields();
                    }
                } else {
                    Optional<QueryMovieInfo> result = queryMovieDialog.showAndWait();
                    
                    if (result.isPresent()) {
                        QueryMovieInfo qi = result.get();
                        String userInput = qi.getUserInput();
                        SearchMovieOptions searchMovieOption = qi.getSearchOption();
                    
                        try {
                            con.handleQueryMovieEvent(searchMovieOption, userInput); //try-catch här för om inget resultat?
                        } catch (Exception e) {
                            Alert alert = new Alert(AlertType.INFORMATION, "No results found");
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    
                        queryMovieDialog.clearFields();
                    }
                }
            }
        });
        
        Button lIviewAllAlbumsButton = new Button("View All Albums");
        lIviewAllAlbumsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (usingAlbums) {
                    try {
                        con.handleGetAllAlbumsEvent();
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Could not load albums!");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                } else {
                    try {
                        con.handleGetAllMoviesEvent();
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Could not load movies!");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                }
            }
        });

        Button lIdeleteButton = new Button("Delete");
        lIdeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                if(usingAlbums) {
                    Album selectedAlbum = mainTable.getSelectionModel().getSelectedItem();
                    try {
                        con.handleDeleteAlbumEvent(selectedAlbum);
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Could not delete album!");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                } else {
                    Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
                    try {
                        con.handleDeleteMovieEvent(selectedMovie);
                    } catch (Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Could not delete movie!");
                        alert.setTitle("");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                }
            }
        });

        Button lIrateButton = new Button("Rate");
        lIrateButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                if(usingAlbums) {
                    Album slectedAlbum = mainTable.getSelectionModel().getSelectedItem();
                    Optional<ReviewInfo> result = addReviewDialog.showAndWait();
                    
                    if (result.isPresent()) {
                        ReviewInfo ri = result.get();
                        String rating = ri.getRating();
                        String comment = ri.getComment();
                        addReviewDialog.clearFields();
                        try {
                            con.handleReviewAlbumEvent(slectedAlbum.getAlbumID(), Integer.parseInt(rating), comment);
                        } catch (Exception e) {
                            Alert alert = new Alert(AlertType.ERROR, "Could not add review!");
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    }
                } else {
                    Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
                    Optional<ReviewMovieInfo> result = addMovieReviewDialog.showAndWait();
                    
                    if (result.isPresent()) {
                        ReviewMovieInfo ri = result.get();
                        String rating = ri.getRating();
                        String comment = ri.getComment();
                        addMovieReviewDialog.clearFields();
                        try {
                            con.handleReviewMovieEvent(selectedMovie.getMovieID(), Integer.parseInt(rating), comment);
                        } catch (Exception e) {
                            Alert alert = new Alert(AlertType.ERROR, "Could not add review!");
                            alert.setTitle("");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    }
                }
            }
        });
        
        Button lIswitchViewButton = new Button("Switch Albums/Movies");
        lIswitchViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (border.getCenter() != mainTable) {
                    usingAlbums = true;
                    border.setCenter(mainTable);
                } else {
                    usingAlbums = false;
                    border.setCenter(movieTable);
                }
            }
        });
        
        //Create FlowPane loggedInPane
        loggedInPane = new FlowPane();
        loggedInPane.setHgap(20);
        loggedInPane.getChildren().addAll(lIaddAlbumButton, lIsearchAlbumsButton, lIviewAllAlbumsButton, lIrateButton, lIdeleteButton, lIswitchViewButton);
        loggedInPane.setAlignment(Pos.CENTER);
        loggedInPane.setPrefHeight(50);
        
        //Create menu option 'File' and sub menus 'Open File', 'Save File'
        //and 'Exit'
       Menu lIfileMenu = new Menu("File");
        
        MenuItem lIlogOutItem = new MenuItem("Log out");
        lIlogOutItem.setOnAction(new EventHandler<ActionEvent>() {
                
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to log out?");
                alert.setTitle("Log out");
                alert.setHeaderText(null);
                
                Optional<ButtonType> result = alert.showAndWait();               
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    con.handleLogOutEvent();
                    border.setBottom(bottomPane);
                    border.setTop(menuBar);
                }
            }
        });
        
        MenuItem lIexitApplicationItem = new MenuItem("Exit");
        lIexitApplicationItem.setOnAction(new EventHandler<ActionEvent>() {

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
        lIfileMenu.getItems().addAll(lIlogOutItem, lIexitApplicationItem);
        
        //Create menu bar and add menu option 'File'
        lImenuBar = new MenuBar();
        lImenuBar.getMenus().add(lIfileMenu);
    }
    
    
    public void initMovieTableView() {
        //Create TableView
        movieTable = new TableView<>();
        ObservableList<Movie> movies = FXCollections.observableArrayList(ac.getMovies());
        
        TableColumn movieTitleCol = new TableColumn("Title");
        movieTitleCol.setMinWidth(100);
        movieTitleCol.setCellValueFactory(
            new PropertyValueFactory<>("title")
        );
        
        TableColumn releaseYearCol = new TableColumn<>("Released");
        releaseYearCol.setMinWidth(100);
        releaseYearCol.setCellValueFactory(
            new PropertyValueFactory<>("releaseYear")
        );
        
        TableColumn directorCol = new TableColumn("Director");
        directorCol.setMinWidth(100);
        directorCol.setCellValueFactory(
            new PropertyValueFactory<>("directorAsString")
        );
        
        TableColumn movieGenreCol = new TableColumn("Genre");
        movieGenreCol.setMinWidth(100);
        movieGenreCol.setCellValueFactory(
            new PropertyValueFactory<>("genreAsString")
        );
        
        TableColumn movieLengthCol = new TableColumn("Length");
        movieLengthCol.setMinWidth(100);
        movieLengthCol.setCellValueFactory(
            new PropertyValueFactory<>("length")
        );

        TableColumn movieRatingCol = new TableColumn("Rating");
        movieRatingCol.setMinWidth(100);
        movieRatingCol.setCellValueFactory(
                new PropertyValueFactory<>("rating")
        );
        
        TableColumn<Movie, String > movieNrOfReviewsCol = new TableColumn<>("Nr Of Reviews");
        movieNrOfReviewsCol.setMinWidth(100);
        movieNrOfReviewsCol.setCellValueFactory(
                new PropertyValueFactory<>("reviews")
        );
        
        movieTitleCol.prefWidthProperty().bind(movieTable.widthProperty().divide(4.72)); //divide(5.03) instead of 5 to avoid scrollbar at bottom
        movieTitleCol.setStyle("-fx-alignment: CENTER;");
        releaseYearCol.prefWidthProperty().bind(movieTable.widthProperty().divide(10.02));
        releaseYearCol.setStyle("-fx-alignment: CENTER;");
        directorCol.prefWidthProperty().bind(movieTable.widthProperty().divide(4.72));
        directorCol.setStyle("-fx-alignment: CENTER;");
        movieGenreCol.prefWidthProperty().bind(movieTable.widthProperty().divide(6.02));
        movieGenreCol.setStyle("-fx-alignment: CENTER;");
        movieLengthCol.prefWidthProperty().bind(movieTable.widthProperty().divide(10.03));
        movieLengthCol.setStyle("-fx-alignment: CENTER;");
        movieRatingCol.prefWidthProperty().bind(movieTable.widthProperty().divide(10.03));
        movieRatingCol.setStyle("-fx-alignment: CENTER;");
        movieNrOfReviewsCol.prefWidthProperty().bind(movieTable.widthProperty().divide(10.03));
        movieNrOfReviewsCol.setStyle("-fx-alignment: CENTER;");
        
        //Makes rows clickable
        movieTable.setRowFactory( tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Movie rowData = row.getItem();
                    if(rowData.getReviews() != null) {
                        ArrayList<Review> reviewData = ac.getReviews(rowData.getMovieID(), "movie");
                        reviewMovieTable.setItems(FXCollections.observableArrayList(reviewData));
                        border.setCenter(reviewMovieTable);
                    }
                }
            });
            return row;
        });
        
        movieTable.setItems(movies);
        movieTable.getColumns().addAll(movieTitleCol, releaseYearCol, directorCol, 
                movieGenreCol, movieLengthCol, movieRatingCol, movieNrOfReviewsCol);
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
    
    public TableView<Movie> getMovieTable() {
        return movieTable;
    }

    /**
     * @return the border
     */
    public BorderPane getBorder() {
        return border;
    }
    
}

