package deco.combatevolved.commands;

import deco.combatevolved.handlers.commands.AbstractCommandListener;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.CommandsManager;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private CommandFactory() {

    }

    public static Command getCommand(String name, AbstractCommandListener listener, Command.Permission permission) {
        return getCommand(name, listener, permission, "", new HashMap<>());
    }

    public static Command getCommand(String name, AbstractCommandListener listener, Command.Permission permission,
                                     Map<String, ArgumentType> arguments) {
        return getCommand(name, listener, permission, "", arguments);
    }

    public static Command getCommand(String name, AbstractCommandListener listener, Command.Permission permission,
                                     String description) {
        return getCommand(name, listener, permission, description, new HashMap<>());
    }

    public static Command getCommand(String name, AbstractCommandListener listener, Command.Permission permission,
                                     String description, Map<String, ArgumentType> arguments) {
        Command command = new Command();
        command.setName(name);
        command.setListener(listener);
        command.setPermission(permission);
        command.setDescription(description);
        for (Map.Entry<String, ArgumentType> arg : arguments.entrySet()) {
            command.addArgument(arg.getKey(), arg.getValue());
        }
        if (!GameManager.get().getManager(CommandsManager.class).getCommands().contains(command)) {
            GameManager.get().getManager(CommandsManager.class).addCommand(command);
        }
        return command;
    }
}
