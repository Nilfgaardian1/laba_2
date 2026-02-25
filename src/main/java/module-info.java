module com.example.laba2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.laba2 to javafx.fxml;
    exports com.example.laba2;
}