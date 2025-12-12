package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.AlertDAO;
import com.jobseeker.dao.JobDAO;
import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Job;
import com.jobseeker.model.Profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/alerts")
public class JobAlertServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        // 1) Check user login
        if (session == null || session.getAttribute("userId") == null) {
            resp.getWriter().write("{\"error\":\"NOT_LOGGED_IN\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // 2) Check if alerts are enabled
        AlertDAO alertDao = new AlertDAO();
        boolean enabled = alertDao.isEnabled(userId);

        if (!enabled) {
            resp.getWriter().write("{\"alerts\":\"DISABLED\"}");
            return;
        }

        // 3) Fetch user profile to read skills
        ProfileDAO profileDao = new ProfileDAO();
        Profile profile = profileDao.findByUserId(userId);

        if (profile == null || profile.getSkills() == null) {
            resp.getWriter().write("{\"alerts\":\"NO_PROFILE\"}");
            return;
        }

        String skillString = profile.getSkills().toLowerCase();

        // 4) Fetch all recent jobs (last 7 days)
        JobDAO jobDao = new JobDAO();
        List<Job> recentJobs = jobDao.getRecentJobs(7);

        List<Job> matchingJobs = new ArrayList<>();

        // 5) Match jobs by skill keyword
        for (Job job : recentJobs) {
            if (job.getRequiredSkills() == null) continue;

            String jobSkills = job.getRequiredSkills().toLowerCase();

            // Check if skills intersect
            for (String s : skillString.split(",")) {
                s = s.trim();
                if (!s.isEmpty() && jobSkills.contains(s)) {
                    matchingJobs.add(job);
                    break;
                }
            }
        }

        // 6) Return matching jobs
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(matchingJobs));
    }
}
