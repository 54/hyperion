package org.hyperion.rs2.model;

/**
 * Represents a single game object.
 * @author Graham
 *
 */
public class GameObject {

	/**
	 * The location.
	 */
	private Location location;
	
	/**
	 * The definition.
	 */
	private GameObjectDefinition definition;
	
	/**
	 * The type.
	 */
	private int type;
	
	/**
	 * The face.
	 */
	private int face;
	
	/**
	 * Creates the game object.
	 * @param definition The definition.
	 * @param location The location.
	 * @param type The type.
	 * @param face The face.
	 */
	public GameObject(GameObjectDefinition definition, Location location, int type, int face) {
		this.definition = definition;
		this.location = location;
		this.type = type;
		this.face = face;
	}
	
	/**
	 * Gets the location.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Gets the definition.
	 * @return The definition.
	 */
	public GameObjectDefinition getDefinition() {
		return definition;
	}
	
	/**
	 * Gets the type.
	 * @return The type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Gets the face.
	 * @return The face.
	 */
	public int getFace() {
		return face;
	}

}
