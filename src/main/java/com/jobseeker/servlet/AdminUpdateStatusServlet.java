package com.jobseeker.servlet;

import com.jobseeker.dao.ApplicationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/update-status")
public class AdminUpdateStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null ||
                !session.getAttribute("role").equals("ADMIN")) {

            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        int applicationId = Integer.parseInt(req.getParameter("applicationId"));
        String status = req.getParameter("status");

        ApplicationDAO dao = new ApplicationDAO();
        boolean success = dao.updateStatus(applicationId, status);

        if (success) {
            resp.getWriter().write("{\"status\":\"success\", \"message\":\"Status updated\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Update failed\"}");
        }
    }
}

