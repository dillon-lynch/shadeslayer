package com.shadeslayer.model.items;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Item;

public class BoneKey extends Item {

    public BoneKey() {
        super("Bone Key",
                "A key carved from yellowed bone, etched with runes. It thrums with dark magic.",
                "/icons/BoneKey.png",
                1,
                1);
    }

    @Override
    public String onUse(GameState gameState) {
        return "The Bone Key thrums with dark magic. It will unlock the sealed iron gate when you approach it.";
    }

    public boolean unlocksDoor(String doorId) {
        return "RITUAL_ARCHIVE_GATE".equals(doorId);
    }
}
