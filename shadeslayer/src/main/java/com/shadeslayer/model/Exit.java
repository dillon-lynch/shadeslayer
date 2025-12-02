package com.shadeslayer.model;

import java.io.Serializable;
import java.util.function.Predicate;

public class Exit implements Serializable {
    private final Room targetRoom;
    private final String description;
    private final Predicate<Player> canPassThrough;

    public Exit(Room targetRoom, String description, Predicate<Player> canPassThrough) {
        this.targetRoom = targetRoom;
        this.description = description;
        this.canPassThrough = canPassThrough;
    }

    public Exit(Room targetRoom, String description) {
        this(targetRoom, description, player -> true);
    }

    public Room getTargetRoom() {
        return targetRoom;
    }

    public String getDescription() {
        return description;
    }

    public boolean canPassThrough(Player player) {
        return canPassThrough.test(player);
    }
}
