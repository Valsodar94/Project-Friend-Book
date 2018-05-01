package exceptions;

public class LikeException extends Exception {

	public LikeException() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = -8222777196691521842L;

	public LikeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public LikeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LikeException(String arg0) {
		super(arg0);
	}

	public LikeException(Throwable arg0) {
		super(arg0);
	}


}
