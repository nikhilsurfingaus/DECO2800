package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;

public class TeleportCommandListener extends AbstractCommandListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeleportCommandListener.class);

    @Override
    public void call(List<Object> args, Integer senderId) {
        PlayerPeon fromPlayer = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getManager(NetworkManager.class)
                        .getPlayerEntityFromConnection((int) args.get(0)));
        PlayerPeon toPlayer = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getManager(NetworkManager.class)
                        .getPlayerEntityFromConnection((int) args.get(1)));

        fromPlayer.resetMovementTask();
        float col = toPlayer.getCol();
        float row = toPlayer.getRow();
        int height = toPlayer.getZ();
        fromPlayer.setPosition(col, row, height);
    }
}
