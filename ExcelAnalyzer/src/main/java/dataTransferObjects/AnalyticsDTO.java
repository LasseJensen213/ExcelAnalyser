package dataTransferObjects;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDTO {
	
	private List<String> eventNameList;			//Collective list of events
	private List<String> sheetNameList;			//Collective list of sheetNames
	private List<SheetDTO> sheetList;			//Collective list of sheets - This is where the data is stored.
	private ConfigDTO configDTO;				//Config data. 
	private List<String> categoryList;			//Collective list of categories 
	
	//Data indexes
	private final int eventCategoryIndex = 0;
	private final int eventActionIndex = 1;
	private final int eventLabelIndex = 2;
	private final int AllActivityIndex = 3;
	private final int SessionsIndex = 4;
	private final int UniqueEventsIndex = 5;
	private final int UsersIndex = 6;

	public enum calculationsTypes {CLICKS_PER_LABEL_PER_SESSION, CLICKS_PER_LABEL_PER_USERS,UNIQUE_CLICKS_PER_LABEL_PER_USERS};
	
	
	public AnalyticsDTO() {
		this.eventNameList = new ArrayList<String>();
		this.sheetNameList = new ArrayList<String>();
		this.sheetList = new ArrayList<SheetDTO>();
		this.categoryList = new ArrayList<String>();
		

		
		
	}

	public List<String> getColumnNameList() {
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

	public int getEventCategoryIndex() {
		return eventCategoryIndex;
	}

	public int getEventActionIndex() {
		return eventActionIndex;
	}

	public int getEventLabelIndex() {
		return eventLabelIndex;
	}

	public int getAllActivityIndex() {
		return AllActivityIndex;
	}

	public int getSessionsIndex() {
		return SessionsIndex;
	}

	public int getUniqueEventsIndex() {
		return UniqueEventsIndex;
	}

	public int getUsersIndex() {
		return UsersIndex;
	}
	
	
	
	
	
	
	
	
	

}
