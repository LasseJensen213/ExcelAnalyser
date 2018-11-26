package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import csv.CSVReader;
import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.DataModificationDTO;
import dataTransferObjects.FileDTO;
import dataTransferObjects.FileInformationDTO;
import dataTransferObjects.RecurringDataEntry;
import dataTransferObjects.SettingVariableNamesDTO;
import excel.ExcelInputController;
import excel.ExcelOutputController;
import exceptions.IncorrectDirectoryException;
import exceptions.UnknownSettingsVariableNameException;
import filePathReader.FilePathReader;
import globalValues.GlobalValues;

public class Controller {



	//Folder values
	private String folderPrefix = "./%s/";


	public static final String activeUsersKey = "activeUsersData";
	public static final String generalDataKey = "generalData";
	public static final String sessionsKey = "sessionData";

	//CSVReader
	private CSVReader csvReader;

	//ExcelController
	private ExcelOutputController excelController;
	
	private final String fileType = ".csv";
	
	GlobalValues values;



	public Controller() throws EncryptedDocumentException, InvalidFormatException, IOException {
		csvReader = new CSVReader();
		excelController = new ExcelOutputController();
		values = GlobalValues.getInstance();

	}



	/**
	 * Main function. 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public void analyze() throws EncryptedDocumentException, InvalidFormatException, IOException {
		//Read the control document
		GlobalValues values = GlobalValues.getInstance();
		ExcelInputController inputController = new ExcelInputController();
		DataModificationDTO dataModificationDTO = inputController.readDataModificationDocument();
		SettingVariableNamesDTO settingVariableNames;
		try {
			settingVariableNames = inputController.readSettingsSheet();
			values.setNameDataFolder(settingVariableNames.getNameDataFolder());
			values.setNameAndroidFolder(settingVariableNames.getNameAndroidFolder());
			values.setNameDataSheetFile(settingVariableNames.getNameDataSheetFile());
			values.setNameIOSFolder(settingVariableNames.getNameIOSFolder());
			values.setNameOutputExcelFile(settingVariableNames.getNameOutputExcelFile());
			values.setTypeNameForSumOfVariables(settingVariableNames.getTypeNameForSumAndDelete());
			
		} catch (UnknownSettingsVariableNameException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Scanner scanner = new Scanner(System.in);
			System.out.println("Press enter to close");
			scanner.nextLine();
			scanner.close();
			System.exit(0);
		}
		this.folderPrefix = String.format(this.folderPrefix, values.getNameDataFolder());
		
		ArrayList<RecurringDataEntry> recurringData = inputController.readRecurringDataSheet();
		
		
		
		
		
		//Get the file names and the path. 
		FilePathReader pathReader = new FilePathReader();
		try {
			pathReader.readFileNames();
		} catch (IncorrectDirectoryException e1) {
			e1.printStackTrace();
			Scanner scanner = new Scanner(System.in);
			System.out.println("Press enter to close");
			scanner.nextLine();
			scanner.close();
			System.exit(0);
			// TODO Auto-generated catch block
		}

		AnalyticsDTO generalData = new AnalyticsDTO();

		if(pathReader.isFolder()) {
			//All folders
			for(FileInformationDTO path : pathReader.getFileNames()) {
				
				String finalPath = folderPrefix + path.getFolder() + "/" + path.getName();
				String fileName = path.getName().replace(this.fileType, "");
				csvReader.readCSVFile(fileName,path.getFolder(), finalPath, generalData);
			}
			collectAppData(generalData);


		}


		else {
			//All files
			for(FileInformationDTO path : pathReader.getFileNames()) {
				String finalPath = folderPrefix + path.getName();
				String fileName = path.getName().replace(this.fileType, "");
				csvReader.readCSVFile(fileName,null, finalPath, generalData);
			}
		}



		//We have the data. Fill it into the excel file. 
		try {
			excelController.processData(generalData);
			//	excelController.createTable();
			//			//Collect all of the data in a sheet with no modifications
			//			excelController.sortData("TOTAL", data.get(generalDataKey));
			//			
			//			
			//			
			//			for(String sortBy : configReader.getConfigDTO().getSortBy()) {
			//				excelController.sortData(sortBy, data.get(generalDataKey));
			//				//excelController.sortDataByCategory(sortBy, "Unikke hændelser", data);
			//			}
			//			
			//			
			//			excelController.sortDataByPercentage("TOTAL", data);
			//			for(String sortBy : configReader.getConfigDTO().getSortBy()) {
			//				excelController.sortDataByPercentage(sortBy, data);
			//				//excelController.sortDataByCategory(sortBy, "Unikke hændelser", data);
			//			}
			//			
			//








			excelController.closeWorkbook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void collectAppData(AnalyticsDTO data) {
		AnalyticsDTO returnDTO = new AnalyticsDTO();
		String finalFolderName = "Total";
		ArrayList<String> usedFiles = new ArrayList<String>();
		FileDTO entry;

		String fileName = null;
		int[] row = new int[data.getCategoryList().size()];
		String[] rowString = new String[row.length];
		boolean[] rowBoolean = new boolean[row.length];	//if int true, if string false
		boolean rowChanged = false;

	
		ArrayList<String> dataRow = null;


		//Run through all of the files
		for(FileDTO fileNameDTO : data.getFileList()) {
			fileName = fileNameDTO.getFileName();
			if(usedFiles.contains(fileName)) {
				continue;
			}
			else {
				usedFiles.add(fileName);
			}
			entry = new FileDTO(fileName, finalFolderName);


			//Run through all of the events
			for(String event : data.getColumnNameList()) {
								//Run through all of the directories
				for(String directory : data.getDirectoryNameList()) {
					for(FileDTO dto : data.getFileList()) {

						if(dto.getDirectoryName().equals(directory) && dto.getFileName().equals(fileName)) {
							

							
							dataRow = dto.getRow(event);
							if(dataRow != null) {
								rowChanged = true;


								for(int i = 0;i<dataRow.size();i++) {
									if(!Character.isLetter(dataRow.get(i).charAt(0)) && Character.isDigit(dataRow.get(i).charAt(0))) {
										

										rowBoolean[i] = true;
										row[i] += Integer.parseInt(dataRow.get(i));
									}
									else {
						
										rowString[i] = dataRow.get(i);
									}
									
								}
							}

						}

					}


				}

				if (rowChanged) {
					rowChanged = false;
					for(int i = 0;i<row.length;i++) {
						if(rowBoolean[i]) {
							rowString[i] = "" + row[i];
						}
					}
					entry.addRow(event,  new ArrayList<String>(Arrays.asList(rowString)));
					row = new int[data.getCategoryList().size()];
					rowBoolean = new boolean[row.length];
					rowString = new String[row.length];
				}
			}
			
			entry.setKeys((ArrayList<String>) data.getColumnNameList());
			returnDTO.addFile(entry);

		}
		//		for(FileDTO dto : data.getFileList()) {
		//			returnDTO.addFile(dto);
		//		}

		for(FileDTO dto : returnDTO.getFileList()) {
			data.addFile(dto);
		}
		

	}




}
