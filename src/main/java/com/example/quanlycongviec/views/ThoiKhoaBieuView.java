package com.example.quanlycongviec.views;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ThoiKhoaBieuView extends BorderPane {
    private final StackPane centerContainer = new StackPane();

    public ThoiKhoaBieuView(Runnable onBack) {
        Button btnBack = new Button("← Quay lại");
        btnBack.setOnAction(e -> onBack.run());
        btnBack.setStyle("-fx-font-size: 14;");
        btnBack.setPadding(new Insets(5));

        HBox topBar = new HBox(btnBack);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        this.setTop(topBar);

        centerContainer.getChildren().add(new Label("[Giao diện Thời khoá biểu]"));
        this.setCenter(centerContainer);
        this.setPrefSize(500, 400);
    }

    public void showThoiKhoaBieu() {
        switchCenter(new Label("[Giao diện Thời khoá biểu]"));
    }

    public void showSua() {
        switchCenter(new Label("[Giao diện Sửa thời khoá biểu]"));
    }

    public void showLuu() {
        switchCenter(new Label("[Giao diện Lưu thời khoá biểu]"));
    }

    public void showXoa() {
        switchCenter(new Label("[Giao diện Xoá thời khoá biểu]"));
    }

    private void switchCenter(Node newContent) {
        if (!centerContainer.getChildren().isEmpty()) {
            Node oldContent = centerContainer.getChildren().get(0);
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), oldContent);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                centerContainer.getChildren().setAll(newContent);
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), newContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            centerContainer.getChildren().setAll(newContent);
        }
    }
}
