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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Keyword;
import model.Research;

/**
 *
 * @author Gerald
 */
public class ResearchController {
    
    private Connection conn;
    private final String RESEARCH_FIELDS = " `researches`.`id`, `title`, `author`, `publish_at`, `description` ";
    
    public ResearchController()
    {
        conn = DBHelper.connect();
    }
    
    public ResultSet getResearches(String search)
    {
        String sql = "SELECT " + RESEARCH_FIELDS + " FROM researches LEFT JOIN keywords ON researches.id = researches.id "
                + "WHERE title LIKE ? OR keywords.keyword = ? GROUP BY researches.id";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + search + "%");
            stmt.setString(2, search);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
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
    
    public HashMap getResearchMap(long id) {
        HashMap hash = new HashMap();
        
        String sql = "SELECT " + RESEARCH_FIELDS + " FROM researches where id = ? ";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            List<String> keywords = new ArrayList();
            stmt = conn.prepareStatement("SELECT keyword FROM keywords WHERE research_id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            while(rs.next()) {
                keywords.add(rs.getString(1));
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            while(rs.next()) {
                hash.put("id", rs.getInt(1));
                hash.put("title", rs.getString(2));
                hash.put("author", rs.getString(3));
                hash.put("publish_at", rs.getTimestamp(4));
                hash.put("description", rs.getString(5));
                hash.put("keyword", keywords);
            }
            
            return hash;
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public ResultSet addResearch(Research r)
    {
        String sql = "INSERT INTO researches(title, description, author, publish_at) VALUES(?, ?, ?, ?)";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, r.getTitle());
            stmt.setString(2, r.getDesc());
            stmt.setString(3, r.getAuthor());
            stmt.setTimestamp(4, r.getPublishAt());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    addKeywords(generatedKeys.getLong(1), r.getKeyword());
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
    
    public void addKeywords(long id, ArrayList<Keyword> key)
    {
        String sqlKeyword = "INSERT INTO keywords(research_id, keyword) VALUES(?,?)";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {    
            stmt = conn.prepareStatement(sqlKeyword);
            for (int i = 0; i < key.size(); i++) {
                stmt.setLong(1, id);
                stmt.setString(2, key.get(i).getKeyword());
                stmt.addBatch();
            }
            stmt.executeBatch();
            
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet updateResearch(Research r)
    {
        String sql = "UPDATE researches set title = ?, description = ?, author = ?, publish_at = ? WHERE id = ?";
        PreparedStatement stmt;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, r.getTitle());
            stmt.setString(2, r.getDesc());
            stmt.setString(3, r.getAuthor());
            stmt.setTimestamp(4, r.getPublishAt());
            stmt.setLong(5, r.getId());
            
            if(stmt.executeUpdate() > 0) {
                return getResearch(r.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public int[] deleteResearch(long[] id)
    {
        String sql = "DELETE FROM researches WHERE id = ?";
        PreparedStatement stmt, stmtResearch;
        
        try {
            stmt = conn.prepareStatement("DELETE FROM keywords WHERE research_id = ?");
            stmtResearch = conn.prepareStatement(sql);
            for(int i = 0; i < id.length; i++) {
                stmt.setLong(1, id[i]);
                stmtResearch.setLong(1, id[i]);
                stmt.addBatch();
                stmtResearch.addBatch();
            }
            
            int[] keyword = stmt.executeBatch();
            stmtResearch.executeBatch();
            return keyword;
        } catch (SQLException ex) {
            Logger.getLogger(ResearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
