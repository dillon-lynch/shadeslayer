package com.shadeslayer.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Player player;
    private final Dungeon dungeon;

    public GameState(Player player, Dungeon dungeon) {
        this.player = player;
        this.dungeon = dungeon;
    }

    public Player getPlayer() {
        return player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
}
