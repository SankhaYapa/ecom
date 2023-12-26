package com.app.servlets;

import com.app.controller.UserController;
import com.app.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Extract login parameters from the request
        String username = req.getParameter("Username");
        String password = req.getParameter("Password");

        // Login the user using the UserController
        UserController userController = new UserController();
        User loggedInUser = userController.loginUser(username, password);

        if (loggedInUser != null) {
            // Login successful
            // Convert user data to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(loggedInUser);

            // Save JSON to local storage
            resp.getWriter().write("<script>localStorage.setItem('userData', '" + jsonString + "');</script>");

            // Redirect to the home page or a dashboard
            resp.sendRedirect("index.html");
        } else {
            // Login failed, handle the error
            resp.sendRedirect("index.html");
        }
    }
}
