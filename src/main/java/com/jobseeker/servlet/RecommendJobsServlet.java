package com.jobseeker.servlet;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.jobseeker.dao.JobDAO;
import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Job;
import com.jobseeker.model.Profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/api/jobs/recommend")
public class RecommendJobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        // User must be logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.getWriter().write("{\"error\":\"NOT_LOGGED_IN\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // Fetch profile to get user skills
        ProfileDAO pdao = new ProfileDAO();
        Profile pf = pdao.findByUserId(userId);

        if (pf == null || pf.getSkills() == null || pf.getSkills().trim().isEmpty()) {
            resp.getWriter().write("{\"error\":\"NO_SKILLS\"}");
            return;
        }

        String skills = pf.getSkills();

        JobDAO dao = new JobDAO();
        List<Job> recommended = dao.recommendJobs(skills);

        // Convert to JSON
        resp.getWriter().write(gson.toJson(recommended));
    }
}
