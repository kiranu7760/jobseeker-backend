package com.jobseeker.servlet;

import com.jobseeker.dao.UserDAO;
import com.jobseeker.model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/user/login")

public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

	    resp.setContentType("application/json");
	    resp.setCharacterEncoding("UTF-8");

	    // 1. Read request data
	    String email = req.getParameter("email");
	    String password = req.getParameter("password");

	    // 2. Validate (basic)
	    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
	        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Missing fields\"}");
	        return;
	    }

	    // 3. Fetch user using DAO
	    UserDAO dao = new UserDAO();
	    User user = dao.findByEmail(email);

	    if (user == null) {
	        resp.getWriter().write("{\"status\":\"error\", \"message\":\"User not found\"}");
	        return;
	    }

	    // 4. Check password using BCrypt
	    boolean passwordMatch = BCrypt.checkpw(password, user.getPasswordHash());

	    if (!passwordMatch) {
	        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid credentials\"}");
	        return;
	    }

	    // 5. Login success -> create session and set attributes (this makes container send Set-Cookie)
	    HttpSession session = req.getSession(true);     // create session if none
	    session.setAttribute("userId", user.getId());
	    session.setAttribute("name", user.getName());
	    session.setAttribute("role", "USER");

	    // optional: set session timeout (seconds). e.g. 30 minutes:
	    session.setMaxInactiveInterval(30 * 60);

	    // return JSON (unchanged)
	    resp.getWriter().write(
	        "{\"status\":\"success\", " +
	        "\"message\":\"Login successful\", " +
	        "\"name\":\"" + user.getName() + "\", " +
	        "\"email\":\"" + user.getEmail() + "\"}"
	    );
	}

}

