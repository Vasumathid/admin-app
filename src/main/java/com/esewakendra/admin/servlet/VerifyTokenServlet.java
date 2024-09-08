package com.esewakendra.admin.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.esewakendra.admin.config.DBConfig;

@WebServlet("/verifyToken")
public class VerifyTokenServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tokenNumber = request.getParameter("tokenNumber");
        response.setContentType("text/plain"); // Setting response type to plain text for simplicity
        
        try {
            // Database connection
            Connection conn = DBConfig.getConnection(); 

            // First, check if the token number exists and is not cancelled
            String checkQuery = "SELECT COUNT(*) AS count FROM bookings WHERE token_number = ? AND status <> 'Cancelled'";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, tokenNumber);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            rs.close();
            checkStmt.close();

            if (count == 0) {
                // Token number does not exist or is cancelled
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Token number does not exist or is already cancelled.");
                return;
            }

            // Proceed to update the status to 'verified'
            String updateQuery = "UPDATE bookings SET status = 'verified' WHERE token_number = ? AND status <> 'Cancelled'";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, tokenNumber);

            int rowsUpdated = updateStmt.executeUpdate();
            updateStmt.close();
            conn.close();

            if (rowsUpdated > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Token Verified Successfully!");
            } else {
                // This should not happen, as the check above guarantees the token exists
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Failed to verify the token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the request.");
        }
    }
}
