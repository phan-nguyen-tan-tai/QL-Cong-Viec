package com.example.quanlycongviec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString(); // Trả về mật khẩu đã hash dạng hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
