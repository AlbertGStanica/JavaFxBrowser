package sample;

import javafx.application.Application;
import javafx.scene.Scene;
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

    String[] bookmarks = {"UNB", "Google", "Bing"};

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font mainFont = new Font("courrier", 24);

        addressBar = new TextField();
        addressBar.setFont(mainFont);
        addressBar.setMinWidth(width-200);

        bookmarkDropdown = new ChoiceBox<>();
        bookmarkDropdown.setStyle("-fx-font: 24px \"Courrier\";");
        bookmarkDropdown.getItems().addAll(bookmarks);
        bookmarkDropdown.getSelectionModel().select(0);

        viewer = new WebView();
        viewer.setMinSize(1000, 750); // width then height
        viewer.getEngine().load("http://www.unb.ca"); // the same code can be used later to change the page viewed
        viewer.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    String location = viewer.getEngine().getLocation();
                    // add the line of code here, to put this location String in your TextField
                }
            }
        } );

        FlowPane pane = new FlowPane(addressBar, bookmarkDropdown);
        Scene scene = new Scene(pane, width, height);
        primaryStage.setTitle("Simple Web Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
