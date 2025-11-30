package com.shadeslayer.model;

import java.util.HashMap;

public class Dungeon {
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
