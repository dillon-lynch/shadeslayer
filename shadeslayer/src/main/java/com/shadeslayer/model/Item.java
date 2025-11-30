package com.shadeslayer.model;

public abstract class Item extends Usable {
    private int durability;
    private final int maxDurability;

    public Item(String name, String description) {
        super(name, description);
        this.durability = 100;
        this.maxDurability = 100;
    }

    public Item(String name, String description, int durability, int maxDurability) {
        super(name, description);
        this.durability = durability;
        this.maxDurability = maxDurability;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getMaxDurability() {
            return maxDurability;
    }

    @Override
    public abstract void onUse(Player player);
}
