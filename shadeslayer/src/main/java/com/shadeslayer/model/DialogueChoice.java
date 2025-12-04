package com.shadeslayer.model;

import java.io.Serializable;
import java.util.function.Predicate;

public class DialogueChoice implements Serializable {
    private final String text;
    private final String targetNodeId;
    private final Runnable onSelect;
    private final Predicate<Player> conditionToShow;

    public DialogueChoice(String text, String targetNodeId) {
        this(text, targetNodeId, null, null);
    }

    public DialogueChoice(String text, String targetNodeId, Runnable onSelect) {
        this(text, targetNodeId, onSelect, null);
    }

    public DialogueChoice(String text, String targetNodeId, Runnable onSelect, Predicate<Player> condition) {
        this.text = text;
        this.targetNodeId = targetNodeId;
        this.onSelect = onSelect;
        this.conditionToShow = condition;
    }

    public String getText() {
        return text;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public boolean isAvailable(Player player) {
        return conditionToShow == null || conditionToShow.test(player);
    }

    public void onSelect() {
        if (onSelect != null) {
            onSelect.run();
        }
    }
}
