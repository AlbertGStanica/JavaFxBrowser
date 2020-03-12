package browser;

import bookmark.Bookmark;
import bookmark.BookmarkList;
import bookmark.EqualIDsException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import java.io.*;
import java.net.URL;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {
    final int width = 1000, height = 800;

    //Creating the JavaFx objects to populate stage
    TextField addressBar;
    ChoiceBox<String> bookmarkDropdown;
    WebView viewer;
    Button setBookmark;

    //Creates an empty bookmark list object
    BookmarkList bookmarks;

    String address;
    URL webAddr;

    Image image;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font mainFont = new Font("courrier", 24);

        //Load bookmarks from bookmarks.dat.
        //Bookmarks from previous sessions are stored in this file.
        //If bookmarks.dat does not exist, test bookmarks are populated.
        loadBookmarks();

        //Create a textField object that functions as the address bar.
        addressBar = new TextField();
        addressBar.setFont(mainFont);
        addressBar.setMinWidth(width-200);
        addressBar.setOnAction(this::processAddressBar);

        //Create a ChoiceBox objcet that serves as drop-down list
        //populated with bookmarks.
        bookmarkDropdown = new ChoiceBox<>();
        bookmarkDropdown.setStyle("-fx-font: 24px \"Courrier\";");
        //Populates dropdown with ID's of bookmarks in bookmark list
        bookmarkDropdown.getItems().addAll(bookmarks.getIDArray());
        bookmarkDropdown.getSelectionModel().select(0);
        bookmarkDropdown.setOnAction(this:: processBookmarkDropdown);

        //Creates a button object that when activated allows the user to
        //create a new bookmark.
        setBookmark = new Button();
        setBookmark.setOnAction(this::processBookmarkButton);
        //Set the image for the button
        image = new Image(getClass().getResourceAsStream("bookmark.png"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        setBookmark.setGraphic(imageView);
        setBookmark.maxWidth(image.getWidth());
        setBookmark.maxHeight(image.getHeight());

        //Creates a webview object which serves to display web content via url
        //entered in address bar. Default home page is set to http://google.ca.
        viewer = new WebView();
        viewer.setMinSize(1000, 750); // width then height
        viewer.getEngine().load("http://google.ca"); // the same code can be used later to change the page viewed
        viewer.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    String location = viewer.getEngine().getLocation();
                    addressBar.setText(location);
                }
            }
        } );

        //Handles what happens when user closes out of browser window.
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                saveBookmarks();
            }
        });

        //Creates main JavaFx pane and adds to primary stage
        FlowPane pane = new FlowPane(addressBar, bookmarkDropdown,setBookmark ,viewer);
        Scene scene = new Scene(pane, width, height);
        primaryStage.setTitle("Simple Web Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method that handles text input in address bar.
    public void processAddressBar(ActionEvent event)
    {
        String tempAddress = addressBar.getText();

        try{
            //Attempts to open the entered address in the web view
            webAddr = new URL(tempAddress);
            webAddr.openStream().close();

            address = tempAddress;
        }
        catch (IOException e)
        {
            //Popup notifying user that url is invalid.
            //Make address the previous web address.
            Alert alert = new Alert(AlertType.INFORMATION,
                    "Invalid URL" , ButtonType.OK);
            alert.showAndWait();

            e.printStackTrace();
        }

        viewer.getEngine().load(address);
    }

    //Method that processes the add bookmark button
    public void processBookmarkButton(ActionEvent event){
        String id;

        //Sets image for bookmark popup
        image = new Image(getClass().getResourceAsStream("kappa.png"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add a bookmark");
        dialog.setHeaderText("Enter bookmark ID");
        dialog.setGraphic(imageView);

        Optional<String> result = dialog.showAndWait();
        if (!result.isEmpty())
        {
            String input = dialog.getEditor().getText();
            id = input;
            if (!input.equals(""))
            {
                //Adds the entered ID along with current web address into bookmark list.
                //If the entered ID already exists in the bookmark list, throw an exception.
                try{
                    bookmarks.addBookmark(new Bookmark(id, addressBar.getText()));
                    bookmarkDropdown.getItems().add(id);
                }
                catch (EqualIDsException e)
                {
                    Alert alert = new Alert(AlertType.INFORMATION,
                            "Cannot add a bookmark with ID that already exists.",
                            ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }

    }

    //Method for process bookmark choice box object.
    public void processBookmarkDropdown(ActionEvent e)
    {
        int index = bookmarkDropdown.getSelectionModel().getSelectedIndex();

        //Displays web address of chosen ID
        address = bookmarks.getBookmark(index).getWebAddress();
        viewer.getEngine().load(address);
    }

    //Method to save the current bookmarks to bookmarks.dat file on close
    public void saveBookmarks()
    {
        FileOutputStream file = null;
        ObjectOutputStream outfile = null;

        try
        {
            file = new FileOutputStream("bookmarks.dat");
            outfile = new ObjectOutputStream(file);

            for (int i = 0; i < bookmarks.size(); i++)
            {
                outfile.writeObject(bookmarks.getBookmark(i));
            }
        }
        catch (IOException e)
        {
            System.out.println("Problem saving to file");
        }
        finally
        {
            try
            {
                if (outfile != null)
                {
                    outfile.close();
                }
            }
            catch (IOException e)
            {
                System.out.println("Problem closing the file");
            }
        }
    }

    //Method to load saved bookmarks from bookmarks.dat.\
    //If bookmarks.dat does not exist, populate bookmark list with test values.
    public void loadBookmarks()
    {
        bookmarks = new BookmarkList();
        FileInputStream file = null;
        ObjectInputStream infile = null;
        boolean problem = false;

        try {
            file = new FileInputStream("bookmarks.dat");
            infile = new ObjectInputStream(file);
            while(true){
                Bookmark book = (Bookmark) infile.readObject();
                bookmarks.addBookmark(book);
            }
        }
        catch (IOException e)
        {
            problem = true;
        }
        catch(ClassNotFoundException e)
        {
            problem = true;
        }
        catch (EqualIDsException e)
        {
            problem = true;
        }
        finally {
            try{
                if (infile != null)
                    infile.close();
            }
            catch (IOException e){
                System.out.println("Problem closing the file");
            }

            //If an error occurred, simply populate bookmark list with test values.
            if (problem) bookmarks.createTestList();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
