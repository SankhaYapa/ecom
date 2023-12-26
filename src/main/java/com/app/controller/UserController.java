package com.app.controller;

import com.app.db.HibernateUtil;
import com.app.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UserController {
    public boolean registerUser(User user) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        return true;
    }
    public User loginUser(String username, String password) {
        try (Session session = HibernateUtil.getSession()) {
            // Implement login logic here
            // Check if the provided username and password match any user in the database
            User user = (User) session.createQuery("FROM User WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            return user; // Returns null if no user is found
        }
    }

    public User getUserData(String username) {
        // Retrieve user data by username
        try (Session session = HibernateUtil.getSession()) {
            Query query = session.createQuery("FROM User WHERE username = :username");
            query.setParameter("username", username);

            User user = (User) query.getSingleResult();

            // Create a UserData object and populate it with user information
            User userData = new User();
            userData.setUsername(user.getUsername());
            userData.setEmail(user.getEmail());
            userData.setPassword(user.getPassword());
            userData.setPhone(user.getPhone());

            return userData;
        } catch (NoResultException e) {
            // User not found
            return null;
        }
    }
}
