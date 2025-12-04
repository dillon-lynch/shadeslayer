package com.shadeslayer.model.items;

import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Item;

public class IronShortblade extends Item {

    public IronShortblade() {
        super("Iron Shortblade",
                "A proper guard's weapon with a double-edged blade and leather-wrapped hilt. The iron gleams with care, its edge honed to razor sharpness. This blade was forged for battle, not practice. Deals 15 damage per light attack (no energy cost) or 30 damage with a heavy strike (costs 10 energy). Highly durable.",
                "/icons/IronShortblade.png",
                100,
                100);
    }

    @Override
    public String onUse(GameState gameState) {
        return "You draw the Iron Shortblade. The balanced weight feels deadly in your hands.";
    }

    public int getBaseDamage() {
        return 15;
    }

    public int getLightAttackEnergy() {
        return 0;
    }

    public int getHeavyAttackDamage() {
        return 25;
    }

    public int getHeavyAttackEnergy() {
        return 5;
    }
}
