package com.shadeslayer.model;

import java.io.Serializable;
import java.util.HashMap;

public class Dungeon implements Serializable {
    private static final long serialVersionUID = 1L;
    private final HashMap<String, Room> rooms;

    public Dungeon() {
        rooms = new HashMap<>();
        createRooms();
    }

    private void createRooms() {
        // TODO: Add Room Creation
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
}
