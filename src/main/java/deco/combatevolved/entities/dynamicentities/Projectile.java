package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.util.WorldUtil;

public class Projectile extends AgentEntity {

    protected transient AbstractTask task;

    private static boolean exists = false;

    private PlayerPeon host;

    HexVector velocity;

    public Projectile(float row, float column, float speed) {
        super(row, column, speed);

        this.host = host;
        velocity = new HexVector();
        this.setTexture("projectile");

        if (!exists) {
            exists = true;

            GameManager.get().setProjectileID(getEntityID());

        }
    }

    public double distanceToEnemy(BasicEnemy enemy) {
        float projectileX = WorldUtil.colRowToWorldCords(this.getCol(), this.getRow())[0];
        float projectileY = WorldUtil.colRowToWorldCords(this.getCol(), this.getRow())[1];
        float enemyX = WorldUtil.colRowToWorldCords(enemy.getCol(), enemy.getRow())[0];
        float enemyY = WorldUtil.colRowToWorldCords(enemy.getCol(), enemy.getRow())[1];
        return Math.sqrt((projectileX - enemyX) * (projectileX - enemyX) +
                (projectileY - enemyY) * (projectileY - enemyY));
    }

    public int hitTarget(BasicEnemy enemy) {
        if (this.distanceToEnemy(enemy) < 150) {
            return 1;
        } else if (this.distanceToEnemy(enemy) < 300) {
            return 2;
        } else if (this.distanceToEnemy(enemy) < 450) {
            return 3;
        }
        return 0;
    }

}
































