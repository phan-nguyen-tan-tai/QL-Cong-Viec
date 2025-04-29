package com.example.quanlycongviec.views;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class HenGioView extends BorderPane {
    private final StackPane centerContainer = new StackPane();

    public HenGioView(Runnable onBack) {
        Button btnBack = new Button("← Quay lại");
        btnBack.setOnAction(e -> onBack.run());
        btnBack.setStyle("-fx-font-size: 14;");
        btnBack.setPadding(new Insets(5));

        HBox topBar = new HBox(btnBack);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        this.setTop(topBar);

        // Các giao diện con cho từng nút
        Label henGioUI = new Label("[Giao diện Hẹn giờ]");
        Label danhSachUI = new Label("[Giao diện Danh sách hẹn giờ]");
        Label luuUI = new Label("[Giao diện Lưu dữ liệu]");
        Label xoaUI = new Label("[Giao diện Xoá dữ liệu]");

        // Thêm mặc định giao diện đầu tiên
        centerContainer.getChildren().add(henGioUI);
        this.setCenter(centerContainer);

        // Giao diện nút thanh bên phải sẽ được xử lý trong HomeWindow
        // Chúng ta tạo các public method để gọi từ HomeWindow
        this.setPrefSize(500, 400); // Chỉ để test nếu cần
    }

    public void showHenGio() {
        switchCenter(new Label("[Giao diện Hẹn giờ]"));
    }

    public void showDanhSach() {
        switchCenter(new Label("[Giao diện Danh sách hẹn giờ]"));
    }

    public void showLuu() {
        switchCenter(new Label("[Giao diện Lưu dữ liệu]"));
    }

    public void showXoa() {
        switchCenter(new Label("[Giao diện Xoá dữ liệu]"));
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