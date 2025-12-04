package com.shadeslayer.controller;

import com.shadeslayer.model.Dungeon;
import com.shadeslayer.model.GameState;
import com.shadeslayer.model.ParsedCommand;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.StarterSpell;
import com.shadeslayer.model.StarterSword;
import com.shadeslayer.view.GameView;

import javafx.application.Platform;

// CURRENTLY IN TEST STATE FOR UI
public class GameController {
    private GameState gameState;

    public GameController() {
        initializeGame();
    }

    private void initializeGame() {
        Player player = new Player("Hero");
        player.addItem(new StarterSword());
        player.learnSpell(new StarterSpell());

        Dungeon dungeon = new Dungeon();

        this.gameState = new GameState(player, dungeon);
    }

    public void startGame(GameView view) {
        new Thread(() -> {
            runGameLoop(view);
        }).start();
    }

    private void runGameLoop(GameView view) {
        if (gameState.getPlayer().getCurrentRoom() != null) {
            gameState.addOutput(gameState.getPlayer().getCurrentRoom().getFullDescription());
        }
        gameState.addOutput("Welcome to Shadeslayer! Type 'help' for commands.");

        Platform.runLater(() -> {
            view.updateGameState(gameState);
        });

        boolean running = true;
        while (running) {
            String input = view.getNextInput();

            gameState.addOutput("> " + input);

            ParsedCommand parsed = Parser.getParsedCommand(input);

            if (!parsed.isValid()) {
                gameState.addOutput("Error: " + parsed.getError());
                Platform.runLater(() -> {
                    view.updateGameState(gameState);
                });
                continue;
            }

            String commandName = parsed.getCommand().getName();

            if ("quit".equals(commandName)) {
                running = false;
                gameState.addOutput("Thanks for playing!");
                Platform.runLater(() -> {
                    view.updateGameState(gameState);
                });
            } else {
                // TODO: Add all command handling
                gameState.addOutput("Executed: " + commandName);
                Platform.runLater(() -> {
                    view.updateGameState(gameState);
                });
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public ParsedCommand processCommand(String input) {
        ParsedCommand parsed = Parser.getParsedCommand(input);

        if (!parsed.isValid()) {
            return parsed;
        }

        // TODO: Implement actual command execution
        // For now, just return the parsed command
        // Later: execute commands like go, take, drop, use, etc.

        return parsed;
    }

    public Player getPlayer() {
        return gameState.getPlayer();
    }

    public Dungeon getDungeon() {
        return gameState.getDungeon();
    }
}
