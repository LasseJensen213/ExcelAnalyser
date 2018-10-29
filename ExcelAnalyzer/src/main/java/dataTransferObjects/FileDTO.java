package dataTransferObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileDTO {

	
	private String sheetName;
	private String directoryName;
	private Map<String, ArrayList<String>> rows;
	private ArrayList<String> keys;
	
	
	public FileDTO(String sheetName, String directoryName) {
		rows = new HashMap<String, ArrayList<String>>();
		this.sheetName = sheetName;
		this.directoryName = directoryName;
		this.keys = new ArrayList<String>();
	}
	

	public String getFileName() {
		return sheetName;
	}


	
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}


	
	public void addRow(String key, ArrayList<String> element) {
		this.keys.add(key);
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


	@Override
	public String toString() {
		return "FileDTO [sheetName=" + sheetName + ", directoryName=" + directoryName + "]";
	}


	public ArrayList<String> getKeys() {
		return keys;
	}


	public void setKeys(ArrayList<String> keys) {
		this.keys = keys;
	}

	
	
	
	
	
}
