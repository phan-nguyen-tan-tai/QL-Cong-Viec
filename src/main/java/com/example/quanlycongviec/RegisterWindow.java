package com.example.quanlycongviec;

import com.example.quanlycongviec.PasswordUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterWindow {
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Đăng ký tài khoản");

        Label titleLabel = new Label("ĐĂNG KÝ TÀI KHOẢN");

        Label phoneLabel = new Label("Số điện thoại:");
        TextField phoneField = new TextField();

        Label passwordLabel = new Label("Mật khẩu:");
        PasswordField passwordField = new PasswordField();

        Label confirmLabel = new Label("Xác nhận mật khẩu:");
        PasswordField confirmPasswordField = new PasswordField();

        Button submitButton = new Button("Tạo tài khoản");

        submitButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String pass = passwordField.getText().trim();
            String confirm = confirmPasswordField.getText().trim();

            // Kiểm tra số điện thoại hợp lệ
            if (!isValidPhoneNumber(phone)) {
                showAlert(Alert.AlertType.ERROR, "Số điện thoại không hợp lệ. Số điện thoại phải có 10 chữ số và bắt đầu bằng một trong các đầu số hợp lệ của Việt Nam.");
                return;
            }

            if (phone.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            if (!pass.equals(confirm)) {
                showAlert(Alert.AlertType.ERROR, "Mật khẩu xác nhận không khớp.");
                return;
            }

            // Hash mật khẩu
            String hashedPassword = PasswordUtils.hashPassword(pass);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                writer.write(phone + "," + hashedPassword);
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Đã xảy ra lỗi khi ghi vào file.");
                return;
            }

            showAlert(Alert.AlertType.INFORMATION, "Đăng ký thành công cho số điện thoại: " + phone);
            stage.close();
        });

        VBox layout = new VBox(10, titleLabel, phoneLabel, phoneField, passwordLabel, passwordField, confirmLabel, confirmPasswordField, submitButton);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 300, 400));
        stage.show();
    }

    private boolean isValidPhoneNumber(String phone) {
        // Kiểm tra số điện thoại phải có 10 chữ số và bắt đầu bằng các đầu số hợp lệ
        return phone.length() == 10 && phone.matches("^(090|091|092|093|094|095|096|097|098|070|076|077|078|079)\\d{7}$");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
