package com.shadeslayer;

import java.util.Scanner;

import com.shadeslayer.controller.GameController;
import com.shadeslayer.view.ViewController;

import javafx.application.Application;

public class Main {
    // This is static which means it can be accessed from either thread, due to
    // shared memory
    private static GameController gameController;
    private static int selectedSaveSlot = 1; // Default to slot 1

    public static void main(String[] args) {
        // Prompt user to select save slot
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== SHADESLAYER ===");
        System.out.println("Select a save slot (1-3):");

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                int slot = Integer.parseInt(input);

                if (slot >= 1 && slot <= 3) {
                    selectedSaveSlot = slot;
                    validInput = true;
                    System.out.println("Save slot " + slot + " selected.\n");
                } else {
                    System.out.println("Invalid slot. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
            }
        }

        gameController = new GameController();
        // Interesting to learn: I pass the ViewController as a Class object, so its not
        // instantiated until the javaFX thread starts
        Application.launch(ViewController.class, args);
    }

    public static GameController getGameController() {
        return gameController;
    }

    public static int getSelectedSaveSlot() {
        return selectedSaveSlot;
    }
}
