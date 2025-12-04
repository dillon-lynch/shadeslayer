package com.shadeslayer.model;

public class StarterSword extends Item {
    
    public StarterSword() {
        super("Starter Sword", "A basic iron sword for beginners", "/icon.png", 100, 100);
    }

    @Override
    public void onUse(Player player) {
        // Basic sword usage - could be equipped or attack
        System.out.println("You wield the " + getName());
    }
}
