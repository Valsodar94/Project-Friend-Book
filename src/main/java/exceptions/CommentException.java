package exceptions;

public class CommentException extends Exception {
	private static final long serialVersionUID = -2934121421151219922L;

	public CommentException(String message) {
		super(message);
	}

	public CommentException(String message, Exception cause) {
		super(message, cause);
	}
	
}
