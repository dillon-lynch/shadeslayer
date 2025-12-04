package com.shadeslayer.model;

import java.io.Serializable;

public class OutputLine implements Serializable {
    private String text;

    public OutputLine(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
