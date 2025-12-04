package com.shadeslayer.model.spells;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Spell;

public class GlimmerWard extends Spell {

    public GlimmerWard() {
        super("Glimmer Ward",
                "A soft light springs from your palm, forming a protective shimmer. It reveals hidden things and shields against minor magic.",
                "/icons/glimmer_ward.png",
                10);
    }

    @Override
    public String onUse(GameState gameState) {
        // Handled by GameController for special room effects or combat
        return null;
    }

    public int getDefenseBonus() {
        return 15; // Reduces incoming magic damage (increased for balance)
    }

    public boolean revealsHiddenRunes() {
        return true;
    }
}
