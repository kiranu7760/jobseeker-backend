package com.jobseeker.servlet;

import com.jobseeker.dao.JobDAO;
import com.jobseeker.model.Job;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/admin/job/add")
public class AddJobServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Collect form data
        String title = req.getParameter("title");
        String company = req.getParameter("company");
        String location = req.getParameter("location");
        String description = req.getParameter("description");
        String requiredSkills = req.getParameter("required_skills");
        String salary = req.getParameter("salary");
        String type = req.getParameter("type"); // full-time, part-time, remote etc
        String source = req.getParameter("source"); // internal or API
        String url = req.getParameter("url"); // external job link
        String companyLogo = req.getParameter("company_logo");

        Job job = new Job();
        job.setTitle(title);
        job.setCompany(company);
        job.setLocation(location);
        job.setDescription(description);
        job.setRequiredSkills(requiredSkills);
        job.setSalary(salary);
        job.setType(type);
        job.setSource(source);
        job.setUrl(url);
        job.setCompanyLogo(companyLogo);

        JobDAO dao = new JobDAO();
        boolean success = dao.addJob(job);

        PrintWriter out = resp.getWriter();
        if (success) {
            out.print("{\"status\":\"success\",\"message\":\"Job added successfully\"}");
        } else {
            out.print("{\"status\":\"error\",\"message\":\"Job addition failed\"}");
        }
    }
}
