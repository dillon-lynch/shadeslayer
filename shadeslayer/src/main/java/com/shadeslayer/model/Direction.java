package com.shadeslayer.model;

public enum Direction {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }


    @Override
    public String toString() {
        return direction;
    }

    public static Direction fromString(String text) {
        for (Direction d : Direction.values()) {
            if (d.direction.equalsIgnoreCase(text)) {
                return d;
            }
        }
        return null;
    }
}
