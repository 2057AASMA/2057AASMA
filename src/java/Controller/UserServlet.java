package Controller;

import Hashing.HashingPassword;
import Model.User;
import Service.UserService;
import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;

/**
 */
@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("page");
        System.out.println(action);

        if (action.equalsIgnoreCase("login"))

        {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username + " " + password + " ");

            User user = new UserService().getUser(username, password);
//            System.out.println(user.getUsername()+" "+user.getPassword());
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("uid", user.getId());
                session.setAttribute("full_name", user.getFull_name());
                session.setAttribute("user", user);
                //                    System.out.println(session.getAttribute("user"));
                request.setAttribute("msg", "Login Successful!");
                System.out.println(request.getAttribute("msg"));

                RequestDispatcher rd = request.getRequestDispatcher("Pages/dashboard.html");
                rd.forward(request, response);
            } else {
                request.setAttribute("msg", "Invalid username or password");
                RequestDispatcher rd = request.getRequestDispatcher("Pages/login.html");
                rd.forward(request, response);
            }
        }


        //To redirect in Register Page
        if (action.equalsIgnoreCase("newUsers"))

        {
            RequestDispatcher rd = request.getRequestDispatcher("Pages/login.html");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("userDetails")) {

            int id = Integer.parseInt(request.getParameter("id"));
            User user = new UserService().getUserRow(id);
            request.setAttribute("id", id);
            request.setAttribute("user", user);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/user_details.jsp");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("register"))

        {
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setPassword(HashingPassword.hashPassword(request.getParameter("password")));
            new UserService().insertUser(user);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/login.jsp");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("index"))

        {
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.forward(request, response);
        }


        if (action.equalsIgnoreCase("logout"))

        {
            HttpSession session = request.getSession(false);
            session.invalidate();
            request.setAttribute("msg", "Logout Success");
            System.out.println(request.getAttribute("msg"));
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("addUser"))

        {
            User user = new User();
            user.setFull_name(request.getParameter("full_name"));
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setRole(request.getParameter("role"));
            new UserService().insertUser(user);
            List<User> userList = new UserService().getUserList();
            request.setAttribute("userList", userList);
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.forward(request, response);
        }

        
        if (action.equalsIgnoreCase("listUser"))

        {
            User user = new User();
            List<User> userList = new UserService().getUserList();
            request.setAttribute("userList", userList);
            request.setAttribute("user", user);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/list_user.jsp");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("home"))

        {
            RequestDispatcher rd = request.getRequestDispatcher("Pages/dashboard.jsp");
            rd.forward(request, response);
        }
        if (action.equalsIgnoreCase("deleteUser"))

        {
            int id = Integer.parseInt(request.getParameter("id"));
            UserService userService = new UserService();
            userService.deleteUser(id);
            List<User> userList = new UserService().getUserList();
            request.setAttribute("userList", userList);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/list_user.jsp");
            rd.forward(request, response);
        }

        if (action.equalsIgnoreCase("userEdit"))

        {
            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println(id);
            User user = new UserService().getUserRow(id);
            request.setAttribute("id", id);
            request.setAttribute("user", user);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/update_user.jsp");
            rd.forward(request, response);
        }
//
        if (action.equalsIgnoreCase("editUser"))

        {
            User user = new User();
            int id = Integer.parseInt(request.getParameter("id"));
            user.setFull_name(request.getParameter("full_name"));
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setRole(request.getParameter("role"));
            try {
                new UserService().editUser(id, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<User> userList = new UserService().getUserList();
            request.setAttribute("userList", userList);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/list_user.jsp");
            rd.forward(request, response);
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
