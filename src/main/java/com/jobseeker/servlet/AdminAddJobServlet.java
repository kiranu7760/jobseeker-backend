package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/job/add")
public class AdminAddJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        // Check admin session
        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        int adminId = (int) session.getAttribute("adminId");

        // Read job data
        Job job = new Job();
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
        job.setCreatedBy(adminId);

        JobDAO dao = new JobDAO();
        boolean added = dao.addJob(job);

        if (added) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Job added\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to add job\"}");
        }
    }
}
