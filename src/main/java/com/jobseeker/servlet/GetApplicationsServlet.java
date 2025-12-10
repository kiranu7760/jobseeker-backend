package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.ApplicationDAO;
import com.jobseeker.model.Application;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/applications")
public class GetApplicationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1) Check login
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.getWriter().write("{\"error\":\"NOT_LOGGED_IN\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // 2) Fetch applications from database
        ApplicationDAO dao = new ApplicationDAO();
        List<Application> list = dao.getApplicationsByUser(userId);

        // 3) Return JSON
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(list));
    }
}
