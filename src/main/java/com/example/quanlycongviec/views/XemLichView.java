package com.example.quanlycongviec.views;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class XemLichView extends BorderPane {
    private final StackPane centerContainer = new StackPane();

    public XemLichView(Runnable onBack) {
        Button btnBack = new Button("← Quay lại");
        btnBack.setOnAction(e -> onBack.run());
        btnBack.setStyle("-fx-font-size: 14;");
        btnBack.setPadding(new Insets(5));

        HBox topBar = new HBox(btnBack);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        this.setTop(topBar);

        centerContainer.getChildren().add(new Label("[Giao diện Lịch dương]"));
        this.setCenter(centerContainer);
        this.setPrefSize(500, 400);
    }

    public void showLichDuong() {
        switchCenter(new Label("[Giao diện Lịch dương]"));
    }

    public void showLichAm() {
        switchCenter(new Label("[Giao diện Lịch âm]"));
    }

    public void showSuKienNam() {
        switchCenter(new Label("[Giao diện Sự kiện trong năm]"));
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
