package deco.combatevolved.entities.staticentities;

import com.google.gson.annotations.Expose;

import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.Tile;

public class Part {
	@Expose
	Boolean obstructed;
	@Expose 
	String textureString;
	HexVector position; 
	
	/**
	 * @param position Relative position to a center position defined in Static Entity 
	 * @param textureString id String for the texture 
	 * @param obstructed whether the underlying tile for the StaticEntity part is obstructed or not 
	 */
	public Part(HexVector position, String textureString, Boolean obstructed){
		this.position = position; 
		this.textureString = textureString;
		this.obstructed = obstructed; 
	}
	
	public Part(Tile center, String textureString, Boolean obstructed){
		this(new HexVector(0,0), textureString, obstructed);	
	}
	
	public Boolean isObstructed() {
		return obstructed;
	}
	
	public String getTextureString() {
		return textureString;
	}
	
	public HexVector getPostion() {
		return position;
	}
}