package com.shadeslayer;

import com.shadeslayer.controller.GameController;
import com.shadeslayer.view.ViewController;

import javafx.application.Application;

public class Main {
    // This is static which means it can be accessed from either thread, due to
    // shared memory
    private static GameController gameController;

    public static void main(String[] args) {
        gameController = new GameController();
        // Interesting to learn: I pass the ViewController as a Class object, so its not
        // instantiated until the javaFX thread starts
        Application.launch(ViewController.class, args);
    }

    public static GameController getGameController() {
        return gameController;
    }
}
