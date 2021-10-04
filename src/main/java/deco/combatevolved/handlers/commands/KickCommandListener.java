package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;

public class KickCommandListener extends AbstractCommandListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(KickCommandListener.class);

    @Override
    public void call(List<Object> args, Integer senderId) {
        GameManager.get().getManager(NetworkManager.class).removeClientConnection((Integer) args.get(0),
                GameManager.get().getManager(NetworkManager.class).getClientUsernameFromConnection((int) args.get(0)));
    }
}