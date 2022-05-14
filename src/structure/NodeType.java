package structure;

public enum NodeType {
	
	CITY('V'),
	RESTAURANT('R'),
	RECREATION('L');
	
	private char csvChar;
	
	NodeType(char csvChar) {
		this.csvChar = csvChar;
	}
	
	/**
	 * Converts the character type in its corresponding <code>NodeType</code> constant
	 * @param text The character type
	 * @return Returns the corresponding <code>NodeType</code> constant
	 * @throws LoadGraphException If the type is not valid
	 */
	public static NodeType typeOf(char text) throws LoadGraphException {
		for (NodeType type: values()) {
			if (type.csvChar == text) {
				return type;
			}
		}
		throw new LoadGraphException("Node type is invalid (should be V, R, or L)");
	}
	
}