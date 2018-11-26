package globalValues;

public class GlobalValues {

	
	
	private static GlobalValues single_instance = null; 
	
	private String typeNameForSumOfVariables = "Total";
	private String nameDataFolder = "//files//";
	private String nameAndroidFolder = "Android";
	private String nameIOSFolder = "IOS";
	private String nameOutputExcelFile = "Google Analytics";
	private String nameDataSheetFile = "Data";
	
	
	
	public static GlobalValues getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new GlobalValues(); 
  
        return single_instance; 
    } 
	
	// private constructor restricted to this class itself 
    private GlobalValues() 
    { 
        
    } 
	
	
	
	
	
	
	
	
	
	public String getTypeNameForSumOfVariables() {
		return typeNameForSumOfVariables;
	}
	public void setTypeNameForSumOfVariables(String typeNameForSumOfVariables) {
		this.typeNameForSumOfVariables = typeNameForSumOfVariables;
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
	
	
	
	

}
