package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpawnCommandListener extends AbstractCommandListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpawnCommandListener.class);

    @Override
    public void call(List<Object> args, Integer senderId) {
        LOGGER.info("{}: Teleporting to spawn.", senderId);
        PlayerPeon entity = (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get()
                .getManager(NetworkManager.class).getPlayerEntityFromConnection(senderId));
        entity.resetMovementTask();
        entity.setPosition(0, 0, 0);
    }
}
