package controller;


import controller.ResearchController;
import dev.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gerald Michael
 */
public class UserController {
    
    private Connection conn;
    private final String USER_FIELDS = " `id`, `name`, `username`";
    
    public UserController()
    {
        conn = DBHelper.connect();
    }
    
    public boolean checkUser(String username, String password)
    {
        String sql = "SELECT " + USER_FIELDS + " FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(2, password);
            stmt.setString(1, username);
 
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                User.name = rs.getString("name");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
