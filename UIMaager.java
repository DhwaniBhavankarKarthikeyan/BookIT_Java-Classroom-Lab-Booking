package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

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

public class UIManager {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/JavaProject";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bhavankar4545";
	
	// Method to perform authentication against the database
    public boolean isValidUser(String username, String password) {
        String query = "SELECT * FROM Credentials WHERE uname = ? AND pwd = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if there is a matching user in the database
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error accessing database. Please try again later.");
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to change password
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Check if the old password matches the one in the database
        if (isValidUser(username, oldPassword)) {
            // Update the password in the database
            String updateQuery = "UPDATE Credentials SET pwd = ? WHERE uname = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, newPassword);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating password.");
                return false;
            }
        } else {
            return false;
        }
    }
    
 // Method to update lab capacity table with booking details
    public void updateLabCapacity(String labName, String startTime, String endTime, String username) {
        String updateQuery = "UPDATE LabsCapacity SET start_time = ?, end_time = ?, booked_by = ? WHERE LabName = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            // Set parameters
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            pstmt.setString(3, username);
            pstmt.setString(4, labName);
            // Execute update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating lab capacity.");
        }
    }

    // Method to update classroom capacity table with booking details
    public void updateClassroomCapacity(String classroomId, String startTime, String endTime, String username) {
        String updateQuery = "UPDATE ClassroomCapacity SET start_time = ?, end_time = ?, booked_by = ? WHERE classroomid = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            // Set parameters
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            pstmt.setString(3, username);
            pstmt.setString(4, classroomId);
            // Execute update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating classroom capacity.");
        }
    }
    
 // Method to get available labs for a given time slot
    public ObservableList<String> getAvailableLabs(String startTime, String endTime) {
        ObservableList<String> availableLabs = FXCollections.observableArrayList();
        String query = "SELECT LabName FROM LabsCapacity WHERE LabName NOT IN " +
                "(SELECT LabName FROM LabsCapacity WHERE (? BETWEEN start_time AND end_time) OR (? BETWEEN start_time AND end_time) OR " +
                "(start_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?))";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set parameters
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            pstmt.setString(3, startTime);
            pstmt.setString(4, endTime);
            pstmt.setString(5, startTime);
            pstmt.setString(6, endTime);
            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Add available labs to the list
                while (rs.next()) {
                    availableLabs.add(rs.getString("LabName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching available labs.");
        }
        return availableLabs;
    }

    // Method to get available classrooms for a given time slot
    public ObservableList<String> getAvailableClassrooms(String startTime, String endTime) {
        ObservableList<String> availableClassrooms = FXCollections.observableArrayList();
        String query = "SELECT classroomid FROM ClassroomCapacity WHERE classroomid NOT IN " +
                "(SELECT classroomid FROM ClassroomCapacity WHERE (? BETWEEN start_time AND end_time) OR (? BETWEEN start_time AND end_time) OR " +
                "(start_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?))";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set parameters
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            pstmt.setString(3, startTime);
            pstmt.setString(4, endTime);
            pstmt.setString(5, startTime);
            pstmt.setString(6, endTime);
            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Add available classrooms to the list
                while (rs.next()) {
                    availableClassrooms.add(rs.getString("classroomid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching available classrooms.");
        }
        return availableClassrooms;
    }

	
	public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Create the grid pane layout
        VBox grid = new VBox(10);
        grid.setPadding(new Insets(20));

        // Create username label and field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        grid.getChildren().addAll(usernameLabel, usernameField);

        // Create password label and field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        grid.getChildren().addAll(passwordLabel, passwordField);

        // Create login button
        Button loginButton = new Button("Login");
        grid.getChildren().add(loginButton);

        // Create label for showing success or error message
        Label messageLabel = new Label();
        grid.getChildren().add(messageLabel);

        // Create action for login button
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Perform authentication
            if (isValidUser(username, password)) {
                messageLabel.setText("Login successful!");
                navigateToDashboard(primaryStage, username);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Invalid username or password. Please try again.");
            }
        });

        // Set scene
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    

    // Method to navigate to the dashboard after successful login
    private void navigateToDashboard(Stage primaryStage, String username) {
        primaryStage.close();

        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Dashboard");

        // Create border pane layout for the dashboard
        BorderPane borderPane = new BorderPane();

        // Create tab pane for navigation
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Disallow closing tabs

        // Create tabs without allowing them to be closable
        Tab currentBookingsTab = new Tab("Current Bookings");
        currentBookingsTab.setClosable(false);
        currentBookingsTab.setContent(createCurrentBookingsScene(username)); // Pass username

        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);

        // Create a VBox for the profile tab content
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));

        // Label to display username
        Label usernameLabel = new Label("Username: " + username);

        // Button to change password
        Button changePasswordButton = new Button("Change Password");

        // Action for change password button
        changePasswordButton.setOnAction(event -> {
            // Show password change dialog
            showChangePasswordDialog(username);
        });

        // Add UI elements to the profile layout
        profileLayout.getChildren().addAll(usernameLabel, changePasswordButton);

        // Set profile layout as content of profile tab
        profileTab.setContent(profileLayout);

        Tab bookingTab = new Tab("Booking");
        bookingTab.setClosable(false);
        bookingTab.setContent(createBookingScene(username, currentBookingsTab)); // Pass username to booking scene

        // Add tabs to the tab pane
        tabPane.getTabs().addAll(currentBookingsTab, profileTab, bookingTab);

        // Set the tab pane as the center of the border pane
        borderPane.setCenter(tabPane);

        // Create scene for the dashboard and set it to the stage
        Scene dashboardScene = new Scene(borderPane, 800, 600);
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
    }
    
 // Method to show change password dialog
    private void showChangePasswordDialog(String username) {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Change Password");

        // Create UI elements for the dialog
        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(20));

        // Password fields for old and new passwords
        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Enter Previous Password");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");

        // Button to confirm password change
        Button confirmButton = new Button("Confirm");

        // Action for confirm button
        confirmButton.setOnAction(event -> {
            // Get the entered passwords
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();

            // Perform password change
            if (changePassword(username, oldPassword, newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
                dialogStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid previous password.");
            }
        });

        // Add UI elements to the dialog layout
        dialogLayout.getChildren().addAll(oldPasswordField, newPasswordField, confirmButton);

        // Create scene for the dialog
        Scene dialogScene = new Scene(dialogLayout, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

 // Method to fetch bookings for a particular user and room type
    private ObservableList<String> getBookingsForUserAndRoomType(String tableName, String username) {
        ObservableList<String> bookings = FXCollections.observableArrayList();
        String roomNameColumn = tableName.equals("LabsCapacity") ? "LabName" : "classroomid";
        String query = "SELECT * FROM " + tableName + " WHERE booked_by = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String roomName = rs.getString(roomNameColumn);
                    String bookingInfo = roomName + " - " + rs.getString("start_time") + " to " + rs.getString("end_time");
                    bookings.add(bookingInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching bookings.");
        }
        return bookings;
    }

	private VBox createCurrentBookingsScene(String username) {
        VBox currentBookingsLayout = new VBox(10);
        currentBookingsLayout.setPadding(new Insets(20));

        // Fetch and display bookings from LabsCapacity table
        ObservableList<String> labBookings = getBookingsForUserAndRoomType("LabsCapacity", username);
        Label labBookingLabel = new Label("Lab Bookings:");
        ListView<String> labBookingListView = new ListView<>(labBookings);

        // Add event handler to Lab Booking list view to set focus when clicked
        labBookingListView.setOnMouseClicked(event -> labBookingListView.requestFocus());

        // Set cell factory to display delete button next to each item
        labBookingListView.setCellFactory(param -> new ListCell<String>() {
            private Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        // Handle delete action
                        handleDeleteBooking(item, "LabsCapacity", username, labBookingListView);
                    });
                }
            }
        });
        

        currentBookingsLayout.getChildren().addAll(labBookingLabel, labBookingListView);

        // Fetch and display bookings from ClassroomCapacity table
        ObservableList<String> classroomBookings = getBookingsForUserAndRoomType("ClassroomCapacity", username);
        Label classroomBookingLabel = new Label("Classroom Bookings:");
        ListView<String> classroomBookingListView = new ListView<>(classroomBookings);
        currentBookingsLayout.getChildren().addAll(classroomBookingLabel, classroomBookingListView);

        // Set cell factory for classroom bookings list view
        classroomBookingListView.setCellFactory(param -> new ListCell<String>() {
            private Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        // Handle delete action
                        handleDeleteBooking(item, "ClassroomCapacity", username, classroomBookingListView);
                    });
                }
            }
        });

        return currentBookingsLayout;
    }
	
	private void handleDeleteBooking(String selectedItem, String tableName, String username, ListView<String> listView) {
        String[] bookingParts = selectedItem.split(" - ");
        String roomName = bookingParts[0];
        String startTime = bookingParts[1].split(" to ")[0];
        String endTime = bookingParts[1].split(" to ")[1];

        // Update the respective columns to NULL in the database
        String updateQuery = "UPDATE " + tableName + " SET start_time = NULL, end_time = NULL, booked_by = NULL " +
                             "WHERE " + (tableName.equals("LabsCapacity") ? "LabName" : "classroomid") + " = ? " +
                             "AND start_time = ? AND end_time = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, startTime);
            pstmt.setString(3, endTime);
            pstmt.executeUpdate();
            // Refresh the list view
            listView.getItems().remove(selectedItem);
            showAlert(Alert.AlertType.INFORMATION, "Booking Deleted", "Selected booking has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating booking.");
        }
    }
    
 // Modify your createBookingScene method to check for end time greater than start time and throw InvalidTimeException
    private VBox createBookingScene(String username, Tab currentBookingsTab) {
        VBox bookingLayout = new VBox(10);
        bookingLayout.setPadding(new Insets(20));

        // Create a choice box to select between booking a lab or a classroom
        Label selectLabel = new Label("Select Room Type:");
        ChoiceBox<String> roomTypeChoiceBox = new ChoiceBox<>();
        roomTypeChoiceBox.getItems().addAll("Lab", "Classroom");
        roomTypeChoiceBox.setValue("Lab"); // Default selection
        bookingLayout.getChildren().addAll(selectLabel, roomTypeChoiceBox);

        // Create date picker for selecting date
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        bookingLayout.getChildren().addAll(dateLabel, datePicker);
        
        

        // Create combo boxes for selecting start time and end time
        Label startTimeLabel = new Label("Start Time:");
        ComboBox<String> startTimeComboBox = createTimeComboBox();
        Label endTimeLabel = new Label("End Time:");
        ComboBox<String> endTimeComboBox = createTimeComboBox();
        bookingLayout.getChildren().addAll(startTimeLabel, startTimeComboBox, endTimeLabel, endTimeComboBox);

        // Create button for checking available rooms
        Button checkAvailabilityButton = new Button("Check Availability");
        bookingLayout.getChildren().add(checkAvailabilityButton);

        // Create a list view to display available rooms
        ListView<String> availableRoomsListView = new ListView<>();
        bookingLayout.getChildren().add(availableRoomsListView);

        // Create button for booking
        Button bookButton = new Button("Book Selected Room");
        bookingLayout.getChildren().add(bookButton);

        // Action for the check availability button
        checkAvailabilityButton.setOnAction(event -> {
            String roomType = roomTypeChoiceBox.getValue();
            LocalDate date = datePicker.getValue();
            String startTime = startTimeComboBox.getValue();
            String endTime = endTimeComboBox.getValue();

            if (date != null && startTime != null && endTime != null) {
                // Check if end time is greater than start time
                if (LocalTime.parse(endTime).isBefore(LocalTime.parse(startTime))) {
                    // Throw custom exception if end time is greater than start time
                    showAlert(Alert.AlertType.ERROR, "Error", "End time cannot be greater than start time.");
                    return;
                }

                // Construct date time strings
                String formattedStartTime = date.toString() + " " + startTime;
                String formattedEndTime = date.toString() + " " + endTime;

                // Get available rooms
                if (roomType.equals("Lab")) {
                    availableRoomsListView.getItems().clear();
                    availableRoomsListView.getItems().addAll(getAvailableLabs(formattedStartTime, formattedEndTime));
                } else {
                    availableRoomsListView.getItems().clear();
                    availableRoomsListView.getItems().addAll(getAvailableClassrooms(formattedStartTime, formattedEndTime));
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select date, start time, and end time.");
            }
        });

        // Action for the book button
        bookButton.setOnAction(event -> {
            String selectedRoom = availableRoomsListView.getSelectionModel().getSelectedItem();
            if (selectedRoom != null) {
                String roomType = roomTypeChoiceBox.getValue();
                LocalDate date = datePicker.getValue();
                String startTime = startTimeComboBox.getValue();
                String endTime = endTimeComboBox.getValue();

                // Construct date time strings
                String formattedStartTime = date.toString() + " " + startTime;
                String formattedEndTime = date.toString() + " " + endTime;

                // Update the respective table based on the room type
                if (roomType.equals("Lab")) {
                    updateLabCapacity(selectedRoom, formattedStartTime, formattedEndTime, username); // Pass username
                } else {
                    updateClassroomCapacity(selectedRoom, formattedStartTime, formattedEndTime, username); // Pass username
                }
                showAlert(Alert.AlertType.INFORMATION, "Booking Success", "Room booked successfully!");
                refreshTab(currentBookingsTab, username);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a room.");
            }
        });

        return bookingLayout;
    }
    
 // Method to create time combo box with time slots from 8:00 AM to 6:00 PM
    private ComboBox<String> createTimeComboBox() {
        ComboBox<String> timeComboBox = new ComboBox<>();
        for (int i = 8; i <= 18; i++) {
            timeComboBox.getItems().add(String.format("%02d:00", i));
        }
        return timeComboBox;
    }
    
 // Method to refresh the current bookings tab after a new booking is made
    public void refreshTab(Tab tab, String username) {
        VBox content = (VBox) tab.getContent();
        content.getChildren().clear();
        content.getChildren().addAll(createCurrentBookingsScene(username).getChildren());
    }

    // Method to show an alert dialog
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
