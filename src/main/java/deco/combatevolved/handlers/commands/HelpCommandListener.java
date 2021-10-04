package deco.combatevolved.handlers.commands;

import deco.combatevolved.commands.Command;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.OnScreenMessageManager;
import deco.combatevolved.managers.CommandsManager;

import java.util.List;

public class HelpCommandListener extends AbstractCommandListener {
    @Override
    public void call(List<Object> args, Integer senderId) {
        List<Command> commands = GameManager.get().getManager(CommandsManager.class).getCommands();
        for (Command command: commands) {
            GameManager.get().getManager(OnScreenMessageManager.class).addMessage(command.toString());
        }
    }
}
