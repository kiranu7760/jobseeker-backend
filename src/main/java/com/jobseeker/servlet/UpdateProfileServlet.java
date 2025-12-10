package com.jobseeker.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/api/profile/update")
@MultipartConfig
public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // --------- Check login ----------
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Not logged in\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // --------- Receive text fields ----------
        String headline = req.getParameter("headline");
        String summary = req.getParameter("summary");
        String skills = req.getParameter("skills");
        String experience = req.getParameter("experience");
        String education = req.getParameter("education");
        String linkedin = req.getParameter("linkedin");

        // --------- File upload handling ----------
        Part filePart = req.getPart("resume"); 
        String resumePath = null;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("") + "uploads/resumes/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String finalPath = uploadDir + System.currentTimeMillis() + "_" + fileName;
            filePart.write(finalPath);

            resumePath = "uploads/resumes/" + System.currentTimeMillis() + "_" + fileName;
        }

        // --------- Build profile object ----------
        Profile p = new Profile();
        p.setUserId(userId);
        p.setHeadline(headline);
        p.setSummary(summary);
        p.setSkills(skills);
        p.setExperience(experience);
        p.setEducation(education);
        p.setLinkedinUrl(linkedin);

        if (resumePath != null) {
            p.setResumePath(resumePath);
        }

        // --------- Save to DB ----------
        ProfileDAO dao = new ProfileDAO();
        boolean success = dao.saveOrUpdate(p);

        if (success) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Profile updated\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Database error\"}");
        }
    }
}
