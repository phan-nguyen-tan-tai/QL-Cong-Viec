package com.example.quanlycongviec;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HomeWindow {
    public void show() {
        Stage stage = new Stage();
        StackPane root = new StackPane(new Label("Chào mừng bạn đến giao diện chính!"));
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Giao diện chính");
        stage.show();
    }
}
