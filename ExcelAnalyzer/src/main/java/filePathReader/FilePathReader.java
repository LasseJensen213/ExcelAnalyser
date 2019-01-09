package filePathReader;

import java.io.File;
import java.util.ArrayList;

import dataTransferObjects.FileInformationDTO;
import exceptions.IncorrectDirectoryException;
import globalValues.GlobalValues;

public class FilePathReader {

	private ArrayList<FileInformationDTO> fileNames;

	private boolean isFolder = false;

	private int numberOfFolders = 0;

	private final String userDirectory;
	private String filesFolderPathName = "\\%s\\";

	public FilePathReader() {
		super();
		GlobalValues values = GlobalValues.getInstance();
		filesFolderPathName = String.format(filesFolderPathName, values.getNameDataFolder());
		this.userDirectory = System.getProperty("user.dir");
		this.fileNames = new ArrayList<FileInformationDTO>();
	}

	public void readFileNames() throws IncorrectDirectoryException {
		// Get the absolute path
		String filesfolderPath = this.userDirectory + this.filesFolderPathName;

		// Create the file to the directory
		File toSortFolder = new File(filesfolderPath);

		// List all the files in the directory
		File[] files = toSortFolder.listFiles();

		if (files.length == 0) {
			throw new IncorrectDirectoryException("No files in directory");
		}

		// Check whether all the files is a directory or a file
		boolean isFolder = false;
		boolean isFile = false;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				isFolder = true;
			}
			if (files[i].isFile()) {
				isFile = true;
			}
		}
		// To both have folders and files in the to sort folder is not allowed. Throw an
		// exception if it's the case.
		if (isFolder && isFile) {
			throw new IncorrectDirectoryException("Folder and files in same directory");
		}
		// Handle the case where it's only folders in the directory
		else if (isFolder && !isFile) {
			this.isFolder = true;
			this.numberOfFolders = files.length;

			String newPath;
			File directory;
			File[] directoryFiles;

			// Go through each directory to get the file paths
			for (int i = 0; i < files.length; i++) {

				newPath = files[i].getPath();
				directory = new File(newPath);
				directoryFiles = directory.listFiles();

				// If the foldes is empty, throw an exception
				if (directoryFiles.length == 0) {
					throw new IncorrectDirectoryException("Folder empty: " + newPath);
				}

				for (int j = 0; j < directoryFiles.length; j++) {
					if (directoryFiles[j].isFile()) {
						System.out.println("File: " + directoryFiles[j].getName());
						this.fileNames.add(new FileInformationDTO(directoryFiles[j].getName(), directory.getName()));
					}
				}
			}
		}
		// Handle the case where it's only files in the directory
		else if (!isFolder && isFile) {
			this.isFolder = false;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					// System.out.println("File: " + files[i].getName());
					this.fileNames.add(new FileInformationDTO(files[i].getName(), toSortFolder.getName()));
				}
			}
		}

	}

	public ArrayList<FileInformationDTO> getFileNames() {
		return fileNames;
	}

	public boolean isFolder() {
		return this.isFolder;
	}

	public int getNumberOfFolders() {
		return numberOfFolders;
	}

}
