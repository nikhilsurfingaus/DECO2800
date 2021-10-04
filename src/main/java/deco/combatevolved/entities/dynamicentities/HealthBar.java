package deco.combatevolved.entities.dynamicentities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthBar extends Sprite {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthBar.class);

    private Sprite background;
    private Sprite foreground;
    private transient BasicEnemy host;
    private Tower tower;

    public HealthBar(BasicEnemy host, Texture background, Texture foreground) {
        this.host = host;

        this.background = new Sprite(background);
        this.foreground = new Sprite(foreground);

        this.background.setX(WorldUtil.colRowToWorldCords(host.getCol(), host.getRow())[0]);
        this.background.setY(WorldUtil.colRowToWorldCords(host.getCol(), host.getRow())[1]);
        this.foreground.setX(this.getBackground().getX());
        this.foreground.setY(this.getForeground().getY());
        this.foreground.setOrigin(0, 0);
    }

    public HealthBar(Tower tower, Texture background, Texture foreground) {
        this.tower = tower;

        this.background = new Sprite(background);
        this.foreground = new Sprite(foreground);

        this.background.setX(WorldUtil.colRowToWorldCords(tower.getCol(), tower.getRow())[0]);
        this.background.setY(WorldUtil.colRowToWorldCords(tower.getCol(), tower.getRow())[1]);
        this.foreground.setX(this.getBackground().getX());
        this.foreground.setY(this.getForeground().getY());
        this.foreground.setOrigin(0, 0);
    }

    public BasicEnemy getHost() {
        return host;
    }

    public Tower getTower() {return tower;}

    public Sprite getBackground() {
        return background;
    }

    public Sprite getForeground() {
        return foreground;
    }

    public void update() {
        this.getBackground().setX(WorldUtil.colRowToWorldCords(this.getHost().getCol(), this.getHost().getRow())[0]);
        this.getBackground().setY(WorldUtil.colRowToWorldCords(this.getHost().getCol(), this.getHost().getRow())[1]);
        this.getForeground().setX(WorldUtil.colRowToWorldCords(this.getHost().getCol(), this.getHost().getRow())[0]);
        this.getForeground().setY(WorldUtil.colRowToWorldCords(this.getHost().getCol(), this.getHost().getRow())[1]);

        this.getForeground().setScale(this.getHost().getHealth() / (float) this.getHost().getMaxHealth(), 1f);
    }

    public void towerUpdate() {
        this.getBackground().setX(WorldUtil.colRowToWorldCords(this.getTower().getCol(), this.getTower().getRow())[0]);
        this.getBackground().setY(WorldUtil.colRowToWorldCords(this.getTower().getCol(), this.getTower().getRow())[1]);
        this.getForeground().setX(WorldUtil.colRowToWorldCords(this.getTower().getCol(), this.getTower().getRow())[0]);
        this.getForeground().setY(WorldUtil.colRowToWorldCords(this.getTower().getCol(), this.getTower().getRow())[1]);

        this.getForeground().setScale(this.getTower().getHealth() / (float) this.getTower().getMaxHealth(), 1f);
    }

    public void render(Batch batch) {
        this.getBackground().draw(batch);
        this.getForeground().draw(batch);
    }

    public void setOwner(BasicEnemy owner) {
        this.host = owner;
    }
}
