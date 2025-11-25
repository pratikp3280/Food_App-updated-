package com.app.test;

import java.sql.Connection;
import com.app.utility.DBConnection;

public class TestDBConnection {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("✅ Database Connected Successfully!");
        } else {
            System.out.println("❌ Database Connection Failed!");
        }
    }
}
