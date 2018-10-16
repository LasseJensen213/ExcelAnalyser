package dataTransferObjects;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDTO {
	
	private List<String> eventNameList;			//Collective list of events
	private List<String> sheetNameList;			//Collective list of sheetNames
	private List<SheetDTO> sheetList;			//Collective list of sheets - This is where the data is stored.
	private ConfigDTO configDTO;				//Config data. 
	private List<String> categoryList;			//Collective list of categories 
	

	public AnalyticsDTO() {
		this.eventNameList = new ArrayList<String>();
		this.sheetNameList = new ArrayList<String>();
		this.sheetList = new ArrayList<SheetDTO>();
		this.categoryList = new ArrayList<String>();
		

		
		
	}

	public List<String> getEventNameList() {
		return eventNameList;
	}

	public void addEventName(String event) {
		eventNameList.add(event);
	}
	
	public List<String> getSheetNameList() {
		return sheetNameList;
	}

	public void addSheetNames(ArrayList<String> names) {
		sheetNameList.addAll(names);
	}
	
	
	public boolean eventKnown(String name) {
		return eventNameList.contains(name);
	}

	public List<SheetDTO> getSheetList() {
		return sheetList;
	}

	public void addSheet(SheetDTO sheet) {
		sheetList.add(sheet);
	}

	public ConfigDTO getConfigDTO() {
		return configDTO;
	}

	public void setConfigDTO(ConfigDTO configDTO) {
		this.configDTO = configDTO;
	}

	/**
	 * @return the categoryList
	 */
	public List<String> getCategoryList() {
		return categoryList;
	}

	/**
	 * @param add category to list
	 */
	public void addCategory(String category) {
		this.categoryList.add(category);
	}

	public boolean categoryKnown(String category) {
		return this.categoryList.contains(category);
	}
	
	
	
	
	
	
	
	
	

}
