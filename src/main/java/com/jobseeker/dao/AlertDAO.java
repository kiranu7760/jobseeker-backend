package com.jobseeker.dao;

import java.sql.*;

import com.jobseeker.config.DbUtil;

public class AlertDAO {

    public boolean enableAlerts(int userId) {
        String sql = "INSERT INTO job_alerts (user_id, enabled) VALUES (?, TRUE) "
                   + "ON DUPLICATE KEY UPDATE enabled = TRUE";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean disableAlerts(int userId) {
        String sql = "UPDATE job_alerts SET enabled = FALSE WHERE user_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEnabled(int userId) {
        String sql = "SELECT enabled FROM job_alerts WHERE user_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("enabled");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // default
    }
}
