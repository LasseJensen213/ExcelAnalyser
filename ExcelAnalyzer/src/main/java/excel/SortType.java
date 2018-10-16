package excel;

import configReader.configFileElement;
import exceptions.IncorrectConfigFileException;

public enum SortType {
	TOTAL,	 				//Get the total data
	EVENT,					//Get data about events
	LIST,					//Get data about the list
	MAP,					//Get data about the map
	NAVIGATIONMENU,			//Get data about the navigation menu
	SCREEN,					//Get data about the screen
	WEBCAM,					//Get data about the webcam
	GEOFENCE,				//Get data about the geofence
	SUBSCRIPTIONS,			//Get data about subscriptions
	PROJECT;				//Get data about the projects

	
	
	
	public static SortType fromString(String from){
	    for (SortType type: SortType.values	()) {
	        if (type.toString().startsWith(from)) {
	            return type;
	        }
	    	
	    }
		return null;
	}
	
	    		
}
