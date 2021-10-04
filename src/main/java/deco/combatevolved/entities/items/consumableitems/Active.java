package deco.combatevolved.entities.items.consumableitems;

public interface Active {

    /**
     * Reverts stats back to original values after the time duration of consumable has run out
     */
    void revertNormal();
}
