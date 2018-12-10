package exceptions;

public class IncorrectConfigFileException extends Exception {

	public IncorrectConfigFileException(String line) {
		super(line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
