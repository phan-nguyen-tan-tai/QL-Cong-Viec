package com.example.quanlycongviec.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class ThoiKhoaBieuView extends BorderPane {
    public ThoiKhoaBieuView(Runnable onBack) {
        Button btnBack = new Button("← Quay lại");
        btnBack.setOnAction(e -> onBack.run());
        btnBack.setStyle("-fx-font-size: 14;");
        btnBack.setPadding(new Insets(5));

        HBox topBar = new HBox(btnBack);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        this.setTop(topBar);

        Label title = new Label("Giao diện Thời khoá biểu");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        Button btnTKBHangNgay = new Button("Xem TKB hàng ngày");
        Button btnSuaTKB = new Button("Sửa TKB");
        Button btnXoaTKB = new Button("Xoá TKB");

        Button[] buttons = {btnTKBHangNgay, btnSuaTKB, btnXoaTKB};
        for (Button btn : buttons) {
            btn.setPrefWidth(220);
            btn.setPrefHeight(40);
            btn.setStyle("-fx-font-size: 16;");
        }

        VBox contentBox = new VBox(20, title, btnTKBHangNgay, btnSuaTKB, btnXoaTKB);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30));

        this.setCenter(contentBox);
    }
}
