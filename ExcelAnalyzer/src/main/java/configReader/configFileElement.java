package configReader;

import exceptions.IncorrectConfigFileException;

public enum configFileElement {
	FOLDER_GENERAL_DATA,
	FOLDER_ACTIVE_USERS,
	FOLDER_SESSIONS,
	SORT_BY;
	
	
	
	
	
	
	
	public static configFileElement fromString(String from) throws IncorrectConfigFileException{
	    for (configFileElement type: configFileElement.values()) {
	        if (type.toString().startsWith(from)) {
	            return type;
	        }
	    }
	    
	    throw new IncorrectConfigFileException(from);
	
	}

}
