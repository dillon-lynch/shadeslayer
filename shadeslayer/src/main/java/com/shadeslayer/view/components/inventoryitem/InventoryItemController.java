package com.shadeslayer.view.components.inventoryitem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InventoryItemController {

    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemLabel;

    public void setImage(Image image) {
        itemImage.setImage(image);
    }

    public void setImageFromPath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(imagePath);
                setImage(image);
            } catch (Exception e) {
                System.err.println("Could not load image: " + imagePath);
            }
        }
    }

    public void setItemName(String name) {
        itemLabel.setText(name);
    }
}
