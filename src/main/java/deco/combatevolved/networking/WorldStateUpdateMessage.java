package deco.combatevolved.networking;

import deco.combatevolved.handlers.WorldState;

public class WorldStateUpdateMessage {
    private WorldState worldState;

    public WorldStateUpdateMessage(WorldState worldState) {
        this.worldState = worldState;
    }

    public WorldState getWorldState() {
        return worldState;
    }
}
