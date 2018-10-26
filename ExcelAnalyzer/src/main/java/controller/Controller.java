package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import csv.CSVReader;
import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.FileInformationDTO;
import dataTransferObjects.FileDTO;
import excel.ExcelController;
import exceptions.IncorrectDirectoryException;
import filePathReader.FilePathReader;

public class Controller {



	//Folder values
	private final String folderPrefix = "./files/";


	private Map<String,AnalyticsDTO> data;
	public static final String activeUsersKey = "activeUsersData";
	public static final String generalDataKey = "generalData";
	public static final String sessionsKey = "sessionData";

	//CSVReader
	private CSVReader csvReader;

	//ExcelController
	private ExcelController excelController;




	public Controller() throws EncryptedDocumentException, InvalidFormatException, IOException {
		csvReader = new CSVReader();
		excelController = new ExcelController();
		data = new HashMap<String, AnalyticsDTO>();


	}



	/**
	 * Main function. 
	 */
	public void analyze() {

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
				String fileName = path.getName().replace(".csv", "");
				csvReader.readCSVFile(fileName,path.getFolder(), finalPath, generalData);
			}
			collectAppData(generalData);


		}


		else {
			//All files
			for(FileInformationDTO path : pathReader.getFileNames()) {
				String finalPath = folderPrefix + path.getName();
				String fileName = path.getName().replace(".csv", "");
				csvReader.readCSVFile(fileName,null, finalPath, generalData);
			}
		}

		System.out.println("EndBreakPoint");


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

						if(dto.getDirectoryName() == directory && dto.getFileName() == fileName) {

							if(dto.getRow(event) != null) {
								rowChanged = true;


								for(int i = 0;i<dto.getRow(event).size();i++) {
									try {
										rowBoolean[i] = true;
										row[i] += Integer.parseInt(dto.getRow(event).get(i));
									}
									catch(NumberFormatException e) {
										rowBoolean[i] = false;
										rowString[i] = dto.getRow(event).get(i);
									}
								}
							}

						}

					}

					System.out.println("");

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
