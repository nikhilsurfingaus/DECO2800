package deco.combatevolved.managers;

import deco.combatevolved.commands.ArgumentType;
import deco.combatevolved.commands.Command;
import deco.combatevolved.commands.CommandFactory;
import deco.combatevolved.handlers.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandsManager extends AbstractManager {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<Command>> commands;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandsManager.class);
    private Command.Permission permission;

    public CommandsManager() {
        commands = new ConcurrentHashMap<>();
        if (GameManager.get().getManager(NetworkManager.class).isHost()) {
            permission = Command.Permission.OWNER;
        } else {
            permission = Command.Permission.USER;
        }
    }

    /**
     * Add commands to the command manager
     * TODO move command additions to relevant managers
     */
    public void createCommands() {
        CommandFactory.getCommand("help", new HelpCommandListener(), Command.Permission.CLIENT);

        CommandFactory.getCommand("duck", new DuckCommandListener(), Command.Permission.ADMIN);

        Map<String, ArgumentType> kickArguments = new HashMap<>();
        kickArguments.put("user", ArgumentType.USER);
        CommandFactory.getCommand("kick", new KickCommandListener(), Command.Permission.OWNER, kickArguments);

        CommandFactory.getCommand("resources", new ResourcesCommandListener(), Command.Permission.CLIENT);

        CommandFactory.getCommand("spawn", new SpawnCommandListener(), Command.Permission.USER);

        Map<String, ArgumentType> teleportArguments = new HashMap<>();
        teleportArguments.put("fromPlayer", ArgumentType.USER);
        teleportArguments.put("toPlayer", ArgumentType.USER);
        CommandFactory.getCommand("teleport", new TeleportCommandListener(), Command.Permission.USER,
                teleportArguments);
    }

    /**
     * Add a command so that it may be called by the user.
     *
     * This is automatically called by CommandFactory when creating a command
     * @param command Command object created by CommandFactory
     */
    public void addCommand(Command command) {
        if (!commands.containsKey(command.getName())) {
            commands.put(command.getName(), new CopyOnWriteArrayList<>());
        }
        commands.get(command.getName()).add(command);
    }

    /**
     * Get the list of commands that are known to the manager
     * @return a list of the commands that are known to the manager
     */
    public List<Command> getCommands() {
        List<Command> commandList = new LinkedList<>();
        for (CopyOnWriteArrayList<Command> commandVal : commands.values()) {
            commandList.addAll(commandVal);
        }
        return commandList;
    }

    /**
     * Call a command from a raw input string
     * @param commandString user input
     */
    public void callCommand(String commandString) {
        if (commandString.startsWith("/")) {
            commandString = commandString.substring(1);
        }

        List<String> rawArgs = new LinkedList<>(Arrays.asList(commandString.split(" ")));
        String name = rawArgs.remove(0);
        callCommand(name, rawArgs);
    }

    /**
     * Call a command from a split input
     * @param name command name
     * @param rawArgs arguments passed to command
     */
    public void callCommand(String name, List<String> rawArgs) {
        if (!commands.containsKey(name)) {
            LOGGER.info("Command {} not recognised", name);
            return;
        }

        List<Object> args = null;
        for (Command command : commands.get(name)) {
            try {
                args = command.cleanArguments(rawArgs);
                callCommand(command, args, GameManager.get().getManager(NetworkManager.class).getID());
                break;
            } catch (IllegalArgumentException ignored) {}

        }
        if (args == null) {
            LOGGER.info("arguments passed to {} do not match signature", name);
        }
    }

    public void callCommand(String name, List<Object> arguments, Integer senderId) {
        for (Command command : commands.get(name)) {
            if (command.confirmArgumentTypes(arguments)) {
                callCommand(command, arguments, senderId);
                break;
            }
        }
    }

    public void callCommand(Command command, List<Object> arguments, Integer senderId) {
        if (command.checkPermission(permission)) {
            command.call(arguments, senderId);
        } else {
            GameManager.get().getManager(OnScreenMessageManager.class).addMessage(
                    "You do not have permission to use this command");
        }
    }
}
