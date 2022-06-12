package grama.model;

import swing.stroke.DoubleStroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * The enum representing a link's type
 * @author VAILLON Albert
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public enum LinkType {
	
	HIGHWAY("Autoroute", new Color(220,20,20), new DoubleStroke(2.5f,1f)),
	NATIONAL("National", new Color(19, 217, 15), new BasicStroke(1.5f)),
	DEPARTMENTAL("Départemental", new Color(20,20,220), new BasicStroke(1.25f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 12.0f, new float[]{6f}, 0.0f));
	
	private String name;
	private Color color;
	private Stroke stroke;
	
	LinkType(String name, Color color, Stroke stroke) {
		this.name = name;
		this.color = color;
		this.stroke = stroke;
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
	
	public Stroke getStroke(){
		return stroke;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}