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
    private VBox mainToolbar;
    private BorderPane mainLayout;

    public void show() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);

        // Ngày tháng hiện tại
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

        // Đồng hồ số
        Label digitalClockLabel = new Label();
        digitalClockLabel.setTextFill(Color.WHITE);
        digitalClockLabel.fontProperty().bind(Bindings.createObjectBinding(() -> {
            double fontSize = Math.max(20, stage.getWidth() / 25);
            return new Font("Seven Segment", fontSize);
        }, stage.widthProperty()));

        // Đồng hồ kim
        analogClockCanvas = new Canvas();
        StackPane analogClockWrapper = new StackPane(analogClockCanvas);
        analogClockWrapper.setPrefSize(300, 300);
        analogClockCanvas.widthProperty().bind(analogClockWrapper.widthProperty());
        analogClockCanvas.heightProperty().bind(analogClockWrapper.heightProperty());

        Timeline clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            updateDigitalClock(digitalClockLabel);
            drawAnalogClock();
        }));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();

        clockBox = new VBox(10, dateLabel, analogClockWrapper, digitalClockLabel);
        clockBox.setAlignment(Pos.CENTER);
        clockBox.setPadding(new Insets(20));

        // Background tự động đổi theo giờ
        updateBackgroundByTime();
        Timeline backgroundTimeline = new Timeline(new KeyFrame(Duration.minutes(5), e -> updateBackgroundByTime()));
        backgroundTimeline.setCycleCount(Timeline.INDEFINITE);
        backgroundTimeline.play();

        // Các nút chức năng chính
        Button btnHenGio = createMainButton("Hẹn giờ");
        Button btnXemLich = createMainButton("Xem lịch");
        Button btnLichTrongNgay = createMainButton("Lịch trong ngày");
        Button btnThoiKhoaBieu = createMainButton("Thời khoá biểu");

        mainToolbar = new VBox(20, btnHenGio, btnXemLich, btnLichTrongNgay, btnThoiKhoaBieu);
        mainToolbar.setAlignment(Pos.CENTER);
        mainToolbar.setPadding(new Insets(20));
        mainToolbar.setPrefWidth(200);
        mainToolbar.setStyle("-fx-background-color: #e6f7ff;");

        mainLayout = new BorderPane();
        rootContainer.getChildren().add(clockBox);
        mainLayout.setCenter(rootContainer);
        mainLayout.setRight(mainToolbar);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Quản lý thời gian");

        analogClockCanvas.widthProperty().addListener((obs, oldVal, newVal) -> drawAnalogClock());
        analogClockCanvas.heightProperty().addListener((obs, oldVal, newVal) -> drawAnalogClock());

        // Sự kiện bấm các nút chức năng
        btnHenGio.setOnAction(e -> {
            HenGioView henGioView = new HenGioView(this::switchToMainView);
            switchToView(henGioView, makeSubToolbarForHenGio(henGioView));
        });

        btnXemLich.setOnAction(e -> {
            XemLichView xemLichView = new XemLichView(this::switchToMainView);
            switchToView(xemLichView, makeSubToolbarForXemLich(xemLichView));
        });

        btnLichTrongNgay.setOnAction(e -> {
            LichTrongNgayView lichTrongNgayView = new LichTrongNgayView(this::switchToMainView);
            switchToView(lichTrongNgayView, makeSubToolbarForLichTrongNgay(lichTrongNgayView));
        });

        btnThoiKhoaBieu.setOnAction(e -> {
            ThoiKhoaBieuView thoiKhoaBieuView = new ThoiKhoaBieuView(this::switchToMainView);
            switchToView(thoiKhoaBieuView, makeSubToolbarForThoiKhoaBieu(thoiKhoaBieuView));
        });

        stage.show();
    }

    private void switchToMainView() {
        switchToView(clockBox, mainToolbar);
    }

    private void switchToView(Node newView, VBox newToolbar) {
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
        mainLayout.setRight(newToolbar);
    }

    private Button createMainButton(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font(16));
        btn.setPrefHeight(60);
        btn.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(btn, Priority.ALWAYS);
        return btn;
    }

    private VBox makeSubToolbarForHenGio(HenGioView view) {
        Button btn1 = createSubButton("Hẹn giờ", view::showHenGio);
        Button btn2 = createSubButton("Danh sách hẹn giờ", view::showDanhSach);
        Button btn3 = createSubButton("Lưu", view::showLuu);
        Button btn4 = createSubButton("Xoá", view::showXoa);
        return styleSubToolbar(btn1, btn2, btn3, btn4);
    }

    private VBox makeSubToolbarForXemLich(XemLichView view) {
        Button btn1 = createSubButton("Lịch dương", view::showLichDuong);
        Button btn2 = createSubButton("Lịch âm", view::showLichAm);
        Button btn3 = createSubButton("Sự kiện trong năm", view::showSuKienNam);
        return styleSubToolbar(btn1, btn2, btn3);
    }

    private VBox makeSubToolbarForLichTrongNgay(LichTrongNgayView view) {
        Button btn1 = createSubButton("Lịch trình", view::showLichTrongNgay);
        Button btn2 = createSubButton("Sửa", view::showSua);
        Button btn3 = createSubButton("Lưu", view::showLuu);
        Button btn4 = createSubButton("Xoá", view::showXoa);
        return styleSubToolbar(btn1, btn2, btn3, btn4);
    }

    private VBox makeSubToolbarForThoiKhoaBieu(ThoiKhoaBieuView view) {
        Button btn1 = createSubButton("Thời khoá biểu", view::showThoiKhoaBieu);
        Button btn2 = createSubButton("Sửa", view::showSua);
        Button btn3 = createSubButton("Lưu", view::showLuu);
        Button btn4 = createSubButton("Xoá", view::showXoa);
        return styleSubToolbar(btn1, btn2, btn3, btn4);
    }

    private Button createSubButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setFont(new Font(15));
        btn.setPrefHeight(45);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private VBox styleSubToolbar(Button... buttons) {
        VBox box = new VBox(15, buttons);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setPrefWidth(200);
        box.setStyle("-fx-background-color: #f0f0f0;");
        return box;
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

        LocalTime now = LocalTime.now();
        drawClockHands(gc, centerX, centerY, radius, now);
    }

    private void drawClockHands(GraphicsContext gc, double centerX, double centerY, double radius, LocalTime time) {
        double second = time.getSecond();
        double minute = time.getMinute() + second / 60.0;
        double hour = time.getHour() % 12 + minute / 60.0;

        drawHand(gc, centerX, centerY, hour * 30, radius * 0.5, 5, Color.BLACK);
        drawHand(gc, centerX, centerY, minute * 6, radius * 0.7, 3, Color.DARKBLUE);
        drawHand(gc, centerX, centerY, second * 6, radius * 0.85, 1, Color.RED);

        gc.setFill(Color.BLACK);
        gc.fillOval(centerX - 4, centerY - 4, 8, 8);
    }

    private void drawHand(GraphicsContext gc, double cx, double cy, double angleDeg, double length, double width, Color color) {
        double angleRad = Math.toRadians(angleDeg - 90);
        double x = cx + length * Math.cos(angleRad);
        double y = cy + length * Math.sin(angleRad);
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(cx, cy, x, y);
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
}
