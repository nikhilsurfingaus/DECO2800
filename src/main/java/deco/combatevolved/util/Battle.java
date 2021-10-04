package deco.combatevolved.util;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;

public class Battle {

    private Battle() {}

    public static void commenceBattle(PlayerPeon peon1, PlayerPeon peon2) throws InterruptedException {
        while (true) {
            if ("Battle Over".equals(engage(peon1, peon2)) || "Battle Over".equals(engage(peon2, peon1))) {
                System.out.println("Battle Over");
                break;
            }
        }
    }

    public static String engage(PlayerPeon peon1, PlayerPeon peon2) throws InterruptedException {
        int damageCaused = peon1.attack() - peon2.defend();

        if (damageCaused > 0) {
            peon2.loseHealth(damageCaused);
        } else {
            damageCaused = 0;
        }

        System.out.printf("%s attacks %s and deals %d damage\n", peon1.getEntityID(), peon2.getEntityID(), damageCaused);

        Thread.sleep(1000);

        if (peon2.getHealth() < 0) {
            System.out.printf("%s deceased\n", peon2.getEntityID());
            return "Battle Over";
        } else {
            return "Continue";
        }
    }

}
