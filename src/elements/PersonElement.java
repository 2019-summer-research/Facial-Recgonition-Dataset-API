package elements;

import java.io.File;
import java.util.List;

/**
 * A wrapper class for each individual person, and the image files relating to them
 */
public class PersonElement {

	File rootDirectory;
	List<File> images;
	String personId;

	public PersonElement(List<File> images, String personId, File rootDirectory) {
		this.images = images;
		this.personId = personId;
		this.rootDirectory = rootDirectory;
	}

	public List<File> getImages() {
		return images;
	}

	public String getPersonId() {
		return personId;
	}

	public File getRootDirectory() {return rootDirectory;}
}
