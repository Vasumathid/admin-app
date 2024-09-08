package com.esewakendra.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.esewakendra.admin.config.DBConfig;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = null;

        try {
            // Connect to the database using DBConfig
            Connection conn = DBConfig.getConnection();

            // Prepare the SQL statement
            String sql = "SELECT r.role_name FROM users u JOIN roles r ON u.role_id = r.id WHERE u.username=? AND u.password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("role_name");
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                // Redirect to the appropriate dashboard
                if ("System Officer".equals(role)) {
                    response.sendRedirect("dist/pages/system_officer_dashboard.html");
                } else if ("E-Sewa User".equals(role)) {
                    response.sendRedirect("dist/pages/e_sewa_dashboard.html");
                } else if ("High Court User".equals(role)) {
                    response.sendRedirect("dist/pages/high_court_dashboard.html");
                }
            } else {
                response.sendRedirect("login.html?error=1");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=2");
        }
    }
}
