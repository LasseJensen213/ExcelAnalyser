package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.Controller;
import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.SheetDTO;
import exceptions.NoMatchingIdentifiersException;

public class ExcelController {


	private XSSFWorkbook workbook;
	private final String controlSheetName = "totalData";
	private final String menuSheetName = "AppMenu";
	private AnalyticsDTO data;

	private final String path = "./Google Analytics.xlsx";


	//Header values
	private final String eventHeader = "HÃ¦ndelsesnavn";


	private final int dataRowStart = 2;
	private final int dataColumnStart = 1;

	private String startAddress = "A1"; 
	private String endAddress;

	private int highestRowIndex = 0;
	private int highestColumnIndex = 0;
	
	private String[] calculationHeaders = {"Antal klik på etiket/antal sessioner","Antal klik på etiket/antal brugere","Antal unikke klik på etiket/antal brugere"};
	


	public ExcelController() throws EncryptedDocumentException, InvalidFormatException, IOException {
		//Initialize excel ark		
		workbook = new XSSFWorkbook();
		data = new AnalyticsDTO();
	}



	public void processData(AnalyticsDTO data) {
		Sheet sheet = workbook.createSheet("Title");
		int rowReached = 0;
		//Insert the headers
		Row headerRow = sheet.createRow(rowReached);
		headerRow.createCell(0).setCellValue("Fil navn");
		for(int i = 0;i<data.getCategoryList().size();i++) {
			headerRow.createCell(i+1).setCellValue(data.getCategoryList().get(i));
		}
		
		for(int i = 1;i<=3;i++) {
			headerRow.createCell(data.getCategoryList().size()+i).setCellValue(this.calculationHeaders[i-1]);
		}
		rowReached++;

		//Iterate through the files
		Row row;
		int columnIndex = 0;
		String[] addresses = new String[7];
		boolean skipRow = false;
		for(int i = 0;i<data.getSheetList().size();i++) {
			for(int rowIndex = 0;rowIndex<data.getSheetList().get(i).getNumberOfRows();rowIndex++) {
				row = sheet.createRow(rowReached);
				row.createCell(columnIndex).setCellValue(data.getSheetList().get(i).getSheetName());
				columnIndex++;

				for(int index = 0;index<data.getCategoryList().size();index++) {
					String key = data.getColumnNameList().get(rowIndex);
					String cellValue = null;
					try {
						cellValue = data.getSheetList().get(i).getRow(key).get(index);
					}
					catch(NullPointerException e) {
						rowReached--;
						skipRow = true;
						break;
						//e.printStackTrace();

					}
					if(index > 2) {
						if(cellValue == null) {

						}
						else {
							row.createCell(columnIndex).setCellValue(Integer.parseInt(cellValue));
						}
					}
					else {
						if(cellValue == null) {

						}
						else {
							row.createCell(columnIndex).setCellValue(cellValue);
						}

					}
					addresses[index] = row.getCell(columnIndex).getAddress().formatAsString();

					columnIndex++;
				}
				if(!skipRow) {

					for(AnalyticsDTO.calculationsTypes type: AnalyticsDTO.calculationsTypes.values()) {
						row.createCell(columnIndex).setCellFormula(calculateFormula(type, data, addresses));
						columnIndex++;
					}
				}
				else {
					skipRow = false;

				}
				setHighestColumnIndex(columnIndex);
				columnIndex = 0;


				rowReached++;
			}
		}
		this.highestRowIndex = rowReached;
		//this.endAddress = getEndAddress(sheet);
		System.out.println("EndAddress: " + this.endAddress);
		
	}

	private String getEndAddress(Sheet sheet) {
		Row endRow = sheet.getRow(this.highestRowIndex);
		if(endRow == null) {
			endRow = sheet.getRow(highestRowIndex-1);
		}
		Cell endCell = endRow.getCell(this.highestColumnIndex);
		if(endCell == null) {
			endCell = endRow.getCell(this.highestColumnIndex-1);
		}
		return endCell.getAddress().formatAsString();
	}
	private void setHighestColumnIndex(int index) {
		if(this.highestColumnIndex < index) {
			this.highestColumnIndex = index;
		}
	}
	
	

	private String calculateFormula(AnalyticsDTO.calculationsTypes type, AnalyticsDTO data, String[] addresses) {	
		String formula = null;
		switch (type) {
		case CLICKS_PER_LABEL_PER_SESSION:
			formula = addresses[data.getAllActivityIndex()] + "/" + addresses[data.getSessionsIndex()];
			break;
		case CLICKS_PER_LABEL_PER_USERS:
			formula = addresses[data.getAllActivityIndex()] + "/" + addresses[data.getUsersIndex()];
			break;
		case UNIQUE_CLICKS_PER_LABEL_PER_USERS:
			formula = addresses[data.getUniqueEventsIndex()] + "/" + addresses[data.getUsersIndex()];
			break;
		default:
			break;

		}
		return formula;


	}


	/**
	 * Splits the data between sheets based on the given type. 
	 * @param type - String
	 * @param data - AnalyticsDTO
	 */
	public void sortData(String type, AnalyticsDTO data){
		//Create the sheet containing all the data.
		Sheet sheet = workbook.createSheet(type);

		//Create the headerrow
		Row headerrow = sheet.createRow(0);
		headerrow.createCell(0).setCellValue(data.getCategoryList().get(0));
		int numOfCategories = data.getCategoryList().size()-1;

		//Insert the names of the files. Split apart by the number of categories. 
		for(int i = 0;i<data.getSheetList().size();i++) {
			int index = i*(numOfCategories)+1;
			headerrow.createCell(index).setCellValue(data.getSheetList().get(i).getSheetName());
		}
		//Insert the categories
		Row subRow = sheet.createRow(1);
		for(int i = 0;i<data.getSheetList().size();i++) {
			int index = i*(numOfCategories)+1;
			for(int j = 1;j<=numOfCategories;j++) {
				subRow.createCell(index+j-1).setCellValue(data.getCategoryList().get(j));
			}

		}


		//Headers have been created. Insert the data.
		String eventName = null;
		Row dataRow = null;
		int rowIndex = 0;
		int columnIndex = 0;

		//Sheet values
		SheetDTO currentSheet = null;
		ArrayList<String> row = null;


		int index = 0;
		for(int i = 0;i<data.getColumnNameList().size();i++) {

			//Get the event name for the row. 
			eventName = data.getColumnNameList().get(i);


			//If event is something we don't want, we skip the data element. 
			if(!eventName.toLowerCase().startsWith(type.toLowerCase())) {
				if(eventName.startsWith(type.toUpperCase()))
					//System.out.println("Type: " + type + " EventName: " + eventName);
					//Check to see if we want all the data. If not. We skip the data element
					continue;
				if(!type.equals(SortType.TOTAL.toString())) {
					continue;
				}
			}

			//Calculate the correct row
			rowIndex = index+this.dataRowStart;

			//Create the row
			dataRow = sheet.createRow(rowIndex);

			dataRow.createCell(0).setCellValue(eventName);

			for(int j = 0;j<data.getSheetList().size();j++) {

				//Calculate the current column
				columnIndex = j*(numOfCategories)+this.dataColumnStart;
				//Get the current sheet
				currentSheet = data.getSheetList().get(j);
				if(currentSheet.contains(eventName)) { 
					row = currentSheet.getRow(eventName);
					for(int n = 0;n<row.size()-1;n++) {

						//System.out.println("Value: " + row.get(n));
						try {
							//System.out.println("integer");
							String stringValue = row.get(n+1);
							if(stringValue.contains(".")) {
								stringValue = stringValue.replace(".", "");
							}
							double value = (double) Integer.parseInt(stringValue);

							dataRow.createCell(columnIndex+n, CellType.NUMERIC).setCellValue(value);
						}
						catch(NumberFormatException e) {
							//System.out.println("string");

							String value = row.get(n+1);
							dataRow.createCell(columnIndex+n).setCellValue(value);

						}
					}

				}				
			}
			index++;
		}

	}


	/**
	 * This function sorts the data by category and type. 
	 * @param type
	 * @param category
	 * @param data
	 */
	public void sortDataByCategory(String type, String category, AnalyticsDTO data) {


		int categoryIndex = data.getCategoryList().indexOf(category);
		int shiftBy = 1;
		//Create the sheet containing all the data.
		String sheetName = type + "-" + category;
		Sheet sheet = workbook.createSheet(sheetName);

		//Create the headerrow
		Row headerrow = sheet.createRow(0);
		headerrow.createCell(0).setCellValue(data.getCategoryList().get(0));

		//Insert the names of the files. Split apart by the number of categories. 
		for(int i = 0;i<data.getSheetNameList().size();i++) {
			int index = i*(shiftBy)+1;
			headerrow.createCell(index).setCellValue(data.getSheetNameList().get(i));
		}
		//Insert the categories
		Row subRow = sheet.createRow(1);
		//System.out.println(data.getSheetNameList().size());
		for(int i = 0;i<data.getSheetNameList().size();i++) {
			int index = i*(shiftBy)+1;
			for(int j = 1;j<shiftBy+1;j++) {
				subRow.createCell(index+j-1).setCellValue(data.getCategoryList().get(categoryIndex));
			}

		}


		//Headers have been created. Insert the data.
		String eventName = null;
		Row dataRow = null;
		int rowIndex = 0;
		int columnIndex = 0;

		//Sheet values
		SheetDTO currentSheet = null;
		ArrayList<String> row = null;


		int index = 0;
		for(int i = 0;i<data.getColumnNameList().size();i++) {

			//Get the event name for the row. 
			eventName = data.getColumnNameList().get(i);


			//If event is something we don't want, we skip the data element. 
			if(!eventName.toLowerCase().startsWith(type.toLowerCase())) {
				if(eventName.startsWith(type.toUpperCase()))
					//System.out.println("Type: " + type + " EventName: " + eventName);
					//Check to see if we want all the data. If not. We skip the data element

					if(!type.equals(SortType.TOTAL.toString())) {
						continue;
					}
			}

			//Calculate the correct row
			rowIndex = index+this.dataRowStart;

			//Create the row
			dataRow = sheet.createRow(rowIndex);

			dataRow.createCell(0).setCellValue(eventName);

			for(int j = 0;j<data.getSheetList().size();j++) {

				//Calculate the current column
				columnIndex = j*(shiftBy)+this.dataColumnStart;
				//Get the current sheet
				currentSheet = data.getSheetList().get(j);
				if(currentSheet.contains(eventName)) { 
					row = currentSheet.getRow(eventName);

					try {
						//System.out.println("Value: " + row.get(categoryIndex));
					}
					catch(IndexOutOfBoundsException e) {

						e.printStackTrace();
						continue;
					}
					try {
						double value = (double) Integer.parseInt(row.get(categoryIndex));
						dataRow.createCell(columnIndex, CellType.NUMERIC).setCellValue(value);
						//System.out.println("integer");

					}
					catch(NumberFormatException e) {
						//System.out.println("string");

						String value = row.get(categoryIndex);
						dataRow.createCell(columnIndex).setCellValue(value);

					}

				}				
			}
			index++;
		}




	}





	/**
	 * Sorts the data by percentage
	 * @param type
	 * @param data
	 * @throws NoMatchingIdentifiersException 
	 */
	public void sortDataByPercentage(String type, Map<String,AnalyticsDTO> data) throws NoMatchingIdentifiersException {
		//Create the sheet containing all the data.
		String namePrefix = "Percentage - ";
		Sheet sheet = workbook.createSheet(namePrefix + type);

		//Get the data from the map
		AnalyticsDTO generalData = data.get(Controller.generalDataKey);
		AnalyticsDTO sessionData = data.get(Controller.sessionsKey);		
		AnalyticsDTO userData = data.get(Controller.activeUsersKey);


		//Create the headerrow
		Row headerrow = sheet.createRow(0);
		headerrow.createCell(0).setCellValue(generalData.getCategoryList().get(0));

		//How many categories are we working with
		int sortBySessions = generalData.getConfigDTO().isSessionDataPresent() ? 1:0;
		int sortByUsers = generalData.getConfigDTO().isUserDataPresent() ? 1:0;

		int numOfCategories = (generalData.getCategoryList().size()-1)/2;
		int offset = (1+sortBySessions + sortByUsers);

		//Insert the names of the files. Split apart by the number of categories.
		for(int i = 0;i<generalData.getSheetList().size();i++) {
			int categoryIndex = 1 + i*numOfCategories*2;
			headerrow.createCell(categoryIndex).setCellValue(generalData.getSheetList().get(i).getSheetName());

		}


		//Insert the categories
		Row subRow = sheet.createRow(1);
		for(int i = 0;i<generalData.getSheetList().size();i++) {
			//Calculate the correct column

			int index = i*(numOfCategories*2)+1;


			//We only want all activity and unique event. stuff here. 
			int categoryIndex = 1;
			for(int j = 1;j<=numOfCategories*2;j+=2) {
				subRow.createCell(index+j-1).setCellValue(generalData.getCategoryList().get(categoryIndex));
				subRow.createCell(index+j-1+1).setCellValue("Procent");
				categoryIndex++;
			}

		}


		//Headers have been created. Insert the data.
		String eventName = null;
		Row dataRow = null;
		int rowIndex = 0;
		int columnIndex = 0;

		//Sheet values
		SheetDTO currentSheet = null;
		ArrayList<String> row = null;


		int index = 0;
		for(int i = 0;i<generalData.getColumnNameList().size();i++) {

			//Get the event name for the row. 
			eventName = generalData.getColumnNameList().get(i);

			//General sorting.
			//If event is something we don't want, we skip the data element. 
			if(!eventName.toLowerCase().startsWith(type.toLowerCase())) {
				if(eventName.startsWith(type.toUpperCase()))
					//System.out.println("Type: " + type + " EventName: " + eventName);
					//Check to see if we want all the data. If not. We skip the data element
					continue;
				if(!type.equals(SortType.TOTAL.toString())) {
					continue;
				}
			}

			//Calculate the correct row
			rowIndex = index+this.dataRowStart;

			//Create the row
			dataRow = sheet.createRow(rowIndex);

			dataRow.createCell(0).setCellValue(eventName);

			//Iterate through the files to sort
			for(int j = 0;j<generalData.getSheetList().size();j++) {

				//Calculate the current column
				columnIndex = j*(numOfCategories)*2+this.dataColumnStart;

				//Get the current sheet
				currentSheet = generalData.getSheetList().get(j);
				String fileName = currentSheet.getSheetName();
				int identifierEnd = fileName.indexOf("#");
				String fileIdentifier = fileName.substring(0,identifierEnd);

				if(currentSheet.contains(eventName)) { 
					row = currentSheet.getRow(eventName);
					//Remove any excess information we don't want. 
					if(row.size()>3) {
						row.remove(4);																					//Hardcoded
						row.remove(3);
					}
					//We only want half of the data
					rowIndex = 0;
					int insertDataIndex = 0;
					for(int n = 0;n<row.size()-1;n++) {
						insertDataIndex = n*2;
						//System.out.println(row.size()-1);
						//System.out.println("Value: " + row.get(n));
						try {
							String stringValue = row.get(rowIndex+1);
							String sessionValue = null;
							String userValue = null;
							System.out.println("size: " + sessionData.getSheetList().size());
							for(int sheetIndex = 0;sheetIndex < sessionData.getSheetList().size();sheetIndex++) {
								System.out.println("SheetIndex: " + sheetIndex);
								if(sessionData.getSheetList().get(sheetIndex).getSheetName().startsWith(fileIdentifier)) {
									sessionValue = sessionData.getSheetList().get(sheetIndex).getRow("0000").get(1);
								}
								if(userData.getSheetList().get(sheetIndex).getSheetName().startsWith(fileIdentifier)) {
									userValue = userData.getSheetList().get(sheetIndex).getRow("0000").get(1);
								}
							}
							if(sessionValue == null || userValue == null) {
								throw new NoMatchingIdentifiersException("No matching identifiers found for " + fileIdentifier);
							}

							if(stringValue.contains(".")) {
								stringValue = stringValue.replace(".", "");
							}
							if(sessionValue.contains(".")) {
								sessionValue = sessionValue.replace(".", "");
							}
							if(userValue.contains(".")) {
								userValue = userValue.replace(".", "");
							}
							double value = (double) Integer.parseInt(stringValue);
							double sessionTotal = (double) Integer.parseInt(sessionValue);
							double userTotal = (double) Integer.parseInt(userValue);

							dataRow.createCell((columnIndex)+insertDataIndex, CellType.NUMERIC).setCellValue(value);
							double percentage = 0;
							if(generalData.getCategoryList().get(1).equals("Al aktivitet")) {
								percentage = value/sessionTotal;
								System.out.println(percentage);
							}

							if(generalData.getCategoryList().get(2).equals("Unikke hÃ¦ndelser")) {
								percentage = value/sessionTotal;
								System.out.println("percentage");
							}
							dataRow.createCell((columnIndex)+insertDataIndex+1,CellType.NUMERIC).setCellValue(percentage);
							//System.out.println("integer");

						}
						catch(NumberFormatException e) {
							//System.out.println("string");

							String value = row.get(n+1);
							dataRow.createCell(columnIndex+n).setCellValue(value);

						}
						rowIndex++;
					}

				}				
			}
			index++;
		}


	}



	//
	//	public void sortDataByPercentage(String type, AnalyticsDTO data){
	//		//Create the sheet containing all the data.
	//		Sheet sheet = workbook.createSheet(type);
	//
	//		//Create the headerrow
	//		Row headerrow = sheet.createRow(0);
	//		headerrow.createCell(0).setCellValue(this.eventHeader);
	//
	//		//Insert the names of the files. Split apart by 2 cells. 
	//		for(int i = 0;i<data.getSheetNameList().size();i++) {
	//			int index = i + 1;
	//			headerrow.createCell(index*2-1).setCellValue(data.getSheetNameList().get(i));
	//		}
	//
	//		Row subRow = sheet.createRow(1);
	//		for(int i = 0;i<data.getSheetNameList().size()*2;i++) {
	//			int index = i+1;
	//			if(index%2 == 0) {
	//				subRow.createCell(index).setCellValue("Brugere");
	//			}
	//			else {
	//				subRow.createCell(index).setCellValue("Antal");
	//			}
	//		}
	//
	//
	//		//Headers have been created. Insert the data.
	//		String eventName = null;
	//		Row dataRow = null;
	//		int rowIndex = 0;
	//		int columnIndex = 0;
	//
	//		//Sheet values
	//		SheetDTO currentSheet = null;
	//		RowElementDTO row = null;
	//		int quantity;
	//		int users;
	//
	//		int index = 0;
	//		for(int i = 0;i<data.getEventNameList().size();i++) {
	//
	//			//Get the event name for the row. 
	//			eventName = data.getEventNameList().get(i);
	//
	//			if(!eventName.contains(type)) {
	//				if(!type.equals("NONE")) {
	//					continue;
	//				}
	//			}
	//
	//			//Calculate the correct row
	//			rowIndex = index+this.dataRowStart;
	//
	//			//Create the row
	//			dataRow = sheet.createRow(rowIndex);
	//
	//			dataRow.createCell(0).setCellValue(eventName);
	//
	//			for(int j = 0;j<data.getSheetList().size();j++) {
	//
	//				//Calculate the current column
	//				columnIndex = j*2+this.dataColumnStart;
	//				System.out.println("asdf");
	//				System.out.println("Row: " + rowIndex + " ColumnIndex: " + columnIndex);
	//				//Get the current sheet
	//				currentSheet = data.getSheetList().get(j);
	//				if(currentSheet.contains(eventName)) {
	//					row = currentSheet.getRow(eventName);
	//					quantity = row.getQuantity();
	//					users = row.getUsers();
	//
	//					dataRow.createCell(columnIndex).setCellValue(quantity);
	//					dataRow.createCell(columnIndex+1).setCellValue(users);
	//
	//				}				
	//			}
	//			index++;
	//		}
	//
	//	}
	//
	//
	//





	/**
	 * Closes and saves the Excel workbook. <br> call this function when you're done with the excel file. 
	 * @throws IOException
	 */
	public void closeWorkbook() throws IOException{
		FileOutputStream output = new FileOutputStream(this.path);
		workbook.write(output);
		output.flush();
		output.close();
		workbook.close();
	}


}
