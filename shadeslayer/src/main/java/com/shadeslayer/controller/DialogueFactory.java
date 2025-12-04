package com.shadeslayer.controller;

import com.shadeslayer.model.DialogueNode;
import com.shadeslayer.model.DialogueTree;

public class DialogueFactory {

    public static DialogueTree createWoundedPrisonerDialogue() {
        DialogueTree tree = new DialogueTree("greeting");

        DialogueNode greeting = new DialogueNode("greeting",
                "You... you're not one of them. Thank the gods. Are you... are you here to escape?");
        greeting.addChoice("Who are you? What happened here?", "backstory");
        greeting.addChoice("I'm looking for a way out. Can you help?", "help");
        greeting.addChoice("I need to be going.", "end");
        tree.addNode(greeting);

        DialogueNode backstory = new DialogueNode("backstory",
                "I was a guard here, before Durza came. He took the dragon... chained her in the deepest pit. Those who resisted... well, you saw what became of us.");
        backstory.addChoice("Durza? The dragon?", "durza_info");
        backstory.addChoice("Can you help me?", "help");
        tree.addNode(backstory);

        DialogueNode durzaInfo = new DialogueNode("durza_info",
                "Durza, the Shade. Dark sorcerer. He's keeping Saphira - the dragon - trapped below. If you mean to face him, you'll need more than steel.");
        durzaInfo.addChoice("What do you mean?", "magic_hint");
        durzaInfo.addChoice("Tell me more.", "backstory");
        tree.addNode(durzaInfo);

        DialogueNode magicHint = new DialogueNode("magic_hint",
                "Magic. But be warned - magic takes from you. Spend it twice, pay for it thrice. Every spell drains your energy. Waste it, and you'll have nothing left when it matters most.");
        magicHint.addChoice("I'll be careful. What else can you tell me?", "teach_spell");
        tree.addNode(magicHint);

        DialogueNode teachSpell = new DialogueNode("teach_spell",
                "Here... I can teach you something. A simple ward. It won't save you from Durza's full power, but it might keep you alive long enough to learn more. Focus... feel the light within...");
        teachSpell.addChoice("Thank you. I'll use it wisely.", "farewell");
        tree.addNode(teachSpell);

        DialogueNode help = new DialogueNode("help",
                "I'm too weak to move. But I can give you knowledge. Would you like to know about magic?");
        help.addChoice("Yes, tell me about magic.", "magic_hint");
        help.addChoice("Maybe later.", "greeting");
        tree.addNode(help);

        DialogueNode farewell = new DialogueNode("farewell",
                "Go. Be smarter than I was. And if you free Saphira... tell her some of us tried.");
        tree.addNode(farewell);

        DialogueNode end = new DialogueNode("end", "Be careful out there.");
        tree.addNode(end);

        return tree;
    }

    public static DialogueTree createShadeAcolyteDialogue() {
        DialogueTree tree = new DialogueTree("hostile");

        DialogueNode hostile = new DialogueNode("hostile",
                "Intruder! The Master's domain is forbidden to your kind! You will join the shadows!");
        hostile.addChoice("[Fight]", "combat");
        tree.addNode(hostile);

        DialogueNode combat = new DialogueNode("combat", "[Combat initiated]");
        tree.addNode(combat);

        return tree;
    }

    public static DialogueTree createDurzaDialogue() {
        DialogueTree tree = new DialogueTree("initial");

        DialogueNode initial = new DialogueNode("initial",
                "Eragon... how interesting. You've made it this far. But look at you - spent, trembling. Did you think strength was infinite?");
        initial.addChoice("[Fight]", "combat_start");
        initial.addChoice("I've come to free Saphira!", "defiance");
        tree.addNode(initial);

        DialogueNode defiance = new DialogueNode("defiance",
                "Free her? You can barely stand. Every spell you wasted, every reckless strike... all led you here. Powerless.");
        defiance.addChoice("[Fight anyway]", "combat_start");
        tree.addNode(defiance);

        DialogueNode combatStart = new DialogueNode("combat_start",
                "Then die, boy. Like all the others who opposed me.");
        tree.addNode(combatStart);

        DialogueNode duringCombat = new DialogueNode("during_combat",
                "You're stronger than I expected... but it won't be enough!");
        tree.addNode(duringCombat);

        DialogueNode victory = new DialogueNode("victory",
                "Impossible... you... you've won...");
        tree.addNode(victory);

        DialogueNode defeat = new DialogueNode("defeat",
                "As I expected. Pathetic.");
        tree.addNode(defeat);

        return tree;
    }
}
