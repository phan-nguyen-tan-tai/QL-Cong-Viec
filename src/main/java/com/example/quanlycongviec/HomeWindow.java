package com.example.quanlycongviec;

import com.example.quanlycongviec.views.HenGioView;
import com.example.quanlycongviec.views.LichTrongNgayView;
import com.example.quanlycongviec.views.ThoiKhoaBieuView;
import com.example.quanlycongviec.views.XemLichView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HomeWindow {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private VBox clockBox;
    private Canvas analogClockCanvas;
    private StackPane rootContainer = new StackPane();

    public void show() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);

        // Label ngày tháng năm
        Label dateLabel = new Label();
        dateLabel.setTextFill(Color.LIGHTYELLOW);
        dateLabel.setFont(Font.font("Times New Roman", 22));
        dateLabel.setAlignment(Pos.CENTER);

        Timeline dateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            String formattedDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("EEEE, d 'tháng' M 'năm' yyyy", new Locale("vi")));
            formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
            dateLabel.setText(formattedDate);
        }));
        dateTimeline.setCycleCount(Timeline.INDEFINITE);
        dateTimeline.play();

        // Đồng hồ điện tử
        Label digitalClockLabel = new Label();
        digitalClockLabel.setTextFill(Color.WHITE);
        digitalClockLabel.fontProperty().bind(Bindings.createObjectBinding(() -> {
            double fontSize = Math.max(20, stage.getWidth() / 25);
            return new Font("Seven Segment", fontSize);
        }, stage.widthProperty()));

        // Đồng hồ cơ
        analogClockCanvas = new Canvas();
        StackPane analogClockWrapper = new StackPane(analogClockCanvas);
        analogClockWrapper.setPrefSize(300, 300);
        analogClockCanvas.widthProperty().bind(analogClockWrapper.widthProperty());
        analogClockCanvas.heightProperty().bind(analogClockWrapper.heightProperty());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            updateDigitalClock(digitalClockLabel);
            drawAnalogClock();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Clock layout gồm: ngày + analog + digital
        clockBox = new VBox(10, dateLabel, analogClockWrapper, digitalClockLabel);
        clockBox.setAlignment(Pos.CENTER);
        clockBox.setPadding(new Insets(20));

        updateBackgroundByTime();
        Timeline backgroundUpdater = new Timeline(new KeyFrame(Duration.minutes(5), e -> updateBackgroundByTime()));
        backgroundUpdater.setCycleCount(Timeline.INDEFINITE);
        backgroundUpdater.play();

        // Nút chức năng
        Button btnHenGio = new Button("Hẹn giờ");
        Button btnXemLich = new Button("Xem lịch");
        Button btnLichTrongNgay = new Button("Lịch trong ngày");
        Button btnThoiKhoaBieu = new Button("Thời khoá biểu");

        Font buttonFont = new Font(16);
        Button[] buttons = {btnHenGio, btnXemLich, btnLichTrongNgay, btnThoiKhoaBieu};
        for (Button btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPrefHeight(60);
            btn.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(btn, Priority.ALWAYS);
        }

        VBox rightBox = new VBox(20, btnHenGio, btnXemLich, btnLichTrongNgay, btnThoiKhoaBieu);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(20));
        rightBox.setPrefWidth(200);
        rightBox.setStyle("-fx-background-color: #e6f7ff;");

        // Layout tổng
        BorderPane mainLayout = new BorderPane();
        rootContainer.getChildren().add(clockBox);
        mainLayout.setCenter(rootContainer);
        mainLayout.setRight(rightBox);

        Scene scene = new Scene(mainLayout, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Giao diện chính");

        analogClockCanvas.widthProperty().addListener((obs, oldVal, newVal) -> drawAnalogClock());
        analogClockCanvas.heightProperty().addListener((obs, oldVal, newVal) -> drawAnalogClock());

        // Sự kiện nút
        btnHenGio.setOnAction(e -> switchToView(new HenGioView(() -> switchToView(clockBox))));
        btnXemLich.setOnAction(e -> switchToView(new XemLichView(() -> switchToView(clockBox))));
        btnLichTrongNgay.setOnAction(e -> switchToView(new LichTrongNgayView(() -> switchToView(clockBox))));
        btnThoiKhoaBieu.setOnAction(e -> switchToView(new ThoiKhoaBieuView(() -> switchToView(clockBox))));

        stage.show();
    }

    private void updateDigitalClock(Label label) {
        label.setText(LocalTime.now().format(timeFormatter));
    }

    private void drawAnalogClock() {
        GraphicsContext gc = analogClockCanvas.getGraphicsContext2D();
        double w = analogClockCanvas.getWidth();
        double h = analogClockCanvas.getHeight();
        double centerX = w / 2;
        double centerY = h / 2;
        double radius = Math.min(w, h) / 2 - 10;

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.BEIGE);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(3);
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6);
            double inner = (i % 5 == 0) ? radius - 15 : radius - 8;
            double outer = radius;
            double x1 = centerX + inner * Math.cos(angle);
            double y1 = centerY + inner * Math.sin(angle);
            double x2 = centerX + outer * Math.cos(angle);
            double y2 = centerY + outer * Math.sin(angle);
            gc.setStroke(i % 5 == 0 ? Color.BLACK : Color.GRAY);
            gc.setLineWidth(i % 5 == 0 ? 2 : 1);
            gc.strokeLine(x1, y1, x2, y2);
        }

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", radius * 0.12));
        double textRadius = radius - 23;

        drawCenteredText(gc, "12", centerX, centerY - textRadius);
        drawCenteredText(gc, "3", centerX + textRadius, centerY);
        drawCenteredText(gc, "6", centerX, centerY + textRadius);
        drawCenteredText(gc, "9", centerX - textRadius, centerY);

        LocalTime now = LocalTime.now();
        double second = now.getSecond();
        double minute = now.getMinute() + second / 60.0;
        double hour = now.getHour() % 12 + minute / 60.0;

        drawHand(gc, centerX, centerY, hour * 30, radius * 0.5, 5, Color.BLACK);
        drawHand(gc, centerX, centerY, minute * 6, radius * 0.7, 3, Color.DARKBLUE);
        drawHand(gc, centerX, centerY, second * 6, radius * 0.85, 1, Color.RED);

        gc.setFill(Color.BLACK);
        gc.fillOval(centerX - 4, centerY - 4, 8, 8);
    }

    private void drawHand(GraphicsContext gc, double cx, double cy, double angleDeg,
                          double length, double width, Color color) {
        double angleRad = Math.toRadians(angleDeg - 90);
        double x = cx + length * Math.cos(angleRad);
        double y = cy + length * Math.sin(angleRad);
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(cx, cy, x, y);
    }

    private void drawCenteredText(GraphicsContext gc, String text, double x, double y) {
        Text helper = new Text(text);
        helper.setFont(gc.getFont());
        double width = helper.getLayoutBounds().getWidth();
        double height = helper.getLayoutBounds().getHeight();
        gc.fillText(text, x - width / 2, y + height / 4);
    }

    private void updateBackgroundByTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        String imagePath;

        if (hour >= 5 && hour < 6) {
            imagePath = "/images/bình minh.jpg";
        } else if (hour >= 6 && hour < 11) {
            imagePath = "/images/sáng.jpg";
        } else if (hour >= 11 && hour < 13) {
            imagePath = "/images/trưa.jpg";
        } else if (hour >= 13 && hour < 17) {
            imagePath = "/images/chiều.jpg";
        } else if (hour >= 17 && hour < 19) {
            imagePath = "/images/hoàng hôn.jpg";
        } else {
            imagePath = "/images/đêm.jpg";
        }

        try {
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            BackgroundImage bg = new BackgroundImage(
                    image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true)
            );
            clockBox.setBackground(new Background(bg));
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh nền: " + imagePath);
        }
    }

    private void switchToView(Node newView) {
        if (!rootContainer.getChildren().isEmpty()) {
            Node oldView = rootContainer.getChildren().get(0);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), oldView);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(event -> {
                rootContainer.getChildren().setAll(newView);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newView);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
        } else {
            rootContainer.getChildren().setAll(newView);
        }
    }
}
