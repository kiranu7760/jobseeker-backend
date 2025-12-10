package com.jobseeker.servlet;

import com.jobseeker.dao.JobDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/job/delete")
public class AdminDeleteJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        int jobId = Integer.parseInt(req.getParameter("jobId"));

        JobDAO dao = new JobDAO();
        boolean deleted = dao.deleteJob(jobId);

        if (deleted) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Job deleted\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Delete failed\"}");
        }
    }
}
