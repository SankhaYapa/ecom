package com.app.servlets;

import com.app.controller.UserController;
import com.app.entity.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/register")
public class UserRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Extract user registration parameters from the request
        String username = req.getParameter("Username");
        String password = req.getParameter("Password");
        String email = req.getParameter("Email");
        String phone = req.getParameter("Phone");

        // Create a new User instance
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);

        // Register the user using the UserController
        UserController userController = new UserController();
        if (userController.registerUser(user)) {
            // Registration successful, send a confirmation email
            sendConfirmationEmail(user);

            // Redirect to a success page
            resp.sendRedirect("index.html");
        } else {
            // Registration failed, handle the error
            resp.sendRedirect("index.html");
        }
    }

    private void sendConfirmationEmail(User user) {
        // Setup mail server properties for Mailtrap
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.mailtrap.io");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        // Mailtrap credentials (replace with your Mailtrap credentials)
        final String username = "your-mailtrap-username";
        final String password = "your-mailtrap-password";

        // Get the default Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(username));

            // Set To: header field of the header
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));

            // Set Subject: header field
            message.setSubject("Registration Confirmation");

            // Now set the actual message
            message.setText("Dear " + user.getUsername() + ",\n\nThank you for registering with us!");

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully...");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
