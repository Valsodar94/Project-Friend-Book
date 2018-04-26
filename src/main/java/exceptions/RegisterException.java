package exceptions;

public class RegisterException extends Exception {
	private static final long serialVersionUID = 708201786098142276L;

	public RegisterException(String message) {
		super(message);
	}	
	
	public RegisterException(String message, Exception cause) {
		super(message, cause);
	}

}
