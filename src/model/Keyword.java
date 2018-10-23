/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Gerald
 */
public class Keyword {
    private long id;
    private long researchId;
    private String keyword;

    public Keyword(long researchId, String keyword) {
        this.researchId = researchId;
        this.keyword = keyword;
    }

    public Keyword() {
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getResearchId() {
        return researchId;
    }

    public void setResearchId(long researchId) {
        this.researchId = researchId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
