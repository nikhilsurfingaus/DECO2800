package deco.combatevolved.entities.enemyentities;

public class EnemyPoison {

    private DesertEnemy enemy;
    private int damage;
    private long time;
    private int count;

    public EnemyPoison(DesertEnemy enemy) {
        this.enemy = enemy;
        this.damage = enemy.getPoisonDamage();
        this.time = System.currentTimeMillis();
        this.count = 0;
    }

    public int getPoisonDamage() {
        return this.damage;
    }

    public void addCount() {
        this.time = System.currentTimeMillis();
        this.count++;
    }

    public int getCount() {
        return this.count;
    }

    public long getTime() {
        return this.time;
    }

    public DesertEnemy getEnemy() {
        return this.enemy;
    }

}