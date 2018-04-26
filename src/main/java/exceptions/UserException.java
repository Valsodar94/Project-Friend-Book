package exceptions;

public class UserException extends Exception {
	private static final long serialVersionUID = 1862608559963555680L;

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, Exception cause) {
		super(message, cause);
	}
	
}
