package com.jobseeker.servlet;

import com.jobseeker.dao.UserDAO;
import com.jobseeker.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Enable JSON response format
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1. Collect form data
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // 2. Validate (basic)
        if (name == null || email == null || password == null ||
            name.isEmpty() || email.isEmpty() || password.isEmpty()) {

            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Missing fields\"}");
            return;
        }

        // 3. Hash password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 4. Create User object
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setRole("USER");

        // 5. Save user using DAO
        UserDAO dao = new UserDAO();
        boolean saved = dao.save(user);

        // 6. Return JSON response
        if (saved) {
            resp.getWriter().write("{\"status\":\"success\",\"message\":\"User registered successfully\"}");
        } else {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Email may already exist\"}");
        }
    }
}
