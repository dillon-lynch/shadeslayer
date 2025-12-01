package com.shadeslayer.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.SavePoint;

public class SaveController {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE_PREFIX = "slot";
    private static final String SAVE_FILE_EXTENSION = ".sav";

    public SaveController() {
        initializeSaveDirectory();
    }

    private void initializeSaveDirectory() {
        try {
            Path savePath = Paths.get(SAVE_DIRECTORY);
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create save directory: " + e.getMessage());
        }
    }

    public boolean save(int saveSlot, GameState gameState) {
        return save(saveSlot, gameState, null);
    }

    public boolean save(int saveSlot, GameState gameState, String saveName) {
        if (saveSlot < 1 || saveSlot > 3) {
            System.err.println("Invalid save slot. Must be 1-3.");
            return false;
        }

        try {
            SavePoint savePoint = new SavePoint(saveSlot, gameState, saveName);
            String fileName = generateFileName(saveSlot);
            Path filePath = Paths.get(SAVE_DIRECTORY, fileName);

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filePath.toFile()))) {
                oos.writeObject(savePoint);
            }

            System.out.println("Game saved to slot " + saveSlot + " at " + savePoint.getTimestamp());
            return true;

        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public SavePoint load(int saveSlot, String timestamp) {
        if (saveSlot < 1 || saveSlot > 3) {
            System.err.println("Invalid save slot. Must be 1-3.");
            return null;
        }

        List<SavePoint> saves = getSavesForSlot(saveSlot);
        if (saves.isEmpty()) {
            System.err.println("No saves found in slot " + saveSlot);
            return null;
        }

        SavePoint targetSave = null;
        if (timestamp == null) {
            targetSave = saves.get(saves.size() - 1);
        } else {
            for (SavePoint save : saves) {
                if (save.getTimestamp().equals(timestamp)) {
                    targetSave = save;
                    break;
                }
            }
        }

        if (targetSave == null) {
            System.err.println("Save with specified timestamp not found");
            return null;
        }

        deleteNewerSaves(saveSlot, targetSave.getTimestamp());
        return targetSave;
    }

    public SavePoint loadLatest(int saveSlot) {
        return load(saveSlot, null);
    }

    public List<SavePoint> getSavesForSlot(int saveSlot) {
        if (saveSlot < 1 || saveSlot > 3) {
            return new ArrayList<>();
        }

        List<SavePoint> saves = new ArrayList<>();
        Path slotDir = Paths.get(SAVE_DIRECTORY);

        try {
            if (!Files.exists(slotDir)) {
                return saves;
            }

            String pattern = SAVE_FILE_PREFIX + saveSlot + "_*" + SAVE_FILE_EXTENSION;
            List<Path> files = Files.list(slotDir)
                .filter(path -> path.getFileName().toString().matches(
                    SAVE_FILE_PREFIX + saveSlot + "_.*\\" + SAVE_FILE_EXTENSION))
                .sorted() // Sort files by name (timestamp)
                .collect(Collectors.toList()); // Converts stream to list

            for (Path file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file.toFile()))) {
                    SavePoint save = (SavePoint) ois.readObject();
                    saves.add(save);
                } catch (ClassNotFoundException | IOException e) {
                    System.err.println("Failed to load save from " + file + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to list saves: " + e.getMessage());
        }

        saves.sort(null);
        return saves;
    }

    public Map<Integer, List<SavePoint>> getAllSaves() {
        Map<Integer, List<SavePoint>> allSaves = new HashMap<>();
        for (int slot = 1; slot <= 3; slot++) {
            List<SavePoint> saves = getSavesForSlot(slot);
            if (!saves.isEmpty()) {
                allSaves.put(slot, saves);
            }
        }
        return allSaves;
    }

    private void deleteNewerSaves(int saveSlot, LocalDateTime targetTimestamp) {
        List<SavePoint> saves = getSavesForSlot(saveSlot);
        
        for (SavePoint save : saves) {
            if (save.getTimestamp().isAfter(targetTimestamp)) {
                String fileName = generateFileName(saveSlot, save.getTimestamp());
                Path filePath = Paths.get(SAVE_DIRECTORY, fileName);
                try {
                    Files.deleteIfExists(filePath);
                    System.out.println("Deleted newer save: " + save.getDisplayName());
                } catch (IOException e) {
                    System.err.println("Failed to delete save: " + e.getMessage());
                }
            }
        }
    }

    public boolean deleteSave(int saveSlot, LocalDateTime timestamp) {
        String fileName = generateFileName(saveSlot, timestamp);
        Path filePath = Paths.get(SAVE_DIRECTORY, fileName);
        
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                System.out.println("Deleted save from slot " + saveSlot);
            }
            return deleted;
        } catch (IOException e) {
            System.err.println("Failed to delete save: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAllSavesInSlot(int saveSlot) {
        if (saveSlot < 1 || saveSlot > 3) {
            return false;
        }

        List<SavePoint> saves = getSavesForSlot(saveSlot);
        boolean allDeleted = true;

        for (SavePoint save : saves) {
            if (!deleteSave(saveSlot, save.getTimestamp())) {
                allDeleted = false;
            }
        }

        return allDeleted;
    }

    private String generateFileName(int saveSlot) {
        return generateFileName(saveSlot, LocalDateTime.now());
    }

    private String generateFileName(int saveSlot, LocalDateTime timestamp) {
        String timestampStr = timestamp.toString()
            .replace(":", "-")
            .replace(".", "-"); // Replace colons and dots for file compatibility
        return SAVE_FILE_PREFIX + saveSlot + "_" + timestampStr + SAVE_FILE_EXTENSION;
    }

    public void printSavesForSlot(int saveSlot) {
        List<SavePoint> saves = getSavesForSlot(saveSlot);
        
        if (saves.isEmpty()) {
            System.out.println("No saves in slot " + saveSlot);
            return;
        }

        System.out.println("=== Slot " + saveSlot + " Saves ===");
        for (int i = 0; i < saves.size(); i++) {
            System.out.println((i + 1) + ". " + saves.get(i).getDisplayName());
        }
    }

    public void printAllSaves() {
        Map<Integer, List<SavePoint>> allSaves = getAllSaves();
        
        if (allSaves.isEmpty()) {
            System.out.println("No saves found");
            return;
        }

        System.out.println("=== All Saves ===");
        for (int slot = 1; slot <= 3; slot++) {
            List<SavePoint> saves = allSaves.get(slot);
            if (saves != null && !saves.isEmpty()) {
                System.out.println("\nSlot " + slot + ":");
                for (SavePoint save : saves) {
                    System.out.println("  " + save.getDisplayName());
                }
            }
        }
    }
}
