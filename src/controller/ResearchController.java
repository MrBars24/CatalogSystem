/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dev.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Research;

/**
 *
 * @author Gerald
 */
public class ResearchController {
    
    private Connection conn;
    private final String RESEARCH_FIELDS = " `id`, `title`, `author`, `publish_at` ";
    
    public ResearchController()
    {
        conn = DBHelper.connect();
    }
    
    public ResultSet getResearches()
    {
        String sql = "SELECT " + RESEARCH_FIELDS + " FROM researches";
        Statement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    public ResultSet getResearch(long id)
    {
        String sql = "SELECT " + RESEARCH_FIELDS + " FROM researches where id = ? ";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    public ResultSet addResearch(Research r)
    {
        String sql = "INSERT INTO researches(title, description) VALUES(?, ?)";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, r.getTitle());
            stmt.setString(2, r.getDesc());
            stmt.setString(2, r.getAuthor());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return getResearch(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    public ResultSet updateResearch(Research r)
    {
        ResultSet rs = null;
        
        return rs;
    }
}
