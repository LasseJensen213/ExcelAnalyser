//package analyzer;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import org.apache.poi.EncryptedDocumentException;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import dataTransferObjects.AnalyticsDTO;
//import dataTransferObjects.SheetDTO;
//
//public class Analyzer {
//
//	private Workbook workbook;
//	private final String controlSheetName = "status";
//	private AnalyticsDTO dto;
//
//
//
//
//
//
//
//
//
//	public Analyzer() throws EncryptedDocumentException, InvalidFormatException, IOException {
//		//Initialize excel ark		
//		FileInputStream input = new FileInputStream(new File(Values.FILE_PATH));
//		workbook = new XSSFWorkbook(input);
//		dto = new AnalyticsDTO();
//	}
//	public void organizeData() throws EncryptedDocumentException, InvalidFormatException, IOException {
//		//Collect sheet and event names. 
//		for(Sheet sheet : workbook) {
//			dto.addSheetName(sheet.getSheetName());
//			Row row;
//			String eventName = "";
//			int eventUserValue;
//			int eventQuantityValue;
//			SheetDTO sheetDTO = new SheetDTO(sheet.getSheetName());
//			for(int i = 1;i<sheet.getLastRowNum();i++) {
//				row = sheet.getRow(i);
//				eventName = row.getCell(0).getStringCellValue();
//				eventUserValue = (int) row.getCell(1).getNumericCellValue();
//				eventQuantityValue = (int) row.getCell(2).getNumericCellValue();
//				if(!dto.eventKnown(eventName)) {
//					if(!eventName.equals("")) {
//						dto.addEventName(eventName);
//						dto.getSheetList(t
//					}
//				}
//			}
//		}
//		
//		
//		//Create datasheet for users
//		Sheet data = workbook.getSheet(Values.DATA_SHEET_NAME);
//		if(data==null) {
//			data = workbook.createSheet(Values.DATA_SHEET_NAME);
//		}
//		
//		//Create the header row
//		Row headerRow = data.createRow(0);
//		Cell activeCell = headerRow.createCell(0);
//		activeCell.setCellValue("Hændelsesnavn");
//		
//		for(int i = 1;i<dto.getSheetNameList().size();i++) {
//			activeCell = headerRow.createCell(i);
//			activeCell.setCellValue(dto.getSheetNameList().get(i));
//		}
//		
//		Row row;
//		Cell cell;
//		for(int i = 1;i<dto.getEventNameList().size()+1;i++) {
//			row = data.createRow(i);
//			cell = row.createCell(0);
//			row.getCell(0).
//			cell.setCellValue("");
//			for(int j = 1;j<12;i++) {
//				cell = row.createCell(j);
//				
//			}
//			
//		}
//		
//		
//		int i = 1;
//		for(String s : dto.getEventNameList()){
//			Row event = data.createRow(i);
//			Cell eventCell = event.createCell(0);
//			eventCell.setCellValue(s);
//			i++;	
//		}
//		FileOutputStream output = new FileOutputStream(Values.FILE_PATH);
//		workbook.write(output);
//		output.flush();
//		output.close();
//		workbook.close();
//
//
//
//	}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
