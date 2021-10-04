package deco.combatevolved.entities;

import deco.combatevolved.entities.enemyentities.EnemyPoison;
import deco.combatevolved.entities.enemyentities.DesertEnemy;

public interface Poison {

    void poison (DesertEnemy enemy);

    void dealPoisonDamage(EnemyPoison enemyPoison);

    void checkPoison();
}
