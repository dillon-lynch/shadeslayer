package com.shadeslayer.view.components.commandpalette;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CommandPaletteController {

    @FXML
    private ListView<String> optionsList;

    public void setOptions(List<String> options) {
        optionsList.getItems().clear();
        if (options != null && !options.isEmpty()) {
            optionsList.getItems().addAll(options);
        }
    }

    public ListView<String> getListView() {
        return optionsList;
    }
}
