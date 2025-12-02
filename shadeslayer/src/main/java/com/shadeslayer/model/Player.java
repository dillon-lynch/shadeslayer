package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private final String name;
    private Room currentRoom;
    private final List<Item> inventory;
    private final List<Spell> spells;

    public Player(String name) {
        this.name = name;
        this.inventory = new ArrayList<>();
        this.spells = new ArrayList<>();
    }

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
        this.spells = new ArrayList<>();
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

    // Inventory management
    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }

    public Item getItemByName(String name) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public boolean hasItem(String name) {
        return getItemByName(name) != null;
    }

    // Spell management
    public void learnSpell(Spell spell) {
        if (!spells.contains(spell)) {
            spells.add(spell);
        }
    }

    public boolean forgetSpell(Spell spell) {
        return spells.remove(spell);
    }

    public Spell getSpellByName(String name) {
        for (Spell spell : spells) {
            if (spell.getName().equalsIgnoreCase(name)) {
                return spell;
            }
        }
        return null;
    }

    public List<Spell> getSpells() {
        return new ArrayList<>(spells);
    }

    public boolean knowsSpell(String name) {
        return getSpellByName(name) != null;
    }
}
