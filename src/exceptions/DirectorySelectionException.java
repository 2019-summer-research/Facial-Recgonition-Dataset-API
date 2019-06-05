package exceptions;

/**
 * An exception thrown when the Dataset Interface could not get a clear response from the user as to where the
 * dataset root is located.
 */
public class DirectorySelectionException extends Exception{

	@Override
	public String toString() {
		return "There was an error selecting a directory with the JFileChooser";
	}
}
