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

@WebServlet("/api/admin/login")
public class AdminLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        AdminDAO dao = new AdminDAO();
        Admin a = dao.login(email, password);

        PrintWriter out = resp.getWriter();

        if (a == null) {
            out.print("{\"status\":\"error\",\"message\":\"Admin not found\"}");
            return;
        }

        if (!BCrypt.checkpw(password, a.getPassword())) {
            out.print("{\"status\":\"error\",\"message\":\"Incorrect password\"}");
            return;
        }

        out.print("{\"status\":\"success\",\"message\":\"Login successful\",\"admin\":\""
                + a.getName() + "\"}");
    }
}
