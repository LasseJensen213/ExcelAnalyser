package dataTransferObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class RecurringDataEntry {

	private String year = null;
	private String month = null;
	private HashMap<String, Integer> data;
	private ArrayList<String> keys;

	public RecurringDataEntry() {
		data = new HashMap<String, Integer>();
		keys = new ArrayList<String>();
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

	public void InsertDataElement(String key, double value) {
		data.put(key, (int) value);
		keys.add(key);
	}

	public ArrayList<String> getKeys() {
		return keys;
	}

	@Override
	public String toString() {
		return "RecurringDataEntry [year=" + year + ", month=" + month + ", data=" + data + "]";
	}

}
