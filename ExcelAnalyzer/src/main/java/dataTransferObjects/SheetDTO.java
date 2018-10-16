package dataTransferObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetDTO {

	
	private String sheetName;
	private Map<String, ArrayList<String>> rows;
	
	
	
	public SheetDTO(String sheetName) {
		rows = new HashMap<String, ArrayList<String>>();
		this.sheetName = sheetName;
	}
	

	public String getSheetName() {
		return sheetName;
	}



	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}



	public void addRow(ArrayList<String> element) {
		this.rows.put(element.get(0), element);
	}
	
	public boolean contains(String event) {
		return rows.containsKey(event);
	}
	
	
	
	public ArrayList<String> getRow(String key) {
		return rows.get(key);
	}


	
	
	
	
}
