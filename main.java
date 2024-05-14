package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Main extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/JavaProject";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bhavankar4545";

    // Declare ListView at the class level
    private ListView<String> availableRoomsListView;

    

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of UIManager
        UIManager uiManager = new UIManager();
        // Call the start method of UIManager
        uiManager.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
