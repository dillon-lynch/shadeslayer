package com.shadeslayer.model;

import java.io.Serializable;

public abstract class Usable implements Serializable {
    protected String name;
    protected String description;
    
    public Usable(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Abstract method means subclass must implement
    public abstract void onUse(Player player);
}