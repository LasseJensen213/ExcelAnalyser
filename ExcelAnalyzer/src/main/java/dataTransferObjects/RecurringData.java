package dataTransferObjects;

import java.util.ArrayList;

import globalValues.GlobalValues;

public class RecurringData {

	private String sheetName = null;
	private String folder = null;

	private ArrayList<RecurringDataEntry> recurringData;

	public RecurringData(String sheetName) {
		recurringData = new ArrayList<RecurringDataEntry>();
		this.sheetName = sheetName;
		findFolderName();

	}

	public ArrayList<RecurringDataEntry> getRecurringData() {
		return recurringData;
	}

	public void setRecurringData(ArrayList<RecurringDataEntry> recurringData) {
		this.recurringData = recurringData;
	}

	public void addDataEntry(RecurringDataEntry entry) {
		recurringData.add(entry);
	}

	public String getFolder() {
		return folder;
	}

	public String getSheetName() {
		return sheetName;
	}

	/**
	 * Set the folder name corresponding to the folder. <br>
	 * If it's the webmap, set it to a hardcoded value.
	 */
	private void findFolderName() {
		GlobalValues values = GlobalValues.getInstance();
		if (this.sheetName.contains(values.getNameAndroidFolder())) {
			this.folder = values.getNameAndroidFolder();
		} else if (this.sheetName.contains(values.getNameIOSFolder())) {
			this.folder = values.getNameIOSFolder();
		} else {
			this.folder = "Trafikkort";
		}

	}

}
