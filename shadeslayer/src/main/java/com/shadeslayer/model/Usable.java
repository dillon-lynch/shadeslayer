package com.shadeslayer.model;

import java.io.Serializable;

public abstract class Usable implements Serializable {
    protected String name;
    protected String description;
    protected String imagePath;
    
    public Usable(String name, String description) {
        this.name = name;
        this.description = description;
        this.imagePath = "/icon.png"; // Default image
    }
    
    public Usable(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    // Abstract method means subclass must implement
    public abstract void onUse(Player player);
}