package grama.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The enum representing a node's type
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public enum NodeType {
	
	CITY("Ville", "./src/grama/view/city.png"),
	RESTAURANT("Restaurant", "./src/grama/view/restaurant.png"),
	RECREATION("Loisir", "./src/grama/view/recreation.png");
	
	private String name;
	private BufferedImage image;
	
	NodeType(String name, String imageSrc) {
		this.name = name;
		
		try {
			this.image = ImageIO.read(new File(imageSrc));
		} catch (IOException e) {
			System.err.println("Warning : Image couldn't be loaded");
		}
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
	
	public BufferedImage getImage(){
		return image;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}