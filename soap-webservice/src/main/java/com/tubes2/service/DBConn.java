package com.tubes2.service;

import java.sql.*;

public class DBConn {
    private static final String endpoint = "tubes2-soap-db:3306";
    private static final String database = "soap_db";
    private static final String username = "root";
    private static final String password = "carsiths";
    private Connection conn;
    public DBConn() {
        // Construct MySQL Connection
        String url = "jdbc:mysql://" + endpoint + "/" + database;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL at " + endpoint);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return this.conn;
    }
}
