package dataTransferObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class RecurringDataEntry {

	private String fileName = null;
	private HashMap<String, Integer> data;
	private ArrayList<String> keys;

	public RecurringDataEntry() {
		data = new HashMap<String, Integer>();
		keys = new ArrayList<String>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashMap<String, Integer> getData() {
		return data;
	}

	public void setData(HashMap<String, Integer> data) {
		this.data = data;
	}

	public void InsertDataElement(String key, double value) {
		data.put(key, (int) value);
		keys.add(key);
	}

	public ArrayList<String> getKeys() {
		return keys;
	}

}
