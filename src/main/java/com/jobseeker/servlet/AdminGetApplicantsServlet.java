package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.ApplicationDAO;
import com.jobseeker.model.Application;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/admin/applicants")
public class AdminGetApplicantsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1) Check admin login
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null ||
            !session.getAttribute("role").equals("ADMIN")) {

            resp.getWriter().write("{\"error\":\"ADMIN_ONLY\"}");
            return;
        }

        // 2) Read jobId
        String jobIdStr = req.getParameter("jobId");
        if (jobIdStr == null) {
            resp.getWriter().write("{\"error\":\"MISSING_JOB_ID\"}");
            return;
        }

        int jobId = Integer.parseInt(jobIdStr);

        // 3) DAO call
        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applicants = dao.getApplicantsForJob(jobId);

        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(applicants));
    }
}
