package com.example.quanlycongviec;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quản lý công việc");

        // === Left side: Full-size Image ===
        ImageView imageView = new ImageView();
        URL imageUrl = getClass().getResource("/images/clocktime1.jpg");
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            imageView.setImage(image);
        } else {
            System.err.println("Không tìm thấy ảnh: /images/clocktime1.jpg");
        }

        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);

        StackPane leftPane = new StackPane(imageView);
        leftPane.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));
        leftPane.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));
        leftPane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));

        // === Right side: Form ===
        Label titleLabel = new Label("QUẢN LÝ THỜI GIAN");

        // Số điện thoại
        Label phoneLabel = new Label("Số điện thoại:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Nhập số điện thoại");

        Label passwordLabel = new Label("Mật khẩu:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nhập mật khẩu");

        // Tăng chiều cao cho các ô nhập
        phoneField.setMinHeight(40);
        phoneField.setPrefHeight(40);
        passwordField.setMinHeight(40);
        passwordField.setPrefHeight(40);

        // Đăng nhập, Đăng ký
        Button loginButton = new Button("Đăng nhập");
        Button registerButton = new Button("Đăng ký");

        // "Quên mật khẩu?" như link
        Button forgotPasswordButton = new Button("Quên mật khẩu?");
        forgotPasswordButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: dodgerblue;" +
                        "-fx-underline: true;" +
                        "-fx-cursor: hand;"
        );
        forgotPasswordButton.setPadding(new Insets(0));

        loginButton.setStyle("-fx-background-color: limegreen; -fx-text-fill: white;");
        registerButton.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white;");

        HBox buttonsBox = new HBox(10, loginButton, registerButton);
        VBox buttonGroup = new VBox(10, buttonsBox, forgotPasswordButton);

        VBox rightPane = new VBox(15, titleLabel, phoneLabel, phoneField, passwordLabel, passwordField, buttonGroup);
        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(300);
        rightPane.setMaxWidth(Double.MAX_VALUE);

        HBox mainLayout = new HBox(leftPane, rightPane);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 800, 500, Color.WHITE);

        // === Ảnh full nửa cửa sổ ===
        imageView.fitWidthProperty().bind(scene.widthProperty().multiply(0.5));
        imageView.fitHeightProperty().bind(scene.heightProperty());

        // === Font responsive ===
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", scene.widthProperty().divide(25),
                "px; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Times New Roman';"
        ));

        phoneLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", scene.widthProperty().divide(35), "px;"));
        passwordLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", scene.widthProperty().divide(35), "px;"));

        loginButton.styleProperty().bind(Bindings.concat(
                "-fx-background-color: limegreen; -fx-text-fill: white; -fx-font-size: ",
                scene.widthProperty().divide(35), "px;"
        ));
        registerButton.styleProperty().bind(Bindings.concat(
                "-fx-background-color: dodgerblue; -fx-text-fill: white; -fx-font-size: ",
                scene.widthProperty().divide(35), "px;"
        ));
        forgotPasswordButton.styleProperty().bind(Bindings.concat(
                "-fx-background-color: transparent; -fx-text-fill: dodgerblue; -fx-underline: true; -fx-cursor: hand; -fx-font-size: ",
                scene.widthProperty().divide(38), "px;"
        ));

        // Tăng font chữ trong TextField khi phóng to cửa sổ
        phoneField.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", scene.widthProperty().divide(40), "px; " +
                        "-fx-min-height: ", scene.heightProperty().divide(15), "px;"
        ));
        passwordField.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", scene.widthProperty().divide(40), "px; " +
                        "-fx-min-height: ", scene.heightProperty().divide(15), "px;"
        ));

        primaryStage.setScene(scene);
        primaryStage.show();

        loginButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String pass = passwordField.getText().trim();

            // Hash mật khẩu người dùng nhập vào
            String hashedInputPass = PasswordUtils.hashPassword(pass);
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String storedPhone = parts[0].trim();
                        String storedHashedPass = parts[1].trim();
                        if (storedPhone.equals(phone) && storedHashedPass.equals(hashedInputPass)) {
                            found = true;
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (found) {
                HomeWindow home = new HomeWindow();
                home.show();
                primaryStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi đăng nhập");
                alert.setHeaderText(null);
                alert.setContentText("Số điện thoại hoặc mật khẩu không đúng!");
                alert.showAndWait();
            }
        });

        registerButton.setOnAction(e -> {
            RegisterWindow registerWindow = new RegisterWindow();
            registerWindow.show();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
