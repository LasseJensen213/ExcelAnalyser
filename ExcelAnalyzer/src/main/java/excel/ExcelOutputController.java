package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.ColumnCalculationDTO;
import dataTransferObjects.FileDTO;
import globalValues.GlobalValues;

public class ExcelOutputController {


	private XSSFWorkbook workbook;

	private String path = "./%s.xlsx";




	private final int dataRowStart = 2;
	private final int dataColumnStart = 1;

	GlobalValues globalValues;

	


	public ExcelOutputController() throws EncryptedDocumentException, InvalidFormatException, IOException {
		//Initialize excel ark		
		globalValues = GlobalValues.getInstance();
		workbook = new XSSFWorkbook();
		path = String.format(path, globalValues.getNameOutputExcelFile());
	}



	public void processData(AnalyticsDTO data) {
		Sheet sheet = workbook.createSheet(globalValues.getNameDataSheetFile());
		int rowReached = 0;
		//Insert the headers
		Row headerRow = sheet.createRow(rowReached);

		int pushHeaders = 0;
		if(data.getFileList().get(0).getDirectoryName() != null) {
			headerRow.createCell(0).setCellValue("Type");
			pushHeaders = 1;
		}


		headerRow.createCell(0+pushHeaders).setCellValue("Fil navn");
		for(int i = 0;i<data.getCategoryList().size();i++) {
			headerRow.createCell(i+1+pushHeaders).setCellValue(data.getCategoryList().get(i));
		}
		int columnCalculationIndex = 0+pushHeaders+1;
		for(ColumnCalculationDTO columnCalculationDTO : globalValues.getDataModificationDTO().getColumnCalculationList()) {
			
			headerRow.createCell(data.getCategoryList().size()+columnCalculationIndex).setCellValue(columnCalculationDTO.getColumnHeaderName());
			columnCalculationIndex++;
		}

//		for(int i = 1+pushHeaders;i<this.calculationHeaders.length+pushHeaders+1;i++) {
//			headerRow.createCell(data.getCategoryList().size()+i).setCellValue(this.calculationHeaders[i-1-pushHeaders]);
//		}
		rowReached++;

		//Iterate through the files
		Row row;
		int columnIndex = 0;
		String[] addresses = new String[data.getCategoryList().size()];
		boolean skipRow = false;

		//Create variables for frequently used values
		int dataFileListSize = data.getFileList().size();	
		int fileNumberOfRows = 0;
		int dataCategoryListSize = data.getCategoryList().size();

		
		//Run through all of the files
		for(int i = 0;i<dataFileListSize;i++) {
			
			//Run through all of the rows
			fileNumberOfRows = data.getFileList().get(i).getKeys().size();
			for(int rowIndex = 0;rowIndex<fileNumberOfRows;rowIndex++) {

				//If it's in a folder system, add the folder name
				row = sheet.createRow(rowReached);
				if(data.isMultipleFolders()) {
					row.createCell(columnIndex).setCellValue(data.getFileList().get(i).getDirectoryName());
					columnIndex++;
				}
				
				row.createCell(columnIndex).setCellValue(data.getFileList().get(i).getSheetName());
				columnIndex++;

				for(int index = 0;index<dataCategoryListSize;index++) {

					String key = data.getFileList().get(i).getKeys().get(rowIndex);
					String cellValue = null;
					
					ArrayList<String> dataRow = data.getFileList().get(i).getRow(key);
					if(dataRow != null) {
						cellValue = dataRow.get(index);
					}
					else {
						//Delete the inserted values for the directory and file name
						row.getCell(0).setCellValue("");
						row.getCell(1).setCellValue("");

						rowReached--;
						skipRow = true;
						break;
					}
					
					if(cellValue != null && Character.isDigit(cellValue.charAt(0))) {

						row.createCell(columnIndex).setCellValue(Integer.parseInt(cellValue));

					}
					else {

						row.createCell(columnIndex).setCellValue(cellValue);


					}
					addresses[index] = row.getCell(columnIndex).getAddress().formatAsString();

					columnIndex++;
				}
				if(!skipRow) {
					String formula;
					for(ColumnCalculationDTO columnCalculationDTO : globalValues.getDataModificationDTO().getColumnCalculationList()) {
						int index1 = data.getCategoryList().indexOf(columnCalculationDTO.getColumn1Name());
						int index2 = data.getCategoryList().indexOf(columnCalculationDTO.getColumn2Name());
						formula = addresses[index1] + columnCalculationDTO.getOperator() + addresses[index2];
						row.createCell(columnIndex).setCellFormula(formula);
						columnIndex++;

					}
					
				}
				else {
					skipRow = false;

				}
				columnIndex = 0;


				rowReached++;
			}
		}
		//this.endAddress = getEndAddress(sheet);

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
		for(int i = 0;i<data.getFileList().size();i++) {
			int index = i*(numOfCategories)+1;
			headerrow.createCell(index).setCellValue(data.getFileList().get(i).getSheetName());
		}
		//Insert the categories
		Row subRow = sheet.createRow(1);
		for(int i = 0;i<data.getFileList().size();i++) {
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
		FileDTO currentSheet = null;
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

			for(int j = 0;j<data.getFileList().size();j++) {

				//Calculate the current column
				columnIndex = j*(numOfCategories)+this.dataColumnStart;
				//Get the current sheet
				currentSheet = data.getFileList().get(j);
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
							double value = Integer.parseInt(stringValue);

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
		FileDTO currentSheet = null;
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

			for(int j = 0;j<data.getFileList().size();j++) {

				//Calculate the current column
				columnIndex = j*(shiftBy)+this.dataColumnStart;
				//Get the current sheet
				currentSheet = data.getFileList().get(j);
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
						double value = Integer.parseInt(row.get(categoryIndex));
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
