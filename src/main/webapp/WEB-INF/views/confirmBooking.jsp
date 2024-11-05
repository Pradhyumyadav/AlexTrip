<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%
    String hotelId = request.getParameter("hotelId");
    String checkin = request.getParameter("checkin");
    String checkout = request.getParameter("checkout");
    String roomType = request.getParameter("roomType");
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    // Database connection parameters
    String url = "jdbc:postgresql://localhost:5432/AlexTrip";
    String user = "postgresql";
    String password = "123";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      try {
        Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);

