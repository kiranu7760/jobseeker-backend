package com.jobseeker.servlet;

import java.io.IOException;

import com.jobseeker.dao.ApplicationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/withdraw")
public class WithdrawServlet extends HttpServlet {
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
        int jobId = Integer.parseInt(req.getParameter("jobId"));

        ApplicationDAO dao = new ApplicationDAO();
        boolean removed = dao.withdraw(userId, jobId);

        if (removed)
            resp.getWriter().write("{\"status\":\"WITHDRAWN\"}");
        else
            resp.getWriter().write("{\"status\":\"FAILED\"}");
    }
}

