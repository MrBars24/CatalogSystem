/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Gerald Michael
 */
public class DBHelper {
    private static Connection connection;
    
    public DBHelper()
    {
        
    }
    
    public static Connection connect()
    {
        String url = "jdbc:mysql://localhost/db_catalog",
            user = "root",
            password = "";
        
        try
        {
            connection = DriverManager.getConnection(url, user, password);
            System.out.print("Connecting...");
        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        
        return connection;
    }
}
