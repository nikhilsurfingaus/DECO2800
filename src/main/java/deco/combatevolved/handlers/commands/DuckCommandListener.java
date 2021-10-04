package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.entities.dynamicentities.AgentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DuckCommandListener extends AbstractCommandListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DuckCommandListener.class);

    @Override
    public void call(List<Object> args, Integer senderId) {
        LOGGER.info("Spawning 1000 agents :)");
        for (int i = 0; i < 1000; ++i) {
            GameManager.get().getWorld().addEntity(new AgentEntity(0f, 0f, 0.05f));
        }
    }
}
