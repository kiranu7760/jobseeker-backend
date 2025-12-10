package com.jobseeker.servlet;

import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/jobs")

public class GetJobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JobDAO dao = new JobDAO();
        List<Job> jobs = dao.getAllJobs();

        Gson gson = new Gson();
        String json = gson.toJson(jobs);

        resp.getWriter().write(json);
    }
}
