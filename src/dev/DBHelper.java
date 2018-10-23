/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gerald Michael
 */
public class DBHelper {
    private static Connection connection;
    private static final String DBNAME = "db_catalog";
    
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
        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            
            if(ex.getMessage().equals("Unknown database 'db_catalog'")) {
                try {
                    DBHelper.createDB();
                } catch (SQLException ex1) {
                    Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        
        return connection;
    }
    
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:mysql://localhost", "root", "");
    }
    
    public static void createDB() throws SQLException
    {
        String s            = new String();
        StringBuffer sb = new StringBuffer();
        try {
            FileReader fr = new FileReader(new File("db_clean.sql"));
            BufferedReader br = new BufferedReader(fr);
            
            while((s = br.readLine()) != null)
            {
                sb.append(s);
            }
            
            br.close();
            
            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");
 
            Connection c = DBHelper.getConnection();
            Statement st = c.createStatement();
 
            for(int i = 0; i<inst.length; i++)
            {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if(!inst[i].trim().equals(""))
                {
                    st.executeUpdate(inst[i]);
                    System.out.println(">>"+inst[i]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
