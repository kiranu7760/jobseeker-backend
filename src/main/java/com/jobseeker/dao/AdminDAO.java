package com.jobseeker.dao;

import com.jobseeker.config.DbUtil;
import com.jobseeker.model.Admin;

import java.sql.*;

public class AdminDAO {

    // ---------------------------
    // Insert new admin
    // ---------------------------
    public boolean insertAdmin(Admin admin) {
        String sql = "INSERT INTO admins (name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.getName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPasswordHash());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------
    // Get admin by email
    // ---------------------------
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admins WHERE email = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPasswordHash(rs.getString("password_hash"));
                return admin;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
