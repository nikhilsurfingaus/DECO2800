package deco.combatevolved.managers;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.StaticEntity;
import java.util.Queue;

/**
 * Manager that houses the updates to entities for sending them from the host to the client
 */

public class UpdateEntityManager extends AbstractManager{
    private Queue<AbstractEntity> entityQueue;

    public void addEntityToUpdateQueue(AbstractEntity e) {
        if ((!(e instanceof StaticEntity)) && e instanceof PlayerPeon && e != null) {
            entityQueue.add(e);
        }
    }

    public AbstractEntity getEntityForSending() {
        return entityQueue.poll();
    }
}
