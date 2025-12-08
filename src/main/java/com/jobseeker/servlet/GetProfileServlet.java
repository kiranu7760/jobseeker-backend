package com.jobseeker.servlet;

import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Profile;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/profile/get")
public class GetProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String userIdStr = req.getParameter("userId");
            if (userIdStr == null) {
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"userId required\"}");
                return;
            }
            int userId = Integer.parseInt(userIdStr);

            ProfileDAO dao = new ProfileDAO();
            Profile p = dao.findByUserId(userId);

            if (p == null) {
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"Profile not found\"}");
                return;
            }

            // Build JSON manually (simple)
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"status\":\"success\",");
            sb.append("\"userId\":").append(p.getUserId()).append(",");
            sb.append("\"headline\":").append(quote(p.getHeadline())).append(",");
            sb.append("\"summary\":").append(quote(p.getSummary())).append(",");
            sb.append("\"skills\":").append(quote(p.getSkills())).append(",");
            sb.append("\"resumePath\":").append(quote(p.getResumePath())).append(",");
            sb.append("\"experience\":").append(quote(p.getExperience())).append(",");
            sb.append("\"education\":").append(quote(p.getEducation())).append(",");
            sb.append("\"resumeText\":").append(quote(p.getResumeText())).append(",");
            sb.append("\"linkedinUrl\":").append(quote(p.getLinkedinUrl()));
            sb.append("}");

            resp.getWriter().write(sb.toString());

        } catch (NumberFormatException nfe) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid userId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Server error\"}");
        }
    }

    private String quote(String s) {
        if (s == null) return "null";
        String escaped = s.replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }
}
