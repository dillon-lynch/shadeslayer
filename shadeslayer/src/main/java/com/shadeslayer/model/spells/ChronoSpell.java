package com.shadeslayer.model.spells;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Spell;

public class ChronoSpell extends Spell {

    public ChronoSpell() {
        super("ChronoSpell",
                "An instinctive magic that lets you rewind time to your last anchor point. The rune on your arm glows when you think of going back.",
                "/icons/Chrnospell.png",
                0); // No energy cost - it restores energy
    }

    @Override
    public String onUse(GameState gameState) {
        // Actual rewind logic is handled by GameController
        // This spell triggers a load from the last savepoint
        gameState.getPlayer().fullyRestore(); // Restore health and energy
        return null; // GameController handles the message
    }

    public boolean triggersRewind() {
        return true;
    }
}
