package exceptions;

public class PostException extends Exception {
	private static final long serialVersionUID = 1946923317121603539L;

	public PostException(String message) {
		super(message);
	}
	
	public PostException(String message, Exception cause) {
		super(message, cause);
	}

}
