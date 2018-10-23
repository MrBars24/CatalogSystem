/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Gerald
 */
public class Research {

    public long id;
    public String title;
    public String desc;
    public String author;
    public Timestamp publishAt;
    public ArrayList<Keyword> keyword;


    public Research(String title, String desc, String author, Timestamp publishAt, ArrayList<Keyword> keyword) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.publishAt = publishAt;
        this.keyword = keyword;
    }
    

    public Research(String title, String desc, String author, Timestamp publishAt) {
        this.title = title;
        this.author = author;
        this.desc = desc;
        this.publishAt = publishAt;
    }

    public Research(String test_title, String desc, String authors) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ArrayList<Keyword> getKeyword() {
        return keyword;
    }

    public void setKeyword(ArrayList<Keyword> keyword) {
        this.keyword = keyword;
    }
    
    public Timestamp getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Timestamp publishAt) {
        this.publishAt = publishAt;
    }
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
}
