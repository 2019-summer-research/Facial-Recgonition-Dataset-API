import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DatasetUtils {

	/**
	 * A utility function for copying a directory to another location
	 * @param sourceDirectory The source that is to be moved
	 * @param destinationDirectory The location where the source should be moved to
	 * @return true if the operation succeeds, false otherwise
	 */
	public static boolean copyDirectory(File sourceDirectory, File destinationDirectory) {
		try {
			Files.walkFileTree(Paths.get(sourceDirectory.getPath()), new SimpleFileVisitor<Path>() {

				//TODO: preVisitDirectory does not create the directories possible, bugfix required
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(destinationDirectory.toPath().resolve(sourceDirectory.toPath().relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					Files.copy(file, destinationDirectory.toPath().resolve(sourceDirectory.toPath().relativize(file)));
					return FileVisitResult.CONTINUE;
				}
			});

		} catch (IOException ex) {
			// There was an error accessing the file paths. Warn the user and return false.
			System.err.println("[Error] Failed to copy directory " + sourceDirectory.getAbsolutePath() + " to " +
					destinationDirectory.getAbsolutePath());
			return false;
		}

		return true;
	}

}
