package com.shadeslayer.model;

public class StarterSword extends Item {

    public StarterSword() {
        super("Starter Sword", "A basic iron sword for beginners", "/icon.png", 100, 100);
    }

    @Override
    public String onUse(GameState gameState) {
        // Basic sword usage - could be equipped or attack
        return "You wield the " + getName() + ". It feels solid in your hands.";
    }
}
