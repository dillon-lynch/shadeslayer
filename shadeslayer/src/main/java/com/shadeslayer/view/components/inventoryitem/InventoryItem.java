package com.shadeslayer.view.components.inventoryitem;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class InventoryItem extends VBox {

    private InventoryItemController controller;

    public InventoryItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("InventoryItem.fxml"));
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load InventoryItem.fxml", e);
        }
    }

    public void setData(String name, Image image) {
        controller.setItemName(name);
        if (image != null) {
            controller.setImage(image);
        }
    }

    public void setItemName(String name) {
        controller.setItemName(name);
    }

    public void setImage(Image image) {
        controller.setImage(image);
    }

    public void setImageFromPath(String imagePath) {
        controller.setImageFromPath(imagePath);
    }
}
