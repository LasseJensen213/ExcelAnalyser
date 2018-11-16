package dataTransferObjects;

import java.util.HashMap;

public class RecurringDataEntry {

	private String year = null;
	private String month = null;
	private HashMap<String, Integer> data; 
	public RecurringDataEntry() {
		data = new HashMap<String, Integer>();
		// TODO Auto-generated constructor stub
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public HashMap<String, Integer> getData() {
		return data;
	}
	public void setData(HashMap<String, Integer> data) {
		this.data = data;
	}
	
	
	public void InsertDataElement(String key, int value) {
		data.put(key, value);
	}

}
