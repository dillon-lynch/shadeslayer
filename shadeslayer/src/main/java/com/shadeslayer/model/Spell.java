package com.shadeslayer.model;

public abstract class Spell extends Usable {
    private final int energyCost;

    public Spell(String name, String description, int energyCost) {
        super(name, description);
        this.energyCost = energyCost;
    }

    @Override
    public abstract void onUse(Player player);
}
