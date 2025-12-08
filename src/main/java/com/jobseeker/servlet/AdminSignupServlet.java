package com.jobseeker.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.mindrot.jbcrypt.BCrypt;

import com.jobseeker.dao.AdminDAO;
import com.jobseeker.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/signup")
public class AdminSignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        Admin a = new Admin();
        a.setName(name);
        a.setEmail(email);
        a.setPassword(hashed);
        a.setRole("ADMIN");

        AdminDAO dao = new AdminDAO();
        boolean success = dao.registerAdmin(a);

        PrintWriter out = resp.getWriter();
        if (success) {
            out.print("{\"status\":\"success\",\"message\":\"Admin registered\"}");
        } else {
            out.print("{\"status\":\"error\",\"message\":\"Failed to register admin\"}");
        }
    }
}

