package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.ApplicationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/job-stats")
public class AdminJobStatsServlet extends HttpServlet {

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

        ApplicationDAO dao = new ApplicationDAO();
        List<Map<String, Object>> stats = dao.getApplicationsPerJob();

        resp.getWriter().write(new Gson().toJson(stats));
    }
}
