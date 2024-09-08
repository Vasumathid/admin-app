package com.esewakendra.admin.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.esewakendra.admin.config.DBConfig;
import com.esewakendra.admin.service.BookingDetails;
import org.json.JSONObject;
import java.util.HashMap;

public class ServiceUtil {
    public Map<String, String> getServiceDetails(Connection conn, String serviceId, int bookingId) throws SQLException {
        Map<String, String> serviceDetails = new LinkedHashMap<>();
        String tableName = getTableNameForServiceById(serviceId);
        if (tableName != null) {
            String query = "SELECT * FROM " + tableName + " WHERE booking_id = ?";
            try (PreparedStatement serviceStmt = conn.prepareStatement(query)) {
                serviceStmt.setInt(1, bookingId);
                try (ResultSet rs = serviceStmt.executeQuery()) {
                    if (rs.next()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String value = rs.getString(columnName);
                            serviceDetails.put(columnName, value);
                        }
                    }
                }
            }
        }
        return serviceDetails;
    }

    public String formatColumnName(String columnName) {
        // Split the column name by underscores
        String[] words = columnName.split("_");

        // Capitalize the first letter of each word
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Trim the trailing space and return the result
        return formattedName.toString().trim();
    }

    public String getStateNameById(Connection conn, String stateId) throws SQLException {
        String stateName = null;
        String query = "SELECT name FROM states WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(stateId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stateName = rs.getString("name");
                }
            }
        }
        return stateName;
    }

    public String getDistrictNameById(Connection conn, String districtId) throws SQLException {
        String districtName = null;
        String query = "SELECT name FROM districts WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(districtId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    districtName = rs.getString("name");
                }
            }
        }
        return districtName;
    }

    public String getCourtComplexNameById(Connection conn, String courtComplexId) throws SQLException {
        String courtComplexName = null;
        String query = "SELECT name FROM court_complexes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(courtComplexId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    courtComplexName = rs.getString("name");
                }
            }
        }
        return courtComplexName;
    }

    public String getKendraNameById(Connection conn, String kendraId) throws SQLException {
        String kendraName = null;
        String query = "SELECT name FROM sewa_kendras WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(kendraId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kendraName = rs.getString("name");
                }
            }
        }
        return kendraName;
    }

    public String getTableNameForServiceById(String serviceId) throws SQLException {
        String tableName = null;
        String query = "SELECT service_table_name FROM services WHERE name = ?";
        DBConfig DBConfig = new DBConfig();
        try (Connection conn = DBConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tableName = rs.getString("service_table_name");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public String getTableNameForToken(String tokenNumber) throws SQLException {
        String tableName = null;
        String query = "SELECT s.service_table_name " +
                "FROM bookings b " +
                "JOIN services s ON b.service_id = s.id " +
                "WHERE b.token_number = ?";
        DBConfig dbConfig = new DBConfig();
        try (Connection conn = dbConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tokenNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tableName = rs.getString("service_table_name");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public BookingDetails getBookingDetailsByToken(String tokenNumber) throws SQLException {
        BookingDetails bookingDetails = null;

        String bookingDetailsQuery = "SELECT b.*, s.name AS state_name, d.name AS district_name, cc.name AS court_complex_name, sk.name AS sewa_kendra_name, sv.name AS service_name "
                +
                "FROM bookings b " +
                "JOIN states s ON b.state_id = s.id " +
                "JOIN districts d ON b.district_id = d.id " +
                "JOIN court_complexes cc ON b.court_complex_id = cc.id " +
                "JOIN sewa_kendras sk ON b.kendra_id = sk.id " +
                "JOIN services sv ON b.service_id = sv.id " +
                "WHERE b.token_number = ?";

        // DBConfig dbConfig = new DBConfig();
        try (Connection conn = DBConfig.getConnection();
                PreparedStatement bookingDetailsStmt = conn.prepareStatement(bookingDetailsQuery)) {
            bookingDetailsStmt.setString(1, tokenNumber);
            try (ResultSet bookingDetailsRs = bookingDetailsStmt.executeQuery()) {
                if (bookingDetailsRs.next()) {
                    bookingDetails = new BookingDetails();

                    // Set values, checking for nulls and handling optional fields
                    bookingDetails.setId(bookingDetailsRs.getInt("id"));

                    // Set state, district, court complex, kendra, and service names
                    bookingDetails.setState(
                            bookingDetailsRs.getString("state_name") != null ? bookingDetailsRs.getString("state_name")
                                    : "");
                    bookingDetails.setDistrict(
                            bookingDetailsRs.getString("district_name") != null
                                    ? bookingDetailsRs.getString("district_name")
                                    : "");
                    bookingDetails.setCourtComplex(
                            bookingDetailsRs.getString("court_complex_name") != null
                                    ? bookingDetailsRs.getString("court_complex_name")
                                    : "");
                    bookingDetails.setKendra(
                            bookingDetailsRs.getString(
                                    "sewa_kendra_name") != null
                                            ? bookingDetailsRs.getString(
                                                    "sewa_kendra_name")
                                            : "");
                    bookingDetails.setService(
                            bookingDetailsRs.getString("service_name") != null
                                    ? bookingDetailsRs.getString("service_name")
                                    : "");

                    bookingDetails.setAdvocateName(
                            bookingDetailsRs.getString("advocate_name") != null
                                    ? bookingDetailsRs.getString("advocate_name")
                                    : "");
                    bookingDetails.setEnrollmentNumber(bookingDetailsRs.getString("enrollment_number"));
                    bookingDetails.setPhoneNumber(bookingDetailsRs.getString("phone_number"));
                    bookingDetails.setEmail(
                            bookingDetailsRs.getString("email") != null ? bookingDetailsRs.getString("email") : "");
                    bookingDetails.setStatus(bookingDetailsRs.getString("status"));

                    bookingDetails.setTokenNumber(bookingDetailsRs.getString("token_number"));
                    bookingDetails.setBookingTime(bookingDetailsRs.getTimestamp("booking_time"));
                    bookingDetails.setIsAdvocate(bookingDetailsRs.getBoolean("isAdvocate")); // Assuming

                    bookingDetails.setDate(
                            bookingDetailsRs.getDate("date") != null ? bookingDetailsRs.getDate("date") : null);
                    bookingDetails.setTimeSlot(
                            bookingDetailsRs.getString("time_slot") != null ? bookingDetailsRs.getString("time_slot")
                                    : "");
                    bookingDetails.setModifiedTime(
                            bookingDetailsRs.getTimestamp("modified_time") != null
                                    ? bookingDetailsRs.getTimestamp("modified_time")
                                    : null);
                    bookingDetails.setTimeRange(
                            bookingDetailsRs.getString("time_range") != null ? bookingDetailsRs.getString("time_range")
                                    : "");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database connection error", e);
        }

        return bookingDetails;

    }

    public String getServiceTableDetailsByName(String tableName, int bookingId) throws SQLException {
        String serviceDetailsQuery = "SELECT * FROM " + tableName + " where booking_id ='" + bookingId + "'";
        DBConfig dbConfig = new DBConfig();
        JSONObject jsonResult = new JSONObject();

        try (Connection conn = dbConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(serviceDetailsQuery);
                ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    if (!columnName.equals("booking_id"))
                        rowMap.put(columnName, columnValue);
                }
                jsonResult.put("ServiceDetails", new JSONObject(rowMap));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database connection error", e);
        }

        return jsonResult.toString();
    }

}
