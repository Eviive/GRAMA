package grama.model;

import java.awt.Color;

/**
 * The enum representing a link's type
 * @author VAILLON Albert
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public enum LinkType {
	
	HIGHWAY("Autoroute",new Color(220,20,20)),
	NATIONAL("National",new Color(19, 217, 15)),
	DEPARTMENTAL("Départemental",new Color(20,20,220));
	
	private String name;
	private Color color;
	
	LinkType(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	/**
	 * Converts the character type in its corresponding <code>LinkType</code> constant
	 * @param text The character type
	 * @return Returns the corresponding <code>LinkType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
	public static LinkType typeOf(char text) throws LoadGraphException {
		for (LinkType type: values()) {
			if (type.name.charAt(0) == text) {
				return type;
			}
		}
		throw new LoadGraphException("Le type de lien " + text + " n'est pas valide, les types autorisés sont A, N, ou D");
	}

	public char getCharID() {
		return name.charAt(0);
	}
	
	public Color getColor(){
		return color;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}