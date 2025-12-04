package com.shadeslayer.model;

public class StarterSpell extends Spell {

    public StarterSpell() {
        super("Fireball", "A basic fire attack spell", "/icon.png", 10);
    }

    @Override
    public String onUse(GameState gameState) {
        // Basic spell usage - could deal damage
        return "You cast " + getName() + "!";
    }
}
