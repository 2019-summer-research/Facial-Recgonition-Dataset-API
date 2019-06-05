import elements.PersonElement;
import exceptions.DirectorySelectionException;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * A class which handles operations on a Datset. Note that the dataset MUST be organised in the file structure
 * layout as defined in the project readme.
 */
public class DatasetFolderManager {

	/**
	 * The root directory for the dataset. Each child directory is expected to be a person. Each file inside each of
	 * the child directories is expected to be an image of the person.
	 */
	File rootDirectory;

	/**
	 * The original parsed dataset
	 */
	ArrayList<PersonElement> datasetList;

	/**
	 * A list which may be modified while retaining the original datasetList
	 */
	ArrayList<PersonElement> modifiableList;

	public DatasetFolderManager() throws DirectorySelectionException {

		// Open up a JFileChooser to select the dataset directory.
		//JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		JFileChooser jfc = new JFileChooser("C:\\Users\\Conrad\\Pictures\\Wallpaper\\");

		// Ensure that we're only selecting directories. Disabling the 'all files' option
		jfc.setDialogTitle("Select your dataset root directory");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);

		int returnValue = jfc.showOpenDialog(null);

		// Set the class directory to be the one that the user has selected
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			this.rootDirectory = jfc.getSelectedFile();
		}

		else {
			// There was an error with choosing a directory. Throw an exception.
			throw new DirectorySelectionException();
		}

		System.out.println("Chosen directory: " + rootDirectory.getAbsolutePath());

		this.initDataset();

	}

	/**
	 * A function which takes the root directory obtained through the constructor, and arranges a Vector
	 * containing another Vector of persons
	 */
	private void initDataset() {

		datasetList = new ArrayList<>();
		int picturesLoadedCounter = 0;

		// For each directory in the root directory...
		for(File childFile : Objects.requireNonNull(rootDirectory.listFiles())) {

			// If it's not a directory, ignore it
			if(!childFile.isDirectory()) {
				continue;
			}

			// Create an entry in the map for each image. If there are none, print a notice to console and continue
			try {
				List<File> loadedFiles = Arrays.asList(childFile.listFiles(new ImageFileFilter()));
				datasetList.add(new PersonElement(loadedFiles, childFile.getName(), childFile));
				picturesLoadedCounter += loadedFiles.size();
			} catch(NullPointerException ex) {
				System.out.println("[Info] Directory " + childFile.getName() + " has no valid images");
			}
		}

		// Print some info about the loading process
		System.out.println("Initialization Complete - " + datasetList.size() + " people loaded. " + picturesLoadedCounter +
				" images loaded.");

		//TODO: Thinking emoji...
		this.modifiableList = this.datasetList;
	}

	/**
	 * Samples the database for a certain number of people randomly
	 * @param numPersons the number of people you'd like to sample from the dataset
	 * @param removeFromDataset if this is true, the function will remove the people it's sampled from the accessible dataset
	 *                          thus not allowing duplicate entries from future calls
	 * @return a list of personelements
	 */
	public List<PersonElement> samplePersons(int numPersons, boolean removeFromDataset) {
		Random random = new Random();
		List<PersonElement> selectedPeople = new ArrayList<>();

		// Handle an edge case if the user has requested more people than the dataset has to offer, limiting it to the set max
		if(numPersons > modifiableList.size()) {
			System.out.println("[Warning] Requested " + numPersons + " people from the dataset. There currently is only " +
					modifiableList.size() + " available. Limiting to that amount.");

			numPersons = modifiableList.size();
		}

		// Begin sampling random people from the list
		for(int i = 0; i <= numPersons; i++) {

			int personIndex = random.nextInt(modifiableList.size());
			selectedPeople.add(modifiableList.get(personIndex));

			// If the flag is set to remove from the dataset, do such here
			if(removeFromDataset) {
				modifiableList.remove(personIndex);
			}
		}
		return selectedPeople;
	}

	/**
	 * A function which takes a list of people, and makes a copy of the current sample to a temporary location on disk
	 * %appdata%/dataset/xxxxx while on a windows machine.
	 * @param people A sample of people to be copied to another location on disk.
	 */
	public void processSampleListToDisk(List<PersonElement> people) {

		if(people == null) {
			System.err.println("[Error] - Bad call processing sample list to disk");
			return;
		}

		// We want to save to %appdata%/dataset - Verify that the directory exists
		final String fileSystemLocation = System.getenv("APPDATA") + File.separator + "dataset" + File.separator;
		File rootFile = new File(fileSystemLocation);

		// If the directory doesn't exist, create it
		if(!rootFile.exists()) {
			if(!rootFile.mkdir()) {
				System.err.println("[Error] - Failed to create directory: " + fileSystemLocation + " - Processing samples to " +
						"disk will fail. Aborting function.");
				return;
			}
		}

		// Create a new file in this directory with the current timestamp as a title
		File sampleFile = new File(fileSystemLocation + File.separator + System.currentTimeMillis());
		if(!sampleFile.mkdir()) {
			System.err.println("[Error] - Failed to create directory: " + sampleFile.getAbsolutePath() + " for sample file. Aborting function.");
			return;
		}

		// Copy each of the people directories to the newly created sample file.
		for(PersonElement person : people) {
			DatasetUtils.copyDirectory(person.getRootDirectory(), sampleFile);
		}

		System.out.println("Copied sample dataset to " + sampleFile.getAbsolutePath());

	}

}
