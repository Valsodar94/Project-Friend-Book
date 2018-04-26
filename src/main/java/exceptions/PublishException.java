package exceptions;

public class PublishException extends Exception {
	private static final long serialVersionUID = 1946923317121603539L;

	public PublishException(String message) {
		super(message);
	}
	
	public PublishException(String message, Exception cause) {
		super(message, cause);
	}

}
