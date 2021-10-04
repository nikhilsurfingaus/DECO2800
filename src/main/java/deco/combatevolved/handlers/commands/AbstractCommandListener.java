package deco.combatevolved.handlers.commands;

import java.util.List;

public abstract class AbstractCommandListener {
    public abstract void call(List<Object> args, Integer senderId);
}
