package com.shadeslayer.model;

public class Player {
    private final String name;
    private Room currentRoom;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
}
