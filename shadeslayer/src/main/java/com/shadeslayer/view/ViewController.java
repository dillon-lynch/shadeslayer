package com.shadeslayer.view;

import java.util.List;

import com.shadeslayer.controller.Parser;
import com.shadeslayer.model.Command;
import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Item;
import com.shadeslayer.model.OutputLine;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.Spell;
import com.shadeslayer.view.components.commandpalette.CommandPalette;
import com.shadeslayer.view.components.inventoryitem.InventoryItem;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewController extends Application implements GameView {

    // SECTION: FXML INJECTED COMPONENTS

    @FXML
    private VBox inventoryVBox;

    @FXML
    private VBox spellsVBox;

    @FXML
    private TextArea commandLogArea;

    @FXML
    private TextField commandInputField;

    @FXML
    private VBox commandPalettesVBox;

    // SECTION: INPUT HANDLING

    // Volatile to ensure its the most up-to-date value across threads
    private volatile String nextInput = null;
    private final Object inputLock = new Object();

    // SECTION: LIFECYCLE

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout.fxml"));
        loader.setController(this);
        Parent root = loader.load();

        setupCommandInput();

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Shadeslayer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        com.shadeslayer.Main.getGameController().startGame(this);
    }

    // SECTION: PUBLIC API FOR GAME CONTROLLER (GameView)

    @Override
    public void updateGameState(GameState gameState) {
        Player player = gameState.getPlayer();

        updateInventory(player.getInventory());

        updateSpells(player.getSpells());

        updateCommandPalettes(player);

        updateOutputLog(gameState.getOutputHistory());
    }

    @Override
    public String getNextInput() {
        synchronized (inputLock) {
            while (nextInput == null) {
                try {
                    inputLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return "";
                }
            }
            String input = nextInput;
            nextInput = null;
            return input;
        }
    }

    // SECTION: INTERNAL RENDERING METHODS

    private void updateOutputLog(List<OutputLine> outputHistory) {
        if (commandLogArea == null) {
            System.err.println("WARNING: commandLogArea not found");
            return;
        }

        commandLogArea.clear();
        for (OutputLine line : outputHistory) {
            commandLogArea.appendText(line.getText() + "\n");
        }
        commandLogArea.setScrollTop(Double.MAX_VALUE);
    }

    private void updateInventory(List<Item> inventory) {
        if (inventoryVBox == null) {
            System.err.println("WARNING: inventoryVBox not found");
            return;
        }

        inventoryVBox.getChildren().clear();

        for (Item item : inventory) {
            InventoryItem itemComponent = createInventoryItem(item);
            inventoryVBox.getChildren().add(itemComponent);
        }
    }

    private void updateSpells(List<Spell> spells) {
        if (spellsVBox == null) {
            System.err.println("WARNING: spellsVBox not found");
            return;
        }

        spellsVBox.getChildren().clear();

        for (Spell spell : spells) {
            InventoryItem spellComponent = createSpellItem(spell);
            spellsVBox.getChildren().add(spellComponent);
        }
    }

    private void updateCommandPalettes(Player player) {
        if (commandPalettesVBox == null) {
            System.err.println("WARNING: commandPalettesVBox not found");
            return;
        }

        commandPalettesVBox.getChildren().clear();

        for (Command command : Parser.getAvailableCommands(player)) {
            String commandName = command.getName();
            List<String> suggestions = Parser.getCommandArgumentSuggestions(command, player);

            CommandPalette palette = new CommandPalette();
            palette.setData(commandName.toUpperCase(), suggestions);

            // Add click handler for the TitledPane header (command name)
            palette.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && event.getTarget() == palette) {
                    commandInputField.setText(commandName.toLowerCase());
                    commandInputField.requestFocus();
                    commandInputField.positionCaret(commandInputField.getText().length());
                }
            });

            // Add click handler for the ListView items (arguments)
            ListView<String> listView = palette.getListView();
            if (listView != null) {
                listView.setOnMouseClicked(event -> {
                    String selectedArg = listView.getSelectionModel().getSelectedItem();
                    if (selectedArg != null) {
                        commandInputField.setText(commandName.toLowerCase() + " " + selectedArg.toLowerCase());
                        commandInputField.requestFocus();
                        commandInputField.positionCaret(commandInputField.getText().length());
                    }
                });
            }

            commandPalettesVBox.getChildren().add(palette);
        }
    }

    private InventoryItem createInventoryItem(Item item) {
        Image icon = loadIconFromPath(item.getImagePath());
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setData(item.getName(), icon);
        return inventoryItem;
    }

    private InventoryItem createSpellItem(Spell spell) {
        Image icon = loadIconFromPath(spell.getImagePath());
        InventoryItem spellItem = new InventoryItem();
        spellItem.setData(spell.getName(), icon);
        return spellItem;
    }

    private Image loadIconFromPath(String imagePath) {
        try {
            return new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
            return null;
        }
    }

    private void setupCommandInput() {
        if (commandInputField == null) {
            System.err.println("WARNING: commandInputField not found");
            return;
        }

        commandInputField.setPromptText("Enter command here...");

        commandInputField.setOnAction(event -> {
            String input = commandInputField.getText().trim();
            if (!input.isEmpty()) {
                processUserCommand(input);
                commandInputField.clear();
            }
        });
    }

    private void processUserCommand(String input) {
        synchronized (inputLock) {
            nextInput = input;
            inputLock.notify();
        }
    }
}
