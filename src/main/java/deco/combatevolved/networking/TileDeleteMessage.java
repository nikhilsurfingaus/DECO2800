package deco.combatevolved.networking;

public class TileDeleteMessage {
    private int tileID;

    /**
     * Gets the tile id to delete.
     * @return tile id to delete
     */
    public int getTileID() {
        return tileID;
    }

    /**
     * Sets the tile id to delete in the message.
     * @param tileID tile id to delete
     */
    public void setTileID(int tileID) {
        this.tileID = tileID;
    }
}