package com.jobseeker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.jobseeker.config.DbUtil;
import com.jobseeker.model.Job;

public class JobDAO {

	// ---------- ADD NEW JOB ----------
	public boolean addJob(Job job) {
		String sql = "INSERT INTO jobs (title, company, location, description, required_skills, created_by, salary, type, source, url, company_logo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DbUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, job.getTitle());
			ps.setString(2, job.getCompany());
			ps.setString(3, job.getLocation());
			ps.setString(4, job.getDescription());
			ps.setString(5, job.getRequiredSkills());
			ps.setObject(6, job.getCreatedBy());
			ps.setString(7, job.getSalary());
			ps.setString(8, job.getType());
			ps.setString(9, job.getSource());
			ps.setString(10, job.getUrl());
			ps.setString(11, job.getCompanyLogo());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ---------- LIST ALL JOBS ----------
	public List<Job> getAllJobs() {
		List<Job> list = new ArrayList<>();
		String sql = "SELECT * FROM jobs ORDER BY created_at DESC";

		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Job j = new Job();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setCompany(rs.getString("company"));
				j.setLocation(rs.getString("location"));
				j.setDescription(rs.getString("description"));
				j.setRequiredSkills(rs.getString("required_skills"));
				j.setSalary(rs.getString("salary"));
				j.setType(rs.getString("type"));
				j.setSource(rs.getString("source"));
				j.setUrl(rs.getString("url"));
				j.setCompanyLogo(rs.getString("company_logo"));
				j.setCreatedBy(rs.getInt("created_by"));
				j.setCreatedAt(rs.getString("created_at"));

				list.add(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// ---------- GET SINGLE JOB BY ID ----------
	public Job getJobById(int id) {
		Job j = null;
		String sql = "SELECT * FROM jobs WHERE id = ?";

		try (Connection con = DbUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				j = new Job();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setCompany(rs.getString("company"));
				j.setLocation(rs.getString("location"));
				j.setDescription(rs.getString("description"));
				j.setRequiredSkills(rs.getString("required_skills"));
				j.setSalary(rs.getString("salary"));
				j.setType(rs.getString("type"));
				j.setSource(rs.getString("source"));
				j.setUrl(rs.getString("url"));
				j.setCompanyLogo(rs.getString("company_logo"));
				j.setCreatedBy(rs.getInt("created_by"));
				j.setCreatedAt(rs.getString("created_at"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return j;
	}

	// ---------- UPDATE JOB ----------
	public boolean updateJob(Job job) {
		String sql = "UPDATE jobs SET title=?, company=?, location=?, description=?, required_skills=?, salary=?, type=?, source=?, url=?, company_logo=? "
				+ "WHERE id=?";

		try (Connection con = DbUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, job.getTitle());
			ps.setString(2, job.getCompany());
			ps.setString(3, job.getLocation());
			ps.setString(4, job.getDescription());
			ps.setString(5, job.getRequiredSkills());
			ps.setString(6, job.getSalary());
			ps.setString(7, job.getType());
			ps.setString(8, job.getSource());
			ps.setString(9, job.getUrl());
			ps.setString(10, job.getCompanyLogo());
			ps.setInt(11, job.getId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ---------- DELETE JOB ----------
	public boolean deleteJob(int id) {
		String sql = "DELETE FROM jobs WHERE id = ?";

		try (Connection con = DbUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public List<Job> searchJobs(String keyword, String company, String location, String type, Integer minSalary,
			Integer maxSalary, String dateRange, String sortBy) {

		List<Job> jobs = new ArrayList<>();

		StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE 1=1 ");

// ---- Dynamic Filters ----
		if (keyword != null && !keyword.isEmpty()) {
			sql.append(" AND (title LIKE ? OR description LIKE ? OR required_skills LIKE ?) ");
		}

		if (company != null && !company.isEmpty()) {
			sql.append(" AND company LIKE ? ");
		}

		if (location != null && !location.isEmpty()) {
			sql.append(" AND location LIKE ? ");
		}

		if (type != null && !type.isEmpty()) {
			sql.append(" AND type LIKE ? ");
		}

		if (minSalary != null) {
			sql.append(" AND salary >= ? ");
		}

		if (maxSalary != null) {
			sql.append(" AND salary <= ? ");
		}

// ---- Date Filters ----
		if ("24h".equals(dateRange)) {
			sql.append(" AND created_at >= NOW() - INTERVAL 1 DAY ");
		} else if ("7d".equals(dateRange)) {
			sql.append(" AND created_at >= NOW() - INTERVAL 7 DAY ");
		} else if ("30d".equals(dateRange)) {
			sql.append(" AND created_at >= NOW() - INTERVAL 30 DAY ");
		}

// ---- Sorting ----
		if ("salary_desc".equals(sortBy)) {
			sql.append(" ORDER BY salary DESC ");
		} else if ("salary_asc".equals(sortBy)) {
			sql.append(" ORDER BY salary ASC ");
		} else if ("newest".equals(sortBy)) {
			sql.append(" ORDER BY created_at DESC ");
		} else {
// default relevance (title + skills match)
			sql.append(" ORDER BY title LIKE ? DESC, required_skills LIKE ? DESC, created_at DESC ");
		}

		try (Connection con = DbUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

			int index = 1;

			if (keyword != null && !keyword.isEmpty()) {
				String kw = "%" + keyword + "%";
				ps.setString(index++, kw);
				ps.setString(index++, kw);
				ps.setString(index++, kw);
			}

			if (company != null && !company.isEmpty()) {
				ps.setString(index++, "%" + company + "%");
			}

			if (location != null && !location.isEmpty()) {
				ps.setString(index++, "%" + location + "%");
			}

			if (type != null && !type.isEmpty()) {
				ps.setString(index++, "%" + type + "%");
			}

			if (minSalary != null) {
				ps.setInt(index++, minSalary);
			}

			if (maxSalary != null) {
				ps.setInt(index++, maxSalary);
			}

// relevance parameters for ORDER BY
			if (sortBy == null) {
				String kw = "%" + (keyword == null ? "" : keyword) + "%";
				ps.setString(index++, kw);
				ps.setString(index++, kw);
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Job j = new Job();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setCompany(rs.getString("company"));
				j.setLocation(rs.getString("location"));
				j.setDescription(rs.getString("description"));
				j.setRequiredSkills(rs.getString("required_skills"));
				j.setSalary(rs.getInt("salary") + "");
				j.setType(rs.getString("type"));
				j.setSource(rs.getString("source"));
				j.setUrl(rs.getString("url"));
				j.setCompanyLogo(rs.getString("company_logo"));
				j.setCreatedBy(rs.getInt("created_by"));
				j.setCreatedAt(rs.getString("created_at"));

				jobs.add(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jobs;
	}
	public List<Job> recommendJobs(String skills) {
	    List<Job> list = new ArrayList<>();

	    if (skills == null || skills.trim().isEmpty()) {
	        return list;
	    }

	    // Split skills by comma
	    String[] skillArray = skills.split(",");

	    // Build dynamic SQL
	    StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE ");
	    for (int i = 0; i < skillArray.length; i++) {
	        sql.append("required_skills LIKE ?");
	        if (i < skillArray.length - 1) {
	            sql.append(" OR ");
	        }
	    }
	    sql.append(" ORDER BY created_at DESC");

	    try (Connection con = DbUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql.toString())) {

	        // Fill the placeholders
	        for (int i = 0; i < skillArray.length; i++) {
	            ps.setString(i + 1, "%" + skillArray[i].trim() + "%");
	        }

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Job j = new Job();
	            j.setId(rs.getInt("id"));
	            j.setTitle(rs.getString("title"));
	            j.setCompany(rs.getString("company"));
	            j.setLocation(rs.getString("location"));
	            j.setDescription(rs.getString("description"));
	            j.setRequiredSkills(rs.getString("required_skills"));
	            j.setSalary(rs.getString("salary"));
	            j.setType(rs.getString("type"));
	            j.setSource(rs.getString("source"));
	            j.setUrl(rs.getString("url"));
	            j.setCompanyLogo(rs.getString("company_logo"));
	            j.setCreatedBy(rs.getInt("created_by"));
	            j.setCreatedAt(rs.getString("created_at"));

	            list.add(j);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}


}
