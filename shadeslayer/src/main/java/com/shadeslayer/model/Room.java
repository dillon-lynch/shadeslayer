package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {
    private final String name;
    private final String description;
    private final Map<Direction, Exit> exitsMap;
    private final List<Item> items;
    private final List<NPC> npcs;
    
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exitsMap = new HashMap<>();
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public Room(String name, String description, Map<Direction, Exit> exits) {
        this.name = name;
        this.description = description;
        this.exitsMap = exits;
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getRoomDescription() {
        return description;
    }

    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are ").append(description).append(".\n");
        sb.append("Exits: ").append(getExitsList()).append("\n");
        
        if (!items.isEmpty()) {
            sb.append("Items: ");
            for (Item item : items) {
                sb.append(item.getName()).append(" ");
            }
            sb.append("\n");
        }
        
        if (!npcs.isEmpty()) {
            sb.append("NPCs: ");
            for (NPC npc : npcs) {
                sb.append(npc.getName()).append(" ");
            }
            sb.append("\n");
        }
        
        return sb.toString().trim();
    }

    public void setExit(Direction direction, Exit exit) {
        exitsMap.put(direction, exit);
    }

    public Exit getExit(Direction direction) {
        return exitsMap.get(direction);
    }

    public String getExitsList() {
        StringBuilder exitList = new StringBuilder();
        for (Direction dir : exitsMap.keySet()) {
            exitList.append(dir.toString()).append(" ");
        }
        return exitList.toString().trim();
    }

    // Item management
    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public boolean hasItem(String name) {
        return getItemByName(name) != null;
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public boolean removeNPC(NPC npc) {
        return npcs.remove(npc);
    }

    public NPC getNPCByName(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(name)) {
                return npc;
            }
        }
        return null;
    }

    public List<NPC> getNPCs() {
        return new ArrayList<>(npcs);
    }

    public boolean hasNPC(String name) {
        return getNPCByName(name) != null;
    }
}
