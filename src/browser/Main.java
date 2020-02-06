package browser;

import bookmark.Bookmark;
import bookmark.BookmarkList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.beans.value.*;
import javafx.concurrent.*;
import javafx.concurrent.Worker.*;

public class Main extends Application {
    final int width = 1000, height = 800;

    TextField addressBar;
    ChoiceBox<String> bookmarkDropdown;
    WebView viewer;

    //Creates a bookmark list populated with bookmarks
    BookmarkList bookmarks = new BookmarkList();

    //Hardcoded bookmark array
    //String[] bookmarks = {"UNB", "Google", "Bing"};
    String address;

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

        viewer = new WebView();
        viewer.setMinSize(1000, 750); // width then height
        viewer.getEngine().load("http://www.unb.ca"); // the same code can be used later to change the page viewed
        viewer.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    String location = viewer.getEngine().getLocation();
                    addressBar.setText(location);
                }
            }
        } );

        FlowPane pane = new FlowPane(addressBar, bookmarkDropdown, viewer);
        Scene scene = new Scene(pane, width, height);
        primaryStage.setTitle("Simple Web Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processAddressBar(ActionEvent event)
    {
        address = addressBar.getText();
        viewer.getEngine().load(address);
        //System.out.println(address);
    }

    public void processBookmarkDropdown(ActionEvent e){

       /* Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "You are about to visit " + bookmarkDropdown.getValue() ,
                ButtonType.OK);
        alert.showAndWait();*/

       //Returns bookmark object based on chosen ID in dropdown
        Bookmark choice = bookmarks.searchID(bookmarkDropdown.getValue());

        address = choice.getWebAddress();
        viewer.getEngine().load(address);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
