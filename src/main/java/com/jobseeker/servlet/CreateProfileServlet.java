package com.jobseeker.servlet;

import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Profile;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/profile/create")
public class CreateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String headline = req.getParameter("headline");
            String summary = req.getParameter("summary");
            String skills = req.getParameter("skills");
            String resumePath = req.getParameter("resumePath");
            String experience = req.getParameter("experience");
            String education = req.getParameter("education");
            String resumeText = req.getParameter("resumeText");
            String linkedinUrl = req.getParameter("linkedinUrl");

            // Basic validation
            if (userId <= 0) {
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"userId required\"}");
                return;
            }

            Profile p = new Profile();
            p.setUserId(userId);
            p.setHeadline(headline);
            p.setSummary(summary);
            p.setSkills(skills);
            p.setResumePath(resumePath);
            p.setExperience(experience);
            p.setEducation(education);
            p.setResumeText(resumeText);
            p.setLinkedinUrl(linkedinUrl);

            ProfileDAO dao = new ProfileDAO();
            boolean ok = dao.saveOrUpdate(p);

            if (ok) {
                resp.getWriter().write("{\"status\":\"success\",\"message\":\"Profile saved\"}");
            } else {
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"Could not save profile\"}");
            }

        } catch (NumberFormatException nfe) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid userId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Server error\"}");
        }
    }
}
