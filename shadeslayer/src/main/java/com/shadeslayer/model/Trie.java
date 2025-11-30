package com.shadeslayer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Trie (prefix tree) is a tree-based data structure used for efficient string
 * storage and retrieval.
 * Each node represents a single character, and paths from root to nodes form
 * words.
 * Tries are excellent for autocomplete, spell checking, and prefix-based
 * searches.
 */
public class Trie {

    /**
     * TrieNode represents each node in the Trie tree.
     * Each node contains:
     * - A map of child nodes (character -> TrieNode)
     * - A boolean flag indicating if this node marks the end of a valid word
     */
    static class TrieNode {
        // HashMap to store child nodes, where key is the character and value is the
        // child TrieNode
        Map<Character, TrieNode> children;
        // Flag to mark the end of a word
        boolean isEndOfWord;

        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    // Root node of the Trie - starts empty and builds as words are inserted
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     * For example, inserting "cat":
     * 1. Start at root
     * 2. Create/traverse to node 'c'
     * 3. Create/traverse to node 'a' (child of 'c')
     * 4. Create/traverse to node 't' (child of 'a')
     * 5. Mark 't' as end of word
     */
    public void insert(String word) {
        TrieNode current = root;

        // Traverse through each character in the word
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Character ch = Character.valueOf(c);// Autoboxing from char to Character
            // If the character doesn't exist as a child, create a new node
            if (!current.children.containsKey(ch)) {
                current.children.put(ch, new TrieNode());
            }
            // Move to the child node
            current = current.children.get(ch);
        }

        // Mark the last node as the end of a complete word
        current.isEndOfWord = true;
    }

    /**
     * Searches for an exact word in the Trie.
     * Returns true only if the word exists AND is marked as a complete word.
     */
    public boolean search(String word) {
        TrieNode current = root;

        // Traverse through each character
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Character ch = Character.valueOf(c); // Autoboxing from char to Character
            // If character doesn't exist, word is not in Trie
            if (!current.children.containsKey(ch)) {
                return false;
            }
            current = current.children.get(ch);
        }

        // Return true only if we reached a node marked as end of word
        return current.isEndOfWord;
    }

    /**
     * Checks if there are any words in the Trie that start with the given prefix.
     * For autocomplete: if user types "ca", this returns true if "cat", "car",
     * "cap" exist.
     */
    public boolean startsWith(String prefix) {
        TrieNode current = root;

        // Traverse through each character in the prefix
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            Character ch = Character.valueOf(c); // Autoboxing from char to Character
            if (!current.children.containsKey(ch)) {
                return false;
            }
            current = current.children.get(ch);
        }

        // If we successfully traversed all characters, prefix exists
        return true;
    }

    /**
     * Autocomplete function: returns all words that start with the given prefix.
     * This is the core functionality for autocomplete features.
     * 
     * Algorithm:
     * 1. Navigate to the node representing the last character of the prefix
     * 2. From that node, perform DFS to find all complete words
     * 3. Append each complete word to the result list
     * 
     * DFS (Depth-First Search):
     * - Explores as far as possible down one branch before backtracking
     * - In this Trie context: follows one character path completely before trying other paths
     * - Example: for prefix "ca", if paths exist for "cat", "car", "cap"
     *   DFS might explore: c→a→t (found "cat"), then backtrack to 'a', try c→a→r (found "car"), etc.
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;

        // First, navigate to the end of the prefix
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            Character ch = Character.valueOf(c); // Autoboxing from char to Character
            if (!current.children.containsKey(ch)) {
                // Prefix doesn't exist, return empty list
                return results;
            }
            current = current.children.get(ch);
        }

        // Now perform DFS from this node to find all words with this prefix
        findAllWords(current, prefix, results);
        return results;
    }

    /**
     * Helper method for autocomplete using Depth-First Search (DFS).
     * Recursively explores all paths from the current node to find complete words.
     * 
     * @param node        Current node being explored
     * @param currentWord The word built so far (starts with the prefix)
     * @param results     List to accumulate all found words
     */
    private void findAllWords(TrieNode node, String currentWord, List<String> results) {
        // If this node marks the end of a word, add it to results
        if (node.isEndOfWord) {
            results.add(currentWord);
        }

        // Recursively explore all child nodes
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            Character ch = entry.getKey();
            TrieNode childNode = entry.getValue();
            // Recursively build the word by appending each character
            findAllWords(childNode, currentWord + ch, results);
        }
    }

    /**
     * Main method demonstrating Trie usage for autocomplete functionality
     */
    public static void main(String[] args) {
        Trie trie = new Trie();

        // Build a dictionary of words
        System.out.println("=== Building Trie Dictionary ===");
        String[] words = {
                "apple", "app", "application", "apply", "apricot",
                "banana", "band", "bandana", "bandit",
                "cat", "car", "card", "care", "careful", "carry",
                "dog", "dodge", "door"
        };

        // Insert all words into the Trie
        for (String word : words) {
            trie.insert(word);
            System.out.println("Inserted: " + word);
        }

        System.out.println("\n=== Testing Search ===");
        // Test exact word search
        System.out.println("Search 'apple': " + trie.search("apple")); // true
        System.out.println("Search 'app': " + trie.search("app")); // true
        System.out.println("Search 'appl': " + trie.search("appl")); // false (not a complete word)
        System.out.println("Search 'orange': " + trie.search("orange")); // false

        System.out.println("\n=== Testing Prefix Check ===");
        // Test if prefix exists
        System.out.println("Starts with 'app': " + trie.startsWith("app")); // true
        System.out.println("Starts with 'ban': " + trie.startsWith("ban")); // true
        System.out.println("Starts with 'xyz': " + trie.startsWith("xyz")); // false

        System.out.println("\n=== Autocomplete Demo ===");
        // Demonstrate autocomplete functionality
        testAutocomplete(trie, "app");
        testAutocomplete(trie, "car");
        testAutocomplete(trie, "ban");
        testAutocomplete(trie, "do");
        testAutocomplete(trie, "z");
    }

    /**
     * Helper method to demonstrate autocomplete results
     */
    private static void testAutocomplete(Trie trie, String prefix) {
        System.out.println("\nAutocomplete for '" + prefix + "':");
        List<String> suggestions = trie.autocomplete(prefix);
        if (suggestions.isEmpty()) {
            System.out.println("  No suggestions found");
        } else {
            for (String suggestion : suggestions) {
                System.out.println("  - " + suggestion);
            }
        }
    }
}
