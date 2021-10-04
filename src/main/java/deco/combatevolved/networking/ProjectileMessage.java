package deco.combatevolved.networking;

import deco.combatevolved.entities.dynamicentities.Projectile;

public class ProjectileMessage {

    public enum ProjectileMsgType {
        ADD,
        SHOOT
    }

    private int playerEntityId;
    private Projectile projectile;
    private ProjectileMsgType projectileMsgType;
    private int screenX;
    private int screenY;

    public void setPlayerEntityId(int playerEntityId) {
        this.playerEntityId = playerEntityId;
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectileMsgType(ProjectileMsgType projectileMsgType) {
        this.projectileMsgType = projectileMsgType;
    }

    public ProjectileMsgType getProjectileMsgType() {
        return this.projectileMsgType;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public int getScreenX() {
        return this.screenX;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public int getScreenY() {
        return this.screenY;
    }
}
