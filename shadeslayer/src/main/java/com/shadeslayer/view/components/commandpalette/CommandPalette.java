package com.shadeslayer.view.components.commandpalette;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class CommandPalette extends TitledPane {

    private CommandPaletteController controller;

    public CommandPalette() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("CommandPalette.fxml"));
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load CommandPalette.fxml", e);
        }
    }

    public void setData(String commandName, List<String> options) {
        setText(commandName);
        controller.setOptions(options);
    }

    public void setCommandName(String commandName) {
        setText(commandName);
    }

    public void setOptions(List<String> options) {
        controller.setOptions(options);
    }

    public ListView<String> getListView() {
        return controller.getListView();
    }
}
