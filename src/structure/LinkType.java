package structure;

/**
 * The enum representing a link's type
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public enum LinkType {
	
	HIGHWAY('A'),
	NATIONAL('N'),
	DEPARTMENTAL('D');
	
	private char csvChar;
	
	LinkType(char csvChar) {
		this.csvChar = csvChar;
	}
	
	/**
	 * Converts the character type in its corresponding <code>LinkType</code> constant
	 * @param text The character type
	 * @return Returns the corresponding <code>LinkType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
	public static LinkType typeOf(char text) throws LoadGraphException {
		for (LinkType type: values()) {
			if (type.csvChar == text) {
				return type;
			}
		}
		throw new LoadGraphException("Le type de lien " + text + " n'est pas valide, les types autorisés sont A, N, ou D");
	}
	
}