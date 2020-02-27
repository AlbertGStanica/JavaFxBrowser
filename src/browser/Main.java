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

    URL webAddr;

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
        loadBookmarks();

        addressBar = new TextField();
        addressBar.setFont(mainFont);
        addressBar.setMinWidth(width-200);
        addressBar.setOnAction(this::processAddressBar);

        bookmarkDropdown = new ChoiceBox<>();
        bookmarkDropdown.setStyle("-fx-font: 24px \"Courrier\";");
        //Populates dropdown with ID's of bookmarks in bookmark list
        bookmarkDropdown.getItems().addAll(bookmarks.getIDArray());
        bookmarkDropdown.getSelectionModel().select(0);
        bookmarkDropdown.setOnAction(this:: processBookmarkDropdown);

        //Button
        setBookmark = new Button();
        setBookmark.setOnAction(this::processBookmarkButton);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        setBookmark.setGraphic(imageView);
        setBookmark.maxWidth(image.getWidth());
        setBookmark.maxHeight(image.getHeight());


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

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                saveBookmarks();
            }
        });

        FlowPane pane = new FlowPane(addressBar, bookmarkDropdown,setBookmark ,viewer);
        Scene scene = new Scene(pane, width, height);
        primaryStage.setTitle("Simple Web Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processAddressBar(ActionEvent event)
    {
        String tempAddress = addressBar.getText();
        try{
            webAddr = new URL(tempAddress);
            webAddr.openStream().close();

            address = tempAddress;
        }
        catch (IOException e)
        {
            //Popup notifying user that url is invalid
            //Make address the previous web address
            Alert alert = new Alert(AlertType.INFORMATION,
                    "Invalid URL" , ButtonType.OK);
            alert.showAndWait();

            e.printStackTrace();
        }
        catch (Exception e) //For handling MalformedURLException
        {
            //Make address the previous web address
            Alert alert = new Alert(AlertType.INFORMATION,
                    "Invalid URL" , ButtonType.OK);
            alert.showAndWait();

            e.printStackTrace();
        }

        viewer.getEngine().load(address);
    }

    public void processBookmarkButton(ActionEvent event){
        String id = "";

        Image image = new Image(getClass().getResourceAsStream("kappa.png"));
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

    public void processBookmarkDropdown(ActionEvent e)
    {
        int index = bookmarkDropdown.getSelectionModel().getSelectedIndex();

        //Displays web address of chosen ID
        address = bookmarks.getBookmark(index).getWebAddress();
        viewer.getEngine().load(address);
    }

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

    public void loadBookmarks()
    {
        FileInputStream file = null;
        ObjectInputStream infile = null;

        try {
            file = new FileInputStream("bookmarks.dat");
            infile = new ObjectInputStream(file);
            while(true){
                Bookmark book = (Bookmark) infile.readObject();
                System.out.println(book);
                bookmarks.addBookmark(book);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot find the file");
        }
        catch (IOException e){
            System.out.println("");
        }
        catch(ClassNotFoundException e){
            System.out.println("Problem parsing file");
        } catch (EqualIDsException e) {
            System.out.print("Bookmark Already Exist");
        } finally {
            try{
                if (infile != null)
                    infile.close();
            }
            catch (IOException e){
                System.out.println("Problem closing the file");
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
