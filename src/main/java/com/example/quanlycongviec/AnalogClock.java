package com.example.quanlycongviec;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;

public class AnalogClock extends Pane {
    private final Canvas canvas = new Canvas(300, 300);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();

    public AnalogClock() {
        this.getChildren().add(canvas);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawClock();
            }
        };
        timer.start();
    }

    private void drawClock() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double centerX = width / 2;
        double centerY = height / 2;
        double radius = Math.min(width, height) / 2 - 10;

        gc.clearRect(0, 0, width, height);

        // Mặt đồng hồ
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(4);
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Vạch giờ
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double inner = radius - 10;
            double outer = radius;
            double x1 = centerX + inner * Math.sin(angle);
            double y1 = centerY - inner * Math.cos(angle);
            double x2 = centerX + outer * Math.sin(angle);
            double y2 = centerY - outer * Math.cos(angle);
            gc.setLineWidth(2);
            gc.strokeLine(x1, y1, x2, y2);
        }

        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour() % 12;
        int minute = now.getMinute();
        int second = now.getSecond();

        // Kim giờ
        double hourAngle = Math.toRadians((hour + minute / 60.0) * 30);
        drawHand(centerX, centerY, hourAngle, radius * 0.5, 6, Color.WHITE);

        // Kim phút
        double minuteAngle = Math.toRadians((minute + second / 60.0) * 6);
        drawHand(centerX, centerY, minuteAngle, radius * 0.7, 4, Color.LIGHTBLUE);

        // Kim giây
        double secondAngle = Math.toRadians(second * 6);
        drawHand(centerX, centerY, secondAngle, radius * 0.8, 2, Color.RED);

        // Chấm trung tâm
        gc.setFill(Color.WHITE);
        gc.fillOval(centerX - 5, centerY - 5, 10, 10);
    }

    private void drawHand(double centerX, double centerY, double angle, double length, double width, Color color) {
        double x = centerX + length * Math.sin(angle);
        double y = centerY - length * Math.cos(angle);
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(centerX, centerY, x, y);
    }
}
