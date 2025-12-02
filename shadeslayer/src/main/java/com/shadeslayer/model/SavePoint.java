package com.shadeslayer.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SavePoint implements Serializable, Comparable<SavePoint> {
    
    private final int saveSlot;
    private final GameState gameState;
    private final String saveName;
    private final LocalDateTime timestamp;

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
        this.timestamp = LocalDateTime.now();
    }

    public int getSaveSlot() {
        return saveSlot;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getDisplayName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%d] %s - %s", saveSlot, saveName, timestamp.format(formatter));
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
