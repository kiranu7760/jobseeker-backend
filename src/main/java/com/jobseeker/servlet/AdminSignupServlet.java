package com.jobseeker.servlet;

import com.jobseeker.dao.AdminDAO;
import com.jobseeker.model.Admin;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/admin/signup")
public class AdminSignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (name == null || email == null || password == null ||
                name.isEmpty() || email.isEmpty() || password.isEmpty()) {

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Missing fields\"}");
            return;
        }

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPasswordHash(hashed);

        AdminDAO dao = new AdminDAO();
        boolean success = dao.insertAdmin(admin);

        if (success) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"Admin registered successfully\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Failed or email exists\"}");
        }
    }
}
