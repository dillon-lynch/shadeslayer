package com.shadeslayer.model;

public class StarterSpell extends Spell {
    
    public StarterSpell() {
        super("Fireball", "A basic fire attack spell", "/icon.png", 10);
    }

    @Override
    public void onUse(Player player) {
        // Basic spell usage - could deal damage
        System.out.println("You cast " + getName() + "!");
    }
}
