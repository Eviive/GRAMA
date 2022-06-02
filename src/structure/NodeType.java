package structure;

import java.awt.Color;

/**
 * The enum representing a node's type
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public enum NodeType {
	
	CITY("Ville",new Color(220,20,20)),
	RESTAURANT("Restaurant",new Color(20,20,220)),
	RECREATION("Loisir",new Color(20,220,20));
	
	private String name;
	private Color color;
	
	NodeType(String name , Color color) {
		this.name = name;
		this.color = color;
	}
	
	/**
	 * Converts the character type in its corresponding <code>NodeType</code> constant
	 * @param text The character type
	 * @return Returns the corresponding <code>NodeType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
	public static NodeType typeOf(char text) throws LoadGraphException {
		for (NodeType type: values()) {
			if (type.name.charAt(0) == text) {
				return type;
			}
		}
		throw new LoadGraphException("Le type de noeud " + text + " n'est pas valide, les types autorisés sont V, R, ou L");
	}
	
	public Color getColor(){
		return color;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}