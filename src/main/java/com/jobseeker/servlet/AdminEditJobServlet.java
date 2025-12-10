package com.jobseeker.servlet;

import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/job/edit")
public class AdminEditJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        // Admin check
        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        int jobId = Integer.parseInt(req.getParameter("jobId"));

        Job job = new Job();
        job.setId(jobId);
        job.setTitle(req.getParameter("title"));
        job.setCompany(req.getParameter("company"));
        job.setLocation(req.getParameter("location"));
        job.setDescription(req.getParameter("description"));
        job.setRequiredSkills(req.getParameter("skills"));
        job.setSalary(req.getParameter("salary"));
        job.setType(req.getParameter("type"));
        job.setSource("Admin");
        job.setUrl(req.getParameter("url"));
        job.setCompanyLogo(req.getParameter("logo"));

        JobDAO dao = new JobDAO();
        boolean updated = dao.updateJob(job);

        if (updated) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Job updated\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to update\"}");
        }
    }
}
