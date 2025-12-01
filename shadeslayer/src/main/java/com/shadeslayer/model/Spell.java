package com.shadeslayer.model;

import java.io.Serializable;

public abstract class Spell extends Usable implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int energyCost;

    public Spell(String name, String description, int energyCost) {
        super(name, description);
        this.energyCost = energyCost;
    }

    @Override
    public abstract void onUse(Player player);
}
