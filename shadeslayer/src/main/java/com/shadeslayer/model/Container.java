package com.shadeslayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Container<T extends Usable> implements Serializable {
    private final List<T> contents;

    public Container() {
        this.contents = new ArrayList<>();
    }

    public void add(T item) {
        contents.add(item);
    }

    public boolean remove(T item) {
        return contents.remove(item);
    }

    public T findByName(String name) {
        for (T item : contents) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public boolean contains(String name) {
        return findByName(name) != null;
    }

    public boolean contains(T item) {
        return contents.contains(item);
    }

    public List<T> getAll() {
        return new ArrayList<>(contents);
    }

    public int size() {
        return contents.size();
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public void clear() {
        contents.clear();
    }
}
