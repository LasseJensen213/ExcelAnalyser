package excel;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataTransferObjects.ColumnCalculationDTO;
import dataTransferObjects.DataModificationDTO;
import dataTransferObjects.RecurringDataEntry;
import dataTransferObjects.RenameVariableDTO;
import dataTransferObjects.SettingVariableNamesDTO;
import dataTransferObjects.SumAndDeleteDTO;
import dataTransferObjects.SettingVariableNamesDTO.SettingVariableIdentifiers;
import exceptions.UnknownSettingsVariableNameException;

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
		System.out.println(readDataModificationDocument().toString());
	}



	public ArrayList<RecurringDataEntry> readRecurringDataSheet() {
		int startRow = 1;
		int yearColumn = 0;
		int monthColumn = 1;
		int maxColumn = 0;

		ArrayList<RecurringDataEntry> recurringData = new ArrayList<RecurringDataEntry>();
		ArrayList<String> headers = new ArrayList<String>();

		int currentRow = startRow;
		int currentColumn = 0;
		Sheet recurringDataSheet = workbook.getSheet(this.recurringData);

		Row row = null;
		//Read headers and the number of rows that exist. 
		row = recurringDataSheet.getRow(0);
		while(true) {
			//Check if row exists
			if(row == null) {
				break;
			}

			Cell cell = row.getCell(currentColumn);
			if(cell == null) {
				break;
			}

			headers.add(cell.getStringCellValue());

			currentColumn++;
			maxColumn++;
		}

		//Read all the data		
		while(true) {
			//Get current row 
			row = recurringDataSheet.getRow(currentRow);
			//Check for null
			if(row == null) {
				break;
			}

			Cell dataCell;
			RecurringDataEntry entry = new RecurringDataEntry();

			for(int i = 0;i < maxColumn;i++) {
				dataCell = row.getCell(i);
				//Check if dataCell is null. Should never be, unless some data is missing
				if(dataCell == null) {
					break;
				}
				if(i == yearColumn) {
					entry.setYear(String.valueOf((int) dataCell.getNumericCellValue()));
				}
				else if(i == monthColumn) {
					entry.setMonth(dataCell.getStringCellValue());
				}
				else {
					entry.InsertDataElement(headers.get(i), dataCell.getNumericCellValue());
				}

			}
			recurringData.add(entry);
			currentRow++;



		}
		return recurringData;



	}


	public DataModificationDTO readDataModificationDocument() {
		int columnCalculationStart = 0;
		int renameVariableStart = 6;
		int sumAndDeleteStart = 11;

		//Row variables
		int rowStart = 2;
		int currentRow = rowStart;





		//Collect the data
		ArrayList<ColumnCalculationDTO> columnCalculationList = new ArrayList<ColumnCalculationDTO>();
		ArrayList<RenameVariableDTO> renameVariableList = new ArrayList<RenameVariableDTO>();
		ArrayList<SumAndDeleteDTO> sumAndDeleteList = new ArrayList<SumAndDeleteDTO>();
		DataModificationDTO dataModificationDTO = new DataModificationDTO();
		
		Sheet dataModificationSheet = workbook.getSheet(dataModificationName);

		Row row;

		//Read the column calculations
		while(true) {
			row = dataModificationSheet.getRow(currentRow);
			if(row == null) {
				break;
			}

			ColumnCalculationDTO columnCalculation = new ColumnCalculationDTO();

			Cell column1Cell = row.getCell(0+columnCalculationStart);
			Cell column2Cell = row.getCell(1+columnCalculationStart);
			Cell operatorCell = row.getCell(2+columnCalculationStart);
			Cell columnNameCell =  row.getCell(3+columnCalculationStart);

			//If either of the strings are null, some data are missing and the calculation cannot be performed
			if(column1Cell == null || column2Cell == null || operatorCell == null || columnNameCell == null) {
				break;
			}


			columnCalculation.setColumn1Name(column1Cell.getStringCellValue());
			columnCalculation.setColumn2Name(column2Cell.getStringCellValue());
			columnCalculation.setOperator(operatorCell.getStringCellValue());
			columnCalculation.setColumnHeaderName(columnNameCell.getStringCellValue());
			columnCalculationList.add(columnCalculation);
			currentRow++;
		}

		//Reset the currentRow
		currentRow = rowStart;

		//Read the variables to be renamed
		while(true) {
			row = dataModificationSheet.getRow(currentRow);
			if(row == null) {
				break;
			}


			Cell variableNameCell = row.getCell(0+renameVariableStart);
			Cell replacementNameCell = row.getCell(1+renameVariableStart);

			//If either of the strings are null, some data are missing and the calculation cannot be performed
			if(variableNameCell == null || replacementNameCell == null) {
				break;
			}


			String variableName = variableNameCell.getStringCellValue();
			String replacementName = replacementNameCell.getStringCellValue();

			RenameVariableDTO renameVariable = new RenameVariableDTO(variableName,replacementName);

			renameVariableList.add(renameVariable);
			currentRow++;
		}



		//Reset the currentRow
		currentRow = rowStart;

		//Read the variables to summed and deleted
		while(true) {
			row = dataModificationSheet.getRow(currentRow);
			if(row == null) {
				break;
			}


			Cell variable1 = row.getCell(0+sumAndDeleteStart);
			Cell variable2 = row.getCell(1+sumAndDeleteStart);

			//If either of the strings are null, some data are missing and the calculation cannot be performed
			if(variable1 == null || variable2 == null) {
				break;
			}


			String variable1Name= variable1.getStringCellValue();
			String variable2Name = variable2.getStringCellValue();

			SumAndDeleteDTO sumAndDelete = new SumAndDeleteDTO(variable1Name,variable2Name);

			sumAndDeleteList.add(sumAndDelete);
			currentRow++;
		}


		dataModificationDTO.setColumnCalculationList(columnCalculationList);
		dataModificationDTO.setRenameVariableList(renameVariableList);
		dataModificationDTO.setSumAndDeleteList(sumAndDeleteList);
		return dataModificationDTO;






	}

	public SettingVariableNamesDTO readSettingsSheet() throws UnknownSettingsVariableNameException {
		//Where does the data start
		int startRow = 1;	
		int identifierColumn =  2;
		int valueColumn = 1;
		int currentRow = startRow;
		SettingVariableNamesDTO settingVariableNames = new SettingVariableNamesDTO();

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
			if(identifierCell == null) {
				break;
			}
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
