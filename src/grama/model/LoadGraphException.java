package grama.model;

/**
 * An exception for when the graph's file isn't valid
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public final class LoadGraphException extends Exception {
	
	public LoadGraphException() {
		super("Fichier non valide");
	}
	
	public LoadGraphException(String message) {
		super(message);
	}
	
}