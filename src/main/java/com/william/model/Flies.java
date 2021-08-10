package com.william.model;

public class Flies {
    private int flyId;
    private String name;
    private String target;
    private String season;
    private String website;
    private String creator;

    public Flies(){

    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getFlyId() {
        return flyId;
    }

    public void setFlyId(int flyId) {
        this.flyId = flyId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
