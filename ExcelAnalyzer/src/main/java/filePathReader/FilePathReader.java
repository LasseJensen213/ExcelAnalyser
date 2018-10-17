package filePathReader;

import java.io.File;
import java.util.ArrayList;

public class FilePathReader {
	
	private ArrayList<String> fileNames;
	
	private final String userDirectory;
	private final String filesFolderPathName = "\\files\\";

	
	
	public FilePathReader() {
		super();
		this.userDirectory = System.getProperty("user.dir");
		this.fileNames = new  ArrayList<String>();
	}





	public void readFileNames()  {
		//Get the absolute path
		String filesfolderPath = this.userDirectory + this.filesFolderPathName;
		
		File toSortFolder = new File(filesfolderPath);
		File[] filesToSort = toSortFolder.listFiles();
		
		
		for(int i = 0;i<filesToSort.length;i++) {
			if(filesToSort[i].isFile()) {
				System.out.println("File: " + filesToSort[i].getName());
				this.fileNames.add(filesToSort[i].getName());
			}
		}
	}





	public ArrayList<String> getFileNames() {
		return fileNames;
	}
	
	
	
	
	
}
