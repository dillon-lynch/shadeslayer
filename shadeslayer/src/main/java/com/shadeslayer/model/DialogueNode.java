package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DialogueNode implements Serializable {
    private final String id;
    private final String text;
    private final List<DialogueChoice> choices;
    private final Runnable onEnter;
    private final Predicate<Player> condition;

    public DialogueNode(String id, String text) {
        this(id, text, null, null);
    }

    public DialogueNode(String id, String text, Runnable onEnter) {
        this(id, text, onEnter, null);
    }

    public DialogueNode(String id, String text, Runnable onEnter, Predicate<Player> condition) {
        this.id = id;
        this.text = text;
        this.choices = new ArrayList<>();
        this.onEnter = onEnter;
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<DialogueChoice> getChoices() {
        return new ArrayList<>(choices);
    }

    public List<DialogueChoice> getAvailableChoices(Player player) {
        List<DialogueChoice> available = new ArrayList<>();
        for (DialogueChoice choice : choices) {
            if (choice.isAvailable(player)) {
                available.add(choice);
            }
        }
        return available;
    }

    public void addChoice(DialogueChoice choice) {
        choices.add(choice);
    }

    public void addChoice(String text, String targetNodeId) {
        choices.add(new DialogueChoice(text, targetNodeId));
    }

    public void addChoice(String text, String targetNodeId, Predicate<Player> condition) {
        choices.add(new DialogueChoice(text, targetNodeId, null, condition));
    }

    public void addChoice(String text, String targetNodeId, Runnable onSelect, Predicate<Player> condition) {
        choices.add(new DialogueChoice(text, targetNodeId, onSelect, condition));
    }

    public boolean canEnter(Player player) {
        return condition == null || condition.test(player);
    }

    public void onEnter() {
        if (onEnter != null) {
            onEnter.run();
        }
    }

    public boolean isEndNode() {
        return choices.isEmpty();
    }
}
