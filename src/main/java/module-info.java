module com.example.quanlycongviec {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quanlycongviec to javafx.fxml;
    exports com.example.quanlycongviec;
}