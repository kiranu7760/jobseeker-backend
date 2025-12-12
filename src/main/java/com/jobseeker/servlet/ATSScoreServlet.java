package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.JobDAO;
import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Job;
import com.jobseeker.model.Profile;
import com.jobseeker.util.ATSUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/ats-score")
public class ATSScoreServlet extends HttpServlet {

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
        String jobIdStr = req.getParameter("jobId");

        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            resp.getWriter().write("{\"error\":\"MISSING_JOB_ID\"}");
            return;
        }

        int jobId = Integer.parseInt(jobIdStr);


        ProfileDAO profileDao = new ProfileDAO();
        Profile p = profileDao.findByUserId(userId);

        JobDAO jobDao = new JobDAO();
        Job job = jobDao.getJobById(jobId);

        if (p == null || job == null) {
            resp.getWriter().write("{\"error\":\"DATA_MISSING\"}");
            return;
        }

        Map<String, Object> atsScore = ATSUtil.calculateATSScore(
                p.getSkills(),
                p.getResumeText(),
                job.getRequiredSkills(),
                job.getDescription()
        );

        resp.getWriter().write(new Gson().toJson(atsScore));
    }
}
