package com.jobseeker.servlet;

import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/api/job")
public class GetJobByIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));

        JobDAO dao = new JobDAO();
        Job job = dao.getJobById(id);

        Gson gson = new Gson();
        String json = gson.toJson(job);

        resp.getWriter().write(json);
    }
}
