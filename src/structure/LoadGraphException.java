package structure;

public class LoadGraphException extends Exception {
	
	public LoadGraphException() {
		super("Fichier non valide");
	}
	
	public LoadGraphException(String message) {
		super(message);
	}
	
}