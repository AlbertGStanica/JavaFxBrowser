package browser;

import bookmark.Bookmark;
import bookmark.BookmarkList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.beans.value.*;
import javafx.concurrent.*;
import javafx.concurrent.Worker.*;

import java.util.Optional;

public class Main extends Application {
    final int width = 1000, height = 800;

    TextField addressBar;
    ChoiceBox<String> bookmarkDropdown;
    WebView viewer;

    //Creates a bookmark list populated with bookmarks
    BookmarkList bookmarks = new BookmarkList();

    //Button for adding bookmark
    Button setBookmark;

    //Hardcoded bookmark array
    //String[] bookmarks = {"UNB", "Google", "Bing"};
    String address;

    Image image = new Image(getClass().getResourceAsStream("bookmark.png"));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font mainFont = new Font("courrier", 24);

        addressBar = new TextField();
        addressBar.setFont(mainFont);
        addressBar.setMinWidth(width-200);
        addressBar.setOnAction(this::processAddressBar);

        bookmarkDropdown = new ChoiceBox<>();
        bookmarkDropdown.setStyle("-fx-font: 24px \"Courrier\";");
        bookmarkDropdown.getItems().addAll(bookmarks.getIDArray());
        bookmarkDropdown.getSelectionModel().select(0);
        bookmarkDropdown.setOnAction(this:: processBookmarkDropdown);

        //Button
        setBookmark = new Button();
        setBookmark.setOnAction(this::processBookmarkButton);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        imageView.setFitWidth(40);
        setBookmark.setGraphic(imageView);
        setBookmark.maxWidth(image.getWidth());
        setBookmark.maxHeight(image.getHeight());


        viewer = new WebView();
        viewer.setMinSize(1000, 750); // width then height
        viewer.getEngine().load(bookmarks.getBookmark(0).getWebAddress()); // the same code can be used later to change the page viewed
        viewer.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    String location = viewer.getEngine().getLocation();
                    addressBar.setText(location);
                }
            }
        } );

        FlowPane pane = new FlowPane(addressBar, bookmarkDropdown,setBookmark ,viewer);
        Scene scene = new Scene(pane, width, height);
        primaryStage.setTitle("Simple Web Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processAddressBar(ActionEvent event)
    {
        address = addressBar.getText();
        viewer.getEngine().load(address);
    }

    public void processBookmarkButton(ActionEvent event){
        String id = "";

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add a bookmark");
        dialog.setHeaderText("Enter bookmark ID");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            String input = dialog.getEditor().getText();
            id = input;
        }

        bookmarks.addBookmark(new Bookmark(id, addressBar.getText()));
        bookmarkDropdown.getItems().add(id);
    }

    public void processBookmarkDropdown(ActionEvent e){
       //Returns bookmark object based on chosen ID in dropdown
        Bookmark choice = bookmarks.searchID(bookmarkDropdown.getValue());

        address = choice.getWebAddress();
        viewer.getEngine().load(address);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
