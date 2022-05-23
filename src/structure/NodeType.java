package structure;

/**
 * The enum representing a node's type
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public enum NodeType {
	
	CITY("Ville"),
	RESTAURANT("Restaurant"),
	RECREATION("Loisir");
	
	private String name;
	
	NodeType(String name) {
		this.name = name;
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
	
	@Override
	public String toString() {
		return name;
	}
	
}