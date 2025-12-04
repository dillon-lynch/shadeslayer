package com.shadeslayer.model;

import java.io.Serializable;
import java.util.HashMap;

import com.shadeslayer.controller.DialogueFactory;
import com.shadeslayer.model.items.BoneKey;
import com.shadeslayer.model.items.IronShortblade;
import com.shadeslayer.model.items.StoneShoveScroll;
import com.shadeslayer.model.spells.ShadowLash;

public class Dungeon implements Serializable {
    private final HashMap<String, Room> rooms;

    public Dungeon() {
        rooms = new HashMap<>();
        createRooms();
    }

    private void createRooms() {
        // Room 1: Watchtower Cell (Start)
        Room watchtowerCell = new Room("Watchtower Cell",
                "in a forgotten watchtower chamber carved deep into living stone. Ancient cracks spider-web across walls that weep moisture, and a narrow slit window offers a dizzying view into an abyss of darkness below. The remains of a collapsed spiral staircase lie in ruins - the only way forward is deeper into Durza's fortress. Cold air carries the scent of old stone and something else... something draconic.\n\n[TIP: Type 'help' to see available commands]");
        rooms.put("WATCHTOWER_CELL", watchtowerCell);

        // Room 2: Guard Corridor
        Room guardCorridor = new Room("Guard Corridor",
                "in a claustrophobic hallway where iron cell doors line both walls like silent sentinels. Guttering torches cast dancing shadows across ancient Elvish runes carved into the stone - warnings, perhaps, or binding spells. One cell door hangs open on broken hinges, and from within comes the rasping whisper of labored breathing. The air tastes of old blood and despair.\n\n[TIP: Talk to the Wounded Prisoner to learn Glimmer Ward spell. Pick up the Stone Shove Scroll to clear rubble ahead.]");

        NPC woundedPrisoner = new NPC("Wounded Prisoner",
                "a gaunt man slumped against the wall, blood staining his torn guard's uniform",
                50, false, DialogueFactory.createWoundedPrisonerDialogue());
        guardCorridor.addNPC(woundedPrisoner);
        guardCorridor.addItem(new StoneShoveScroll()); // Required to clear rubble
        rooms.put("GUARD_CORRIDOR", guardCorridor);

        // Room 3: Supply Vault
        Room supplyVault = new Room("Supply Vault",
                "in a ransacked armory where splintered crates spill their contents across dusty flagstones. Empty weapon racks line the walls like skeletal fingers, most blades long since claimed. To the south, a stone archway is choked with rubble from a collapsed ceiling. Yet when you focus your mind, you sense something odd - the stones vibrate faintly with residual magic, as if they remember their original positions and yearn to return.\n\n[TIP: Take the Iron Shortblade - you'll need it for combat. Use 'use Stone Shove Scroll' to learn the spell, then go south to clear the rubble.]");
        supplyVault.addItem(new IronShortblade());
        rooms.put("SUPPLY_VAULT", supplyVault);

        // Room 4: Shadow Cells
        Room shadowCells = new Room("Shadow Cells",
                "in a deeper cell block where darkness clings to the bars like living things. Claw marks scar the floor leading out from an open cell. Something still hunts these hallways.\n\n[TIP: Defeat the Shade Acolyte (60 HP, 12 damage) to get the Bone Key. Use 'attack' for light attacks (15 damage) or 'attack heavy' for heavy attacks (30 damage, costs 10 energy).]");

        NPC shadeAcolyte = new NPC("Shade Acolyte",
                "a twisted figure wreathed in shadow, its eyes glowing with malevolent red light",
                60, true, DialogueFactory.createShadeAcolyteDialogue());
        shadeAcolyte.addItem(new BoneKey()); // Drops when defeated - needed for Ritual Archive
        shadowCells.addNPC(shadeAcolyte);
        rooms.put("SHADOW_CELLS", shadowCells);

        // Room 5: Ritual Archive
        Room ritualArchive = new Room("Ritual Archive",
                "in a sanctum filled with chained books and arcane artifacts. A massive statue with crystal eyes watches over a glowing lectern. The air hums with ancient power.\n\n[TIP: Pick up and use the Shadow Lash Scroll to learn the final spell. You need all 3 spells (Glimmer Ward, Stone Shove, Shadow Lash) to enter the portal below.]");
        ritualArchive.addItem(new Item("Shadow Lash Scroll",
                "A dark scroll that teaches the Shadow Lash spell - a whip of pure darkness that deals 25 damage") {
            @Override
            public String onUse(GameState gameState) {
                if (!gameState.getPlayer().knowsSpell("Shadow Lash")) {
                    gameState.getPlayer().learnSpell(new ShadowLash());
                    gameState.getPlayer().removeItem(this);
                    return "You read the scroll and learn Shadow Lash! The scroll crumbles to dust.\n\n[You learned Shadow Lash!]";
                }
                return "You already know this spell.";
            }
        });
        rooms.put("RITUAL_ARCHIVE", ritualArchive);

        // Room 6: Pit of Chains (Final Boss)
        Room pitOfChains = new Room("Pit of Chains",
                "standing at the edge of a vast underground chasm. Massive chains stretch into the darkness above, where a dragon hangs suspended in glowing crystal. Far across the pit, a figure in black robes turns to face you.\n\n[FINAL BATTLE: Durza has 150 HP and deals 25 damage per turn. Use Glimmer Ward (-15 damage) and Shadow Lash (25 damage, 15 energy) strategically!]");

        NPC durza = new NPC("Durza",
                "the Shade - a dark sorcerer whose very presence seems to drain the light from the air",
                150, true, DialogueFactory.createDurzaDialogue());
        pitOfChains.addNPC(durza);
        rooms.put("PIT_OF_CHAINS", pitOfChains);

        // Create exits
        // Watchtower Cell -> Guard Corridor
        watchtowerCell.setExit(Direction.SOUTH, new Exit(guardCorridor, "a heavy wooden door"));

        // Guard Corridor <-> Watchtower Cell, Supply Vault
        guardCorridor.setExit(Direction.NORTH, new Exit(watchtowerCell, "back to the watchtower cell"));
        guardCorridor.setExit(Direction.SOUTH, new Exit(supplyVault, "a stone archway"));

        // Supply Vault <-> Guard Corridor, Shadow Cells (blocked until Stone Shove)
        supplyVault.setExit(Direction.NORTH, new Exit(guardCorridor, "back to the corridor"));
        supplyVault.setExit(Direction.SOUTH,
                new Exit(shadowCells, "a rubble-blocked archway [requires Stone Shove spell to clear]",
                        player -> player.knowsSpell("Stone Shove")));

        // Shadow Cells <-> Supply Vault, Ritual Archive (locked until Bone Key)
        shadowCells.setExit(Direction.NORTH, new Exit(supplyVault, "back to the supply vault"));
        shadowCells.setExit(Direction.SOUTH,
                new Exit(ritualArchive, "a sealed iron gate [requires Bone Key from defeating Shade Acolyte]",
                        player -> player.hasItem("Bone Key")));

        // Ritual Archive <-> Shadow Cells, Pit of Chains (requires all 3 spells)
        ritualArchive.setExit(Direction.NORTH, new Exit(shadowCells, "back to the shadow cells"));
        ritualArchive.setExit(Direction.DOWN,
                new Exit(pitOfChains,
                        "a glowing runic portal [requires all 3 spells: Glimmer Ward, Stone Shove, Shadow Lash]",
                        player -> player.knowsSpell("Glimmer Ward") &&
                                player.knowsSpell("Stone Shove") &&
                                player.knowsSpell("Shadow Lash")));

        // Pit of Chains -> Ritual Archive (one-way back)
        pitOfChains.setExit(Direction.UP, new Exit(ritualArchive, "the runic portal"));
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public java.util.Set<String> getAllRoomIds() {
        return rooms.keySet();
    }
}
