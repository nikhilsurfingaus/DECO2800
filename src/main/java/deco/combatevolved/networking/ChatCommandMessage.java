package deco.combatevolved.networking;

import java.util.List;

public class ChatCommandMessage {
    private String commandName;
    private List<Object> arguments;

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    public List<Object> getArguments() {
        return arguments;
    }
}
