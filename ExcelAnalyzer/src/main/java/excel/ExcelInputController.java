package excel;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import exceptions.UnknownSettingsVariableNameException;
import globalValues.SettingVariableNames;
import globalValues.SettingVariableNames.SettingVariableIdentifiers;

public class ExcelInputController {
	
	
	//Path
	private final String controlDocumentPathName = "./Kontrol Dokument.xlsx";
	
	
	//Sheet names
	private final String settingsName = "Indstillinger";
	private final String recurringData = "Gengående oplysninger";
	private final String dataModificationName = "Data modifikation";
	
	
	
	
	
	//Variables
	private XSSFWorkbook workbook;

	
	
	
	
	public ExcelInputController() throws EncryptedDocumentException, InvalidFormatException, IOException {
		//Initialize excel ark		
		workbook = new XSSFWorkbook(controlDocumentPathName);
	}
	
	public void ReadControlDocoument() throws UnknownSettingsVariableNameException {
		System.out.println(readSettingsSheet().toString());
	}
	

	/**
	 * 
	 */
	public void readRecurringDataSheet() {
		int startRow = 1;
		int yearColumn = 0;
		int monthColumn = 1;

		ArrayList<String> asdf = new ArrayList<String>();
		asdf.add
		
		
	}
	
	
	
	private SettingVariableNames readSettingsSheet() throws UnknownSettingsVariableNameException {
		//Where does the data start
		int startRow = 1;	
		int identifierColumn =  2;
		int valueColumn = 1;
		int currentRow = startRow;
		SettingVariableNames settingVariableNames = new SettingVariableNames();
		
		//Sheet variable
		Sheet settingsSheet = workbook.getSheet(settingsName);
		Row row = null;
		//Go through a while loop to iterate through the data
		while(true) {
			
			//Fetch current row
			row = settingsSheet.getRow(currentRow);
			
			//Check if null - Exit while loop if true
			if(row == null) {
				break;
			}
			
			Cell identifierCell = row.getCell(identifierColumn);
			String cellValue = row.getCell(valueColumn).getStringCellValue(); 
			SettingVariableIdentifiers enumVariable = SettingVariableIdentifiers.fromString(identifierCell.getStringCellValue());
			switch(enumVariable) {
			case nameAndroidFolder:
				settingVariableNames.setNameAndroidFolder(cellValue);
				break;
			case nameDataFolder:
				settingVariableNames.setNameDataFolder(cellValue);
				break;
			case nameDataSheetFile:
				settingVariableNames.setNameDataSheetFile(cellValue);
				break;
			case nameIOSFolder:
				settingVariableNames.setNameIOSFolder(cellValue);
				break;
			case nameOutputExcelFile:
				settingVariableNames.setNameOutputExcelFile(cellValue);
				break;
			case typeNameForSumAndDelete:
				settingVariableNames.setTypeNameForSumAndDelete(cellValue);
				break;
			default:
				break;
				
			}
			
			
			
			
			
			currentRow++;
		}
		return settingVariableNames;
		
	}
	
	
	
	
	

}
