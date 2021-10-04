package deco.combatevolved.commands;

import deco.combatevolved.handlers.commands.AbstractCommandListener;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Command {
    public enum Permission {
        CLIENT, USER, ADMIN, OWNER
    }



    private String name;
    private AbstractCommandListener listener;
    private String description;
    private CopyOnWriteArrayList<AbstractMap.SimpleEntry<String, ArgumentType>> arguments;
    private Permission permission;

    private static final List<Class> VALID_TYPES = Arrays.asList(String.class, Integer.class);

    public Command() {
        arguments = new CopyOnWriteArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListener(AbstractCommandListener listener) {
        this.listener = listener;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * Register an argument that is required for the command
     *
     * @param argumentName user-friendly name for argument
     * @param type         the type of the argument, currently only support String and Integer
     */
    public void addArgument(String argumentName, ArgumentType type) {
        arguments.add(new AbstractMap.SimpleEntry<>(argumentName, type));
    }

    public Map<String, ArgumentType> getArguments() {
        Map<String, ArgumentType> argumentMap = new HashMap<>();
        for (Map.Entry<String, ArgumentType> entry : arguments) {
            argumentMap.put(entry.getKey(), entry.getValue());
        }
        return argumentMap;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AbstractCommandListener getListener() {
        return listener;
    }

    public Permission getPermission() {
        return permission;
    }

    public boolean checkPermission(Enum userPermission) {
        if (permission == Permission.USER || permission == Permission.CLIENT) {
            return true;
        }

        if (userPermission == Permission.OWNER) {
            return true;
        }

        return permission == userPermission;
    }

    public List<Object> cleanArguments(List<String> args) {
        List<Object> newArgs = new LinkedList<>();
        if (args.size() < arguments.size()) {
            throw new IllegalArgumentException("Too few arguments");
        }

        if (args.size() > arguments.size()) {
            throw new IllegalArgumentException("Too many arguments");
        }

        for (int i = 0; i < args.size(); i++) {
            String argName = arguments.get(i).getKey();
            ArgumentType type = arguments.get(i).getValue();

            switch (type) {
                case TEXT:
                    newArgs.add(args.get(i));
                    break;
                case NUMBER:
                    try {
                        Integer val = Integer.parseInt(args.get(i));
                        newArgs.add(val);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format(
                                "Value %s for argument %s does not match Integer format", args.get(i), argName));
                    }
                    break;
                case USER:
                    Integer connectionId = GameManager.get().getManager(NetworkManager.class)
                            .getConnectionIdFromUsername(args.get(i));
                    if (connectionId == null) {
                        throw new IllegalArgumentException(String.format("%s is not a valid username", args.get(i)));
                    }
                    newArgs.add(connectionId);
                    break;
                default:
                    break;
            }
        }

        return newArgs;
    }

    public boolean confirmArgumentTypes(List<Object> args) {
        for (int i = 0; i < args.size(); i++) {
            switch (arguments.get(i).getValue()) {
                case TEXT:
                    return args.get(i) instanceof String;
                case NUMBER:
                    return args.get(i) instanceof Integer;
                case USER:
                    if (args.get(i) instanceof Integer) {
                        return GameManager.get().getManager(NetworkManager.class).getUserConnections()
                                .containsKey(args.get(i));
                    }
                    return false;
                default:
                    break;
            }
        }
        return true;
    }

    public void call(List<Object> args, Integer senderId) {
        if (permission == Permission.CLIENT || GameManager.get().getManager(NetworkManager.class).isHost()) {
            listener.call(args, senderId);
        } else {
            GameManager.get().getManager(NetworkManager.class).sendCommand(name, args);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(getName());
        for (Map.Entry<String, ArgumentType> entry : arguments) {
            string.append(String.format(" <%s:%s>", entry.getKey(), entry.getValue()));
        }

        return string.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Command)) {
            return false;
        }
        return obj.toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return name.length() * 31;
    }
}
