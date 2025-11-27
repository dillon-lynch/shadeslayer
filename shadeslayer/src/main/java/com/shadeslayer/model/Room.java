package com.shadeslayer.model;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private final String id;
    private final String name;
    private final String description;
    private final Map<Direction, String> exits; // Maps directions to room IDs
    // TODO: Add items and NPCs later
    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
    }

    public Room(String id, String name, String description, Map<Direction, String> exits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = exits;
    }

    public String getId() {
        return id;
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

    public void setExit(Direction direction, String neighbouringRoomId) {
        exits.put(direction, neighbouringRoomId);
    }

    public String getExitId(Direction direction) {
        return exits.get(direction);
    }

    public String getExitsList() {
        StringBuilder exitList = new StringBuilder();
        for (Direction dir : exits.keySet()) {
            exitList.append(dir.toString()).append(" ");
        }
        return exitList.toString().trim();
    }


}
