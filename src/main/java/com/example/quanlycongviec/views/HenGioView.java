package com.example.quanlycongviec.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class HenGioView extends BorderPane {
    public HenGioView(Runnable onBack) {
        Button btnBack = new Button("← Quay lại");
        btnBack.setOnAction(e -> onBack.run());
        btnBack.setStyle("-fx-font-size: 14;");
        btnBack.setPadding(new Insets(5));

        HBox topBar = new HBox(btnBack);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        this.setTop(topBar);

        Label title = new Label("Giao diện Hẹn giờ");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        Button btnHenGio = new Button("Hẹn giờ");
        Button btnDanhSach = new Button("Danh sách hẹn giờ");
        Button btnLuu = new Button("Lưu");
        Button btnXoa = new Button("Xoá");

        Button[] buttons = {btnHenGio, btnDanhSach, btnLuu, btnXoa};
        for (Button btn : buttons) {
            btn.setPrefWidth(200);
            btn.setPrefHeight(40);
            btn.setStyle("-fx-font-size: 16;");
        }

        VBox contentBox = new VBox(20, title, btnHenGio, btnDanhSach, btnLuu, btnXoa);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30));

        this.setCenter(contentBox);
    }
}
