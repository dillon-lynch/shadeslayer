package com.shadeslayer.model;

import java.util.List;

public class ParsedCommand {
    private final Command command;
    private final List<String> arguments;
    private final String error;

    public ParsedCommand(Command command, List<String> arguments, String error) {
        this.command = command;
        this.arguments = arguments;
        this.error = error;
    }

    public boolean isValid() {
        return error == null && command != null;
    }

    public Command getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public String getError() {
        return error;
    }
}
