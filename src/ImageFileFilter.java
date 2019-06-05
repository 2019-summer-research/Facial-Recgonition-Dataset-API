import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter  implements FileFilter {

	private final String[] okFileExtensions = new String[] {"jpg", "png"};

	@Override
	public boolean accept(File pathname) {
		for(String extension: okFileExtensions) {
			if(pathname.getName().toLowerCase().endsWith(extension)) {
				return true;
			}
		}

		return false;
	}
}
