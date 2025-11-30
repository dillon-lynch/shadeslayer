package com.shadeslayer;

import java.util.Scanner;

import com.shadeslayer.controller.Parser;
import com.shadeslayer.model.Player;

public class Main {
    private final Player player;
    private final Scanner scanner;

    public Main() {
        this.player = new Player("Test");
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        //TODO: Implement game loop
        // while (!finished) {
        //     System.out.print("> ");
        //     String input = scanner.nextLine();
            
        //     ParsedCommand parsed = Parser.getParsedCommand(input);
            
        //     if (!parsed.isValid()) {
        //         System.out.println(parsed.getError());
        //         continue;
        //     }
            
        //     ParsedCommand parsedCommand = Parser.getParsedCommand(input);
        // }
        
        scanner.close();
        System.out.println("Thank you for playing. Goodbye.");
    }


    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Shadeslayer!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the dungeon.");
        System.out.print("Your command words are:\n");
        Parser.showCommands();
    }

    private void look() {
        if (player.getCurrentRoom() != null) {
            System.out.println(player.getCurrentRoom().getFullDescription());
        } else {
            System.out.println("You are nowhere.");
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.play();
    }
}
