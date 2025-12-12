package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.ApplicationDAO;
import com.jobseeker.dao.JobDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/admin/stats")
public class AdminStatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        ApplicationDAO appDao = new ApplicationDAO();
        JobDAO jobDao = new JobDAO();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", jobDao.getTotalJobs());
        stats.put("totalApplications", appDao.getTotalApplications());
        stats.put("todayApplications", appDao.getTodayApplications());
        stats.put("statusSummary", appDao.getStatusSummary());

        resp.getWriter().write(new Gson().toJson(stats));
    }
}
