package com.shadeslayer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogueTree implements Serializable {
    private final Map<String, DialogueNode> nodes;
    private final String startNodeId;
    private String currentNodeId;

    public DialogueTree(String startNodeId) {
        this.nodes = new HashMap<>();
        this.startNodeId = startNodeId;
        this.currentNodeId = startNodeId;
    }

    public void addNode(DialogueNode node) {
        nodes.put(node.getId(), node);
    }

    public DialogueNode getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public DialogueNode getCurrentNode() {
        return nodes.get(currentNodeId);
    }

    public void reset() {
        currentNodeId = startNodeId;
    }

    public boolean advanceToNode(String nodeId) {
        if (nodes.containsKey(nodeId)) {
            currentNodeId = nodeId;
            DialogueNode node = nodes.get(nodeId);
            node.onEnter();
            return true;
        }
        return false;
    }

    public List<DialogueChoice> getAvailableChoices(Player player) {
        DialogueNode current = getCurrentNode();
        if (current != null) {
            return current.getAvailableChoices(player);
        }
        return List.of();
    }

    public boolean selectChoice(int choiceIndex, Player player) {
        DialogueNode current = getCurrentNode();
        if (current == null) {
            return false;
        }

        List<DialogueChoice> choices = current.getAvailableChoices(player);
        if (choiceIndex < 0 || choiceIndex >= choices.size()) {
            return false;
        }

        DialogueChoice selected = choices.get(choiceIndex);
        selected.onSelect();
        return advanceToNode(selected.getTargetNodeId());
    }

    public boolean isAtEnd() {
        DialogueNode current = getCurrentNode();
        return current != null && current.isEndNode();
    }

    public String getStartNodeId() {
        return startNodeId;
    }
}
