package deco.combatevolved.entities.staticentities.defensivetowers;

import deco.combatevolved.managers.AbstractManager;

import java.util.LinkedList;
/**
 *Management of all denfensive towers placed on the grid, only managed when tower can be placed on the
 * grid. Must be a tower to be added to the grid.
 *
* */
public class TowerDefenseManager extends AbstractManager {
    //list to store towers
    private LinkedList<Tower> towers;

    /**
     * Constructs a tower manager to manage current games defensive towers placed
     *
     * */
    public TowerDefenseManager(){
        this.towers = new LinkedList<>();
    }

    /**
     * Adds a tower to the list
     *  @param tower the tower to be added to the list
     *
     * */
    public void addTower(Tower tower) {
        if (tower != null) {
            this.towers.add(tower);
        }
    }

    /**
     * Gets a tower given an index useful for when removing towers that have
     * been destroyed by an enemy.
     *
     * @param index the index of the tower
     * @return Tower class object
     *
     * */
    public Tower getTower(int index) {
        return this.towers.get(index);
    }

    /**
     * Returns a list of current towers managed
     *
     * @return list of current towers on the grid
     *
     * */
    public LinkedList<Tower> getTowers() {
        return new LinkedList<>(this.towers);
    }

    /**Removes a tower from the list of all towers
     * Called when a tower dies
     *
     * @param tower the tower being removed
     */
    public void removeTower(Tower tower) {
        this.towers.remove(tower);
    }

    /**A function to check whether a given tower is still alive
     *
     * @param tower - the tower being checked
     * @return a boolean of if the tower is alive or dead (true or false)
     */
    public boolean checkTowerAlive(Tower tower) {
        for (Tower check: this.towers) {
            if (tower.equals(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes towers with no health and and returns a list of towers, that
     * need to remove.
     * @return A list of towers to remove from the manager
     *
     * */
    public LinkedList<Tower> removeAllTowers() {
            LinkedList<Tower> towersRemove = new LinkedList<>();
            int numTowers = this.towers.size() - 1;
            for (int i = 0; i < numTowers; i++) {
                if (this.getTower(i).getHealth() <= 0) {
                    towersRemove.add(this.getTower(i));
                    this.towers.remove(i);
                }
            }
            return towersRemove;
        }

}
