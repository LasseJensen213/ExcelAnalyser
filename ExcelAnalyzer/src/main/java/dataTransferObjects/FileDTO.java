package dataTransferObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDTO {

	
	private String sheetName;
	private String directoryName;
	private Map<String, ArrayList<String>> rows;
	
	
	
	public FileDTO(String sheetName, String directoryName) {
		rows = new HashMap<String, ArrayList<String>>();
		this.sheetName = sheetName;
		this.directoryName = directoryName;
	}
	

	public String getFileName() {
		return sheetName;
	}



	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}


	
	public void addRow(String key, ArrayList<String> element) {
		this.rows.put(key, element);
	}
	
	public boolean contains(String event) {
		return rows.containsKey(event);
	}
	
	
	
	public ArrayList<String> getRow(String key) {
		return rows.get(key);
	}
	
	public int getNumberOfRows() {
		return this.rows.size();
	}


	public String getDirectoryName() {
		return directoryName;
	}

	
	
	
	
}
