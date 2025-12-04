package com.shadeslayer.model.items;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Item;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.spells.StoneShove;

public class StoneShoveScroll extends Item {

    public StoneShoveScroll() {
        super("Stone Shove Scroll",
                "An ancient scroll inscribed with earth magic runes. Reading it will teach you the Stone Shove spell, allowing you to telekinetically move heavy objects and clear rubble.",
                "/icons/scroll.png");
    }

    @Override
    public String onUse(GameState gameState) {
        Player player = gameState.getPlayer();
        if (!player.knowsSpell("Stone Shove")) {
            player.learnSpell(new StoneShove());
            player.removeItem(this);
            return "You unfurl the ancient scroll and study the earth magic runes. Knowledge flows into your mind...\n\n[You learned Stone Shove!]";
        } else {
            return "You already know the Stone Shove spell. The scroll would be redundant.";
        }
    }
}
