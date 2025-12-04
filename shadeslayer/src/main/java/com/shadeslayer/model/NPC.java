package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NPC implements Serializable {
    private final String name;
    private final String description;
    private final List<Item> inventory;
    private DialogueTree dialogueTree;
    private int health;
    private boolean isHostile;

    public NPC(String name, String description, int health, boolean isHostile,
            DialogueTree dialogueTree) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.isHostile = isHostile;
        this.inventory = new ArrayList<>();
        this.dialogueTree = dialogueTree;
    }

    public NPC(String name, String description, int health, boolean isHostile) {
        this(name, description, health, isHostile, null);
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

    public DialogueTree getDialogueTree() {
        return dialogueTree;
    }

    public void setDialogueTree(DialogueTree dialogueTree) {
        this.dialogueTree = dialogueTree;
    }

    public boolean hasDialogue() {
        return dialogueTree != null;
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
                .filter(item -> item.getName().equalsIgnoreCase(itemName)).findFirst().orElse(null);
    }

    public String talk(Player player) {
        if (dialogueTree != null) {
            DialogueNode currentNode = dialogueTree.getCurrentNode();
            if (currentNode != null && currentNode.canEnter(player)) {
                return currentNode.getText();
            }
        }
        return name + " has nothing to say.";
    }

    public List<DialogueChoice> getDialogueChoices(Player player) {
        if (dialogueTree != null) {
            return dialogueTree.getAvailableChoices(player);
        }
        return new ArrayList<>();
    }

    public boolean selectDialogueChoice(int choiceIndex, Player player) {
        if (dialogueTree != null) {
            return dialogueTree.selectChoice(choiceIndex, player);
        }
        return false;
    }

    public void resetDialogue() {
        if (dialogueTree != null) {
            dialogueTree.reset();
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
}
