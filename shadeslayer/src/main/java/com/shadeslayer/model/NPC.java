package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NPC implements Serializable {
    private final String name;
    private final String description;
    private final List<String> dialogue;
    private final List<Item> inventory;
    private final Consumer<Player> onTalk;
    private final Consumer<Player> onAttack;
    private int health;
    private boolean isHostile;

    public NPC(String name, String description, int health, boolean isHostile, 
               Consumer<Player> onTalk, Consumer<Player> onAttack) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.isHostile = isHostile;
        this.dialogue = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.onTalk = onTalk;
        this.onAttack = onAttack;
    }

    public NPC(String name, String description, int health, boolean isHostile) {
        this(name, description, health, isHostile, null, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean hostile) {
        isHostile = hostile;
    }

    public void addDialogue(String line) {
        dialogue.add(line);
    }

    public List<String> getDialogue() {
        return new ArrayList<>(dialogue);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public boolean hasItem(String itemName) {
        return inventory.stream()
            .anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }

    public Item getItemByName(String itemName) {
        return inventory.stream()
            .filter(item -> item.getName().equalsIgnoreCase(itemName))
            .findFirst()
            .orElse(null);
    }

    public void talk(Player player) {
        if (onTalk != null) {
            onTalk.accept(player);
        } else if (!dialogue.isEmpty()) {
            System.out.println(name + " says: " + dialogue.get(0));
        } else {
            System.out.println(name + " has nothing to say.");
        }
    }

    public void onAttacked(Player player) {
        if (onAttack != null) {
            onAttack.accept(player);
        } else {
            System.out.println(name + " takes damage!");
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
}
