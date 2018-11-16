package globalValues;

import exceptions.UnknownSettingsVariableNameException;

public class SettingVariableNames {
	
	
	public enum SettingVariableIdentifiers {
		typeNameForSumAndDelete, 
		nameDataFolder,
		nameAndroidFolder,
		nameIOSFolder,
		nameOutputExcelFile,
		nameDataSheetFile;
		
		
		public static SettingVariableIdentifiers fromString(String from) throws UnknownSettingsVariableNameException{
		    for (SettingVariableIdentifiers type: SettingVariableIdentifiers.values()) {
		        if (type.toString().startsWith(from)) {
		            return type;
		        }
		    }
		    
		    throw new UnknownSettingsVariableNameException(from);
		
		}
	};
	
	
	private String typeNameForSumAndDelete = "Total";
	private String nameDataFolder = "files";
	private String nameAndroidFolder = "Android";
	private String nameIOSFolder = "IOS";
	private String nameOutputExcelFile = "Google Analytics";
	private String nameDataSheetFile = "Data";
	
	
	public SettingVariableNames() {
		super();
	}


	public String getTypeNameForSumAndDelete() {
		return typeNameForSumAndDelete;
	}


	public void setTypeNameForSumAndDelete(String typeNameForSumAndDelete) {
		this.typeNameForSumAndDelete = typeNameForSumAndDelete;
	}


	public String getNameDataFolder() {
		return nameDataFolder;
	}


	public void setNameDataFolder(String nameDataFolder) {
		this.nameDataFolder = nameDataFolder;
	}


	public String getNameAndroidFolder() {
		return nameAndroidFolder;
	}


	public void setNameAndroidFolder(String nameAndroidFolder) {
		this.nameAndroidFolder = nameAndroidFolder;
	}


	public String getNameIOSFolder() {
		return nameIOSFolder;
	}


	public void setNameIOSFolder(String nameIOSFolder) {
		this.nameIOSFolder = nameIOSFolder;
	}


	public String getNameOutputExcelFile() {
		return nameOutputExcelFile;
	}


	public void setNameOutputExcelFile(String nameOutputExcelFile) {
		this.nameOutputExcelFile = nameOutputExcelFile;
	}


	public String getNameDataSheetFile() {
		return nameDataSheetFile;
	}


	public void setNameDataSheetFile(String nameDataSheetFile) {
		this.nameDataSheetFile = nameDataSheetFile;
	}


	@Override
	public String toString() {
		return "SettingVariableNames [typeNameForSumAndDelete=" + typeNameForSumAndDelete + ", nameDataFolder="
				+ nameDataFolder + ", nameAndroidFolder=" + nameAndroidFolder + ", nameIOSFolder=" + nameIOSFolder
				+ ", nameOutputExcelFile=" + nameOutputExcelFile + ", nameDataSheetFile=" + nameDataSheetFile + "]";
	}
	
	
	
	
	
	

}
