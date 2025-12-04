package com.shadeslayer.controller;

import com.shadeslayer.model.Item;
import com.shadeslayer.model.NPC;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.Spell;
import com.shadeslayer.model.items.BoneKey;
import com.shadeslayer.model.items.IronShortblade;
import com.shadeslayer.model.spells.GlimmerWard;
import com.shadeslayer.model.spells.ShadowLash;

public class CombatHandler {
    private NPC currentEnemy;
    private boolean combatActive;
    private boolean playerDefending;
    private int defenseBonus;

    public void startCombat(NPC enemy) {
        this.currentEnemy = enemy;
        this.combatActive = true;
        this.playerDefending = false;
        this.defenseBonus = 0;
    }

    public void endCombat() {
        this.currentEnemy = null;
        this.combatActive = false;
        this.playerDefending = false;
        this.defenseBonus = 0;
    }

    public boolean isInCombat() {
        return combatActive;
    }

    public NPC getCurrentEnemy() {
        return currentEnemy;
    }

    public String attackWithWeapon(Player player, Item weapon, boolean heavyAttack) {
        if (!combatActive || currentEnemy == null) {
            return "You're not in combat.";
        }

        int damage = 0;
        int energyCost = 0;

        if (weapon instanceof IronShortblade) {
            IronShortblade blade = (IronShortblade) weapon;
            if (heavyAttack) {
                damage = blade.getHeavyAttackDamage();
                energyCost = blade.getHeavyAttackEnergy();
            } else {
                damage = blade.getBaseDamage();
                energyCost = blade.getLightAttackEnergy();
            }
        } else {
            return "You don't have a weapon equipped!";
        }

        if (energyCost > 0 && !player.hasEnergy(energyCost)) {
            return "You don't have enough energy for that attack!";
        }

        player.consumeEnergy(energyCost);
        currentEnemy.takeDamage(damage);

        String result = String.format("You strike %s for %d damage!", currentEnemy.getName(), damage);

        if (!currentEnemy.isAlive()) {
            result += String.format("\n%s has been defeated!", currentEnemy.getName());
            onEnemyDefeated(player);
            endCombat();
        } else {
            result += "\n" + enemyTurn(player);
        }

        return result;
    }

    public String castSpellInCombat(Player player, Spell spell) {
        if (!combatActive || currentEnemy == null) {
            return "You're not in combat.";
        }

        if (!player.hasEnergy(spell.getEnergyCost())) {
            return "You don't have enough energy to cast that spell!";
        }

        String result;

        if (spell instanceof ShadowLash lash) {
            player.consumeEnergy(spell.getEnergyCost());
            currentEnemy.takeDamage(lash.getDamage());
            result = String.format("You lash %s with shadow magic for %d damage!", currentEnemy.getName(),
                    lash.getDamage());

            if (!currentEnemy.isAlive()) {
                result += String.format("\n%s has been defeated!", currentEnemy.getName());
                onEnemyDefeated(player);
                endCombat();
            } else {
                result += "\n" + enemyTurn(player);
            }
        } else if (spell instanceof GlimmerWard ward) {
            player.consumeEnergy(spell.getEnergyCost());
            playerDefending = true;
            defenseBonus = ward.getDefenseBonus();
            result = "You raise a shimmering ward of light.\n" + enemyTurn(player);
        } else {
            result = "You can't use that spell in combat.";
        }

        return result;
    }

    private String enemyTurn(Player player) {
        if (!currentEnemy.isAlive()) {
            return "";
        }

        int damage = calculateEnemyDamage();

        if (playerDefending) {
            damage = Math.max(0, damage - defenseBonus);
            playerDefending = false;
            defenseBonus = 0;
        }

        player.takeDamage(damage);

        String result = String.format("%s attacks you for %d damage!", currentEnemy.getName(), damage);

        if (player.isDead()) {
            result += "\nYou have been defeated!";
            endCombat();
        }

        return result;
    }

    private int calculateEnemyDamage() {
        if (currentEnemy.getName().equals("Shade Acolyte")) {
            return 12;
        } else if (currentEnemy.getName().equals("Durza")) {
            return 25;
        }
        return 10;
    }

    private void onEnemyDefeated(Player player) {
        if (currentEnemy.getName().equals("Shade Acolyte")) {
            player.addItem(new BoneKey());
        }
    }
}
