package com.jobseeker.servlet;

import com.jobseeker.dao.AdminDAO;
import com.jobseeker.model.Admin;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/login")
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        AdminDAO dao = new AdminDAO();
        Admin admin = dao.findByEmail(email);

        if (admin == null) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Admin not found\"}");
            return;
        }

        boolean match = BCrypt.checkpw(password, admin.getPasswordHash());
        if (!match) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid credentials\"}");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("adminId", admin.getId());
        session.setAttribute("role", "ADMIN");

        resp.getWriter().write("{\"status\":\"success\",\"message\":\"Admin login successful\"}");
    }
}
