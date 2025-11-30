package com.shadeslayer.model;

public class Command {
    private final String name;
    private final String description;
    private final ArgumentType argumentType;

    public Command(String name, String description, ArgumentType argumentType) {
        this.name = name;
        this.description = description;
        this.argumentType = argumentType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean requiresArgument() {
        return argumentType != ArgumentType.NONE;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }
}
