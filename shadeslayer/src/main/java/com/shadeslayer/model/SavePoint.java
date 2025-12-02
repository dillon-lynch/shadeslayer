package com.shadeslayer.model;

import java.io.Serializable;

public class SavePoint implements Serializable, Comparable<SavePoint> {
    
    private final int saveSlot;
    private final GameState gameState;
    private final String saveName;
    private transient String timestamp;

    public SavePoint(int saveSlot, GameState gameState) {
        this(saveSlot, gameState, null);
    }

    public SavePoint(int saveSlot, GameState gameState, String saveName) {
        if (saveSlot < 1 || saveSlot > 3) {
            throw new IllegalArgumentException("Save slot must be between 1 and 3");
        }
        this.saveSlot = saveSlot;
        this.gameState = gameState;
        this.saveName = saveName != null ? saveName : "Save " + saveSlot;
    }

    public int getSaveSlot() {
        return saveSlot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getDisplayName() {
        return String.format("[%d] %s - %s", saveSlot, saveName, timestamp);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(SavePoint other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}
