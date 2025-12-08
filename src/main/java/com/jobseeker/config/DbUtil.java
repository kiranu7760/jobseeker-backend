package com.jobseeker.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/jobseeker?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "tiger";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

