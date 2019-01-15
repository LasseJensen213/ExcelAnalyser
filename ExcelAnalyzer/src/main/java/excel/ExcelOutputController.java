package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.ColumnCalculationDTO;
import globalValues.GlobalValues;

public class ExcelOutputController {

	private XSSFWorkbook workbook;

	private String path = "./%s.xlsx";

	GlobalValues globalValues;

	public ExcelOutputController() throws EncryptedDocumentException, InvalidFormatException, IOException {
		// Initialize excel ark
		globalValues = GlobalValues.getInstance();
		workbook = new XSSFWorkbook();
	}

	public void processData(AnalyticsDTO data) {
		Sheet sheet = workbook.createSheet(globalValues.getNameDataSheetFile());
		int rowReached = 0;
		// Insert the headers
		Row headerRow = sheet.createRow(rowReached);

		int pushHeaders = 0;
		if (data.isMultipleFolders()) {
			headerRow.createCell(0).setCellValue("Type");
			pushHeaders = 1;
		}

		headerRow.createCell(0 + pushHeaders).setCellValue("Fil navn");
		for (int i = 0; i < data.getCategoryList().size(); i++) {
			headerRow.createCell(i + 1 + pushHeaders).setCellValue(data.getCategoryList().get(i));
		}
		int columnCalculationIndex = 0 + pushHeaders + 1;
		for (ColumnCalculationDTO columnCalculationDTO : globalValues.getDataModificationDTO()
				.getColumnCalculationList()) {

			headerRow.createCell(data.getCategoryList().size() + columnCalculationIndex)
					.setCellValue(columnCalculationDTO.getColumnHeaderName());
			columnCalculationIndex++;
		}

		// for(int i = 1+pushHeaders;i<this.calculationHeaders.length+pushHeaders+1;i++)
		// {
		// headerRow.createCell(data.getCategoryList().size()+i).setCellValue(this.calculationHeaders[i-1-pushHeaders]);
		// }
		rowReached++;

		// Iterate through the files
		Row row;
		int columnIndex = 0;
		String[] addresses = new String[data.getCategoryList().size()];
		boolean skipRow = false;

		// Create variables for frequently used values
		int dataFileListSize = data.getFileList().size();
		int fileNumberOfRows = 0;
		int dataCategoryListSize = data.getCategoryList().size();

		// Run through all of the files
		for (int i = 0; i < dataFileListSize; i++) {

			// Run through all of the rows
			fileNumberOfRows = data.getFileList().get(i).getKeys().size();
			for (int rowIndex = 0; rowIndex < fileNumberOfRows; rowIndex++) {

				// If it's in a folder system, add the folder name
				row = sheet.createRow(rowReached);
				if (data.isMultipleFolders()) {
					row.createCell(columnIndex).setCellValue(data.getFileList().get(i).getDirectoryName());
					columnIndex++;
				}

				row.createCell(columnIndex).setCellValue(data.getFileList().get(i).getSheetName());
				columnIndex++;

				for (int index = 0; index < dataCategoryListSize; index++) {

					String key = data.getFileList().get(i).getKeys().get(rowIndex);
					String cellValue = null;

					ArrayList<String> dataRow = data.getFileList().get(i).getRow(key);
					if (dataRow != null) {
						if (index >= dataRow.size()) {
							rowReached--;
							skipRow = true;
							break;
						}
						cellValue = dataRow.get(index);
					} else {
						// Delete the inserted values for the directory and file name
						row.getCell(0).setCellValue("");
						if (data.isMultipleFolders()) {
							row.getCell(1).setCellValue("");
						}
						rowReached--;
						skipRow = true;
						break;
					}
					boolean isDigit = true;
					for (int characterIndex = 0; characterIndex < cellValue.length(); characterIndex++) {
						if (cellValue != null && !Character.isDigit(cellValue.charAt(characterIndex))) {
							isDigit = false;
						}
					}
					if (isDigit) {

						row.createCell(columnIndex).setCellValue(Integer.parseInt(cellValue));

					} else {

						row.createCell(columnIndex).setCellValue(cellValue);

					}
					addresses[index] = row.getCell(columnIndex).getAddress().formatAsString();

					columnIndex++;
				}
				if (!skipRow) {
					String formula;
					for (ColumnCalculationDTO columnCalculationDTO : globalValues.getDataModificationDTO()
							.getColumnCalculationList()) {
						int index1 = data.getCategoryList().indexOf(columnCalculationDTO.getColumn1Name().trim());
						int index2 = data.getCategoryList().indexOf(columnCalculationDTO.getColumn2Name().trim());
						formula = addresses[index1] + columnCalculationDTO.getOperator() + addresses[index2];
						row.createCell(columnIndex).setCellFormula(formula);
						columnIndex++;

					}

				} else {
					skipRow = false;

				}
				columnIndex = 0;

				rowReached++;
			}
		}
		// this.endAddress = getEndAddress(sheet);

	}

	/**
	 * Closes and saves the Excel workbook. <br>
	 * call this function when you're done with the excel file.
	 * 
	 * @throws IOException
	 */
	public void closeWorkbook() throws IOException {
		path = String.format(path, globalValues.getNameOutputExcelFile());

		FileOutputStream output = new FileOutputStream(this.path);
		workbook.write(output);
		output.flush();
		output.close();
		workbook.close();
	}

}
