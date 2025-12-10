package com.jobseeker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jobseeker.config.DbUtil;
import com.jobseeker.model.Application;

public class ApplicationDAO {

    // -------------------------------
    // 1️⃣ Check if user already applied
    // -------------------------------
    public boolean hasAlreadyApplied(int userId, int jobId) {
        String sql = "SELECT id FROM applications WHERE user_id = ? AND job_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if record exists

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -------------------------------
    // 2️⃣ Insert new application
    // -------------------------------
    public boolean apply(int userId, int jobId, String resumePath) {
        String sql = "INSERT INTO applications (user_id, job_id, resume_path, status) "
                + "VALUES (?, ?, ?, 'Applied')";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);
            ps.setString(3, resumePath);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 // -------------------------------
 // 3️⃣ Get all applications for a user
 // -------------------------------
 public List<Application> getApplicationsByUser(int userId) {
     List<Application> list = new ArrayList<>();

     String sql = "SELECT a.id AS application_id, a.status, a.applied_at, "
                + "j.id AS job_id, j.title, j.company, j.location, j.type, j.salary "
                + "FROM applications a "
                + "JOIN jobs j ON a.job_id = j.id "
                + "WHERE a.user_id = ? "
                + "ORDER BY a.applied_at DESC";

     try (Connection con = DbUtil.getConnection();
          PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, userId);
         ResultSet rs = ps.executeQuery();

         while (rs.next()) {
             Application app = new Application();
             app.setId(rs.getInt("application_id"));
             app.setUserId(userId);
             app.setJobId(rs.getInt("job_id"));
             app.setStatus(rs.getString("status"));
             app.setAppliedAt(rs.getString("applied_at"));

             // These extra job fields will be added to Application.java later
             app.setJobTitle(rs.getString("title"));
             app.setCompany(rs.getString("company"));
             app.setLocation(rs.getString("location"));
             app.setType(rs.getString("type"));
             app.setSalary(rs.getString("salary"));

             list.add(app);
         }

     } catch (Exception e) {
         e.printStackTrace();
     }

     return list;
 }
 public List<Application> getApplicantsForJob(int jobId) {
	    List<Application> list = new ArrayList<>();

	    String sql = "SELECT a.id AS application_id, a.user_id, a.job_id, a.resume_path, a.status, a.applied_at, " +
	                 "u.name AS user_name, u.email AS user_email " +
	                 "FROM applications a " +
	                 "JOIN users u ON a.user_id = u.id " +
	                 "WHERE a.job_id = ? " +
	                 "ORDER BY a.applied_at DESC";

	    try (Connection con = DbUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, jobId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Application ap = new Application();
	            ap.setId(rs.getInt("application_id"));
	            ap.setUserId(rs.getInt("user_id"));
	            ap.setJobId(rs.getInt("job_id"));
	            ap.setResumePath(rs.getString("resume_path"));
	            ap.setStatus(rs.getString("status"));
	            ap.setAppliedAt(rs.getString("applied_at"));

	            // extra fields (not in application table but needed)
	            ap.setUserName(rs.getString("user_name"));
	            ap.setUserEmail(rs.getString("user_email"));

	            list.add(ap);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
 public boolean updateStatus(int applicationId, String status) {
	    String sql = "UPDATE applications SET status = ?, last_updated = CURRENT_TIMESTAMP WHERE id = ?";

	    try (Connection con = DbUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, status);
	        ps.setInt(2, applicationId);

	        return ps.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}



}
