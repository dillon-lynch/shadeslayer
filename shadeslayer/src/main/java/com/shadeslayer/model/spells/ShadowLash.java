package com.shadeslayer.model.spells;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Spell;

public class ShadowLash extends Spell {

    public ShadowLash() {
        super("Shadow Lash",
                "A whip of dark fire springs from your hand, striking with burning force.",
                "/icons/Scroll.png",
                20);
    }

    @Override
    public String onUse(GameState gameState) {
        // Handled by CombatHandler
        return null;
    }

    public int getDamage() {
        return 30;
    }
}
