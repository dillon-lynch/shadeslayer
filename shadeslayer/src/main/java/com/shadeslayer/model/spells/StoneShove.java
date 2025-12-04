package com.shadeslayer.model.spells;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Spell;

public class StoneShove extends Spell {

    public StoneShove() {
        super("Stone Shove",
                "You command stone to remember its shape and shift. Rubble clears, pathways open.",
                "/icons/stone_shove.png",
                15);
    }

    @Override
    public String onUse(GameState gameState) {
        // Handled by GameController for room-specific effects
        return null;
    }

    public boolean canMoveStone() {
        return true;
    }
}
