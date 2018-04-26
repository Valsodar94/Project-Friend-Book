package exceptions;

public class LoginException extends Exception {
	private static final long serialVersionUID = 254111159014789965L;

	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(String message, Exception cause) {
		super(message, cause);
	}
	
}
