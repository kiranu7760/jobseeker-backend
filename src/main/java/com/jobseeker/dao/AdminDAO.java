package com.jobseeker.dao;

import java.sql.*;
import com.jobseeker.config.DbUtil;
import com.jobseeker.model.Admin;

public class AdminDAO {

    public boolean registerAdmin(Admin a) {
        String sql = "INSERT INTO admin (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getEmail());
            ps.setString(3, a.getPassword());
            ps.setString(4, a.getRole());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Admin login(String email, String password) {
        String sql = "SELECT * FROM admin WHERE email = ?";
        
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setEmail(rs.getString("email"));
                a.setPassword(rs.getString("password"));
                a.setRole(rs.getString("role"));
                return a;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
