package com.jobseeker.servlet;

import com.jobseeker.dao.AlertDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/alerts/disable")
public class DisableAlertsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.getWriter().write("{\"error\":\"NOT_LOGGED_IN\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        AlertDAO dao = new AlertDAO();
        boolean success = dao.disableAlerts(userId);

        if (success) {
            resp.getWriter().write("{\"status\":\"disabled\"}");
        } else {
            resp.getWriter().write("{\"status\":\"failed\"}");
        }
    }
}

