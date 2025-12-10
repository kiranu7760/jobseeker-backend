package com.jobseeker.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.jobseeker.dao.ApplicationDAO;
import com.jobseeker.dao.ProfileDAO;
import com.jobseeker.model.Profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/apply")
public class ApplyJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        // --------------------------
        // 1️⃣ Check Login
        // --------------------------
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            out.write(gson.toJson("NOT_LOGGED_IN"));
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // --------------------------
        // 2️⃣ Read jobId
        // --------------------------
        int jobId = Integer.parseInt(req.getParameter("jobId"));

        // --------------------------
        // 3️⃣ Get resume path from profile
        // --------------------------
        ProfileDAO profileDao = new ProfileDAO();
        Profile pf = profileDao.findByUserId(userId);

        if (pf == null || pf.getResumePath() == null || pf.getResumePath().trim().isEmpty()) {
            out.write(gson.toJson("NO_RESUME"));
            return;
        }

        String resumePath = pf.getResumePath();

        // --------------------------
        // 4️⃣ Check if already applied
        // --------------------------
        ApplicationDAO appDao = new ApplicationDAO();
        boolean exists = appDao.hasAlreadyApplied(userId, jobId);

        if (exists) {
            out.write(gson.toJson("ALREADY_APPLIED"));
            return;
        }

        // --------------------------
        // 5️⃣ Apply now
        // --------------------------
        boolean success = appDao.apply(userId, jobId, resumePath);

        if (success) {
            out.write(gson.toJson("APPLIED_SUCCESS"));
        } else {
            out.write(gson.toJson("APPLY_FAILED"));
        }
    }
}
