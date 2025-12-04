package com.shadeslayer.model;

public abstract class Spell extends Usable {
    private final int energyCost;

    public Spell(String name, String description, int energyCost) {
        super(name, description);
        this.energyCost = energyCost;
    }

    public Spell(String name, String description, String imagePath, int energyCost) {
        super(name, description, imagePath);
        this.energyCost = energyCost;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public abstract String onUse(GameState gameState);
}
