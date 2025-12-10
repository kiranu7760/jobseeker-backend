package com.jobseeker.servlet;

import com.google.gson.Gson;
import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/search")
public class SearchJobsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // -------- Read Parameters --------
        String keyword = req.getParameter("keyword");
        String company = req.getParameter("company");
        String location = req.getParameter("location");
        String type = req.getParameter("type");

        // Salary range (optional)
        Integer minSalary = null;
        Integer maxSalary = null;

        try {
            if (req.getParameter("minSalary") != null && !req.getParameter("minSalary").isEmpty()) {
                minSalary = Integer.parseInt(req.getParameter("minSalary"));
            }
            if (req.getParameter("maxSalary") != null && !req.getParameter("maxSalary").isEmpty()) {
                maxSalary = Integer.parseInt(req.getParameter("maxSalary"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Date filter (24h, 7d, 30d)
        String dateRange = req.getParameter("dateRange");

        // Sorting (newest, salary_desc, salary_asc)
        String sortBy = req.getParameter("sortBy");

        // -------- DAO Call --------
        JobDAO dao = new JobDAO();
        List<Job> jobs = dao.searchJobs(keyword, company, location, type, minSalary, maxSalary, dateRange, sortBy);

        // -------- Return JSON --------
        Gson gson = new Gson();
        String json = gson.toJson(jobs);

        resp.getWriter().write(json);
    }
}
