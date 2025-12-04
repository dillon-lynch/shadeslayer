package com.shadeslayer.model;

import java.io.Serializable;

@FunctionalInterface
public interface SerializablePredicate<T> extends Serializable {
    boolean test(T t);
}
