package com.dbconnectionutil.org;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    static String HOST = "jdbc:mysql://localhost/CRUD";

    static String USER = "root";

    static String PASSWORD = "";

    static Connection conn = null;

    public static Connection getConnection() {

        try{

            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(HOST, USER, PASSWORD);

//            System.out.println("Connected Successfully to the database");

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

        return conn;

    }

}
