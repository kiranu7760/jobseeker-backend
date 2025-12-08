package com.jobseeker.dao;

import com.jobseeker.config.DbUtil;
import com.jobseeker.model.Profile;

import java.sql.*;

public class ProfileDAO {

    // Insert or update (if profile already exists for user)
    public boolean saveOrUpdate(Profile p) {
        // Check if profile exists
        Profile existing = findByUserId(p.getUserId());
        if (existing == null) {
            return insert(p);
        } else {
            p.setId(existing.getId());
            return update(p);
        }
    }

    private boolean insert(Profile p) {
        String sql = "INSERT INTO profiles(user_id, headline, summary, skills, resume_path, experience, education, resume_text, linkedin_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getHeadline());
            ps.setString(3, p.getSummary());
            ps.setString(4, p.getSkills());
            ps.setString(5, p.getResumePath());
            ps.setString(6, p.getExperience());
            ps.setString(7, p.getEducation());
            ps.setString(8, p.getResumeText());
            ps.setString(9, p.getLinkedinUrl());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) p.setId(rs.getInt(1));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update(Profile p) {
        String sql = "UPDATE profiles SET headline=?, summary=?, skills=?, resume_path=?, experience=?, education=?, resume_text=?, linkedin_url=?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getHeadline());
            ps.setString(2, p.getSummary());
            ps.setString(3, p.getSkills());
            ps.setString(4, p.getResumePath());
            ps.setString(5, p.getExperience());
            ps.setString(6, p.getEducation());
            ps.setString(7, p.getResumeText());
            ps.setString(8, p.getLinkedinUrl());
            ps.setInt(9, p.getUserId());

            int rows = ps.executeUpdate();
            return rows >= 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Profile findByUserId(int userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Profile p = new Profile();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setHeadline(rs.getString("headline"));
                p.setSummary(rs.getString("summary"));
                p.setSkills(rs.getString("skills"));
                p.setResumePath(rs.getString("resume_path"));
                p.setExperience(rs.getString("experience"));
                p.setEducation(rs.getString("education"));
                p.setResumeText(rs.getString("resume_text"));
                p.setLinkedinUrl(rs.getString("linkedin_url"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

