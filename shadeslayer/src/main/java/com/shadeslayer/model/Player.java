package com.shadeslayer.model;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    private final String name;
    private Room currentRoom;
    private final Container<Item> inventory;
    private final Container<Spell> spells;
    private int health;
    private int maxHealth;
    private int energy;
    private int maxEnergy;

    public Player(String name) {
        this.name = name;
        this.inventory = new Container<>();
        this.spells = new Container<>();
        this.maxHealth = 150; // Increased for balance
        this.health = maxHealth;
        this.maxEnergy = 100;
        this.energy = maxEnergy;
    }

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new Container<>();
        this.spells = new Container<>();
        this.maxHealth = 150; // Increased for balance
        this.health = maxHealth;
        this.maxEnergy = 100;
        this.energy = maxEnergy;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    public Item getItemByName(String name) {
        return inventory.findByName(name);
    }

    public List<Item> getInventory() {
        return inventory.getAll();
    }

    public boolean hasItem(String name) {
        return inventory.contains(name);
    }

    public void learnSpell(Spell spell) {
        if (!spells.contains(spell)) {
            spells.add(spell);
        }
    }

    public boolean forgetSpell(Spell spell) {
        return spells.remove(spell);
    }

    public Spell getSpellByName(String name) {
        return spells.findByName(name);
    }

    public List<Spell> getSpells() {
        return spells.getAll();
    }

    public boolean knowsSpell(String name) {
        return spells.contains(name);
    }

    // Health management
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    // Energy management
    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, maxEnergy));
    }

    public boolean hasEnergy(int amount) {
        return energy >= amount;
    }

    public boolean consumeEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }

    public void restoreEnergy(int amount) {
        energy += amount;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    public void fullyRestore() {
        health = maxHealth;
        energy = maxEnergy;
    }
}
