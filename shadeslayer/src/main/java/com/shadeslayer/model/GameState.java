package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private final Player player;
    private final Dungeon dungeon;
    private final List<OutputLine> outputHistory;

    public GameState(Player player, Dungeon dungeon) {
        this.player = player;
        this.dungeon = dungeon;
        this.outputHistory = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public List<OutputLine> getOutputHistory() {
        return outputHistory;
    }

    public void addOutput(String text) {
        outputHistory.add(new OutputLine(text));
    }

    public void addOutput(OutputLine line) {
        outputHistory.add(line);
    }
}
