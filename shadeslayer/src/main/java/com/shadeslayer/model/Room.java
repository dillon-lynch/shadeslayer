package com.shadeslayer.model;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private final String name;
    private final String description;
    private final Map<Direction, Exit> exitsMap;
    // TODO: Add items and NPCs later
    
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exitsMap = new HashMap<>();
    }

    public Room(String name, String description, Map<Direction, Exit> exits) {
        this.name = name;
        this.description = description;
        this.exitsMap = exits;
    }

    public String getName() {
        return name;
    }

    public String getRoomDescription() {
        return description;
    }

    public String getFullDescription() {
        return "You are " + description + ".\nExits: " + getExitsList();
    }

    public void setExit(Direction direction, Exit exit) {
        exitsMap.put(direction, exit);
    }

    public Exit getExit(Direction direction) {
        return exitsMap.get(direction);
    }

    public String getExitsList() {
        StringBuilder exitList = new StringBuilder();
        for (Direction dir : exitsMap.keySet()) {
            exitList.append(dir.toString()).append(" ");
        }
        return exitList.toString().trim();
    }


}
