package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import configReader.ConfigReader;
import csv.CSVReader;
import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.ConfigDTO;
import excel.ExcelController;
import exceptions.NoMatchingIdentifiersException;
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
		pathReader.readFileNames();
		
		
		AnalyticsDTO generalData = new AnalyticsDTO();
		for(String path : pathReader.getFileNames()) {
			String finalPath = folderPrefix + path;
			String fileName = path.replace(".csv", "");

			
			csvReader.readCSVFile(fileName, finalPath, generalData);
		}
		
		System.out.println("EndBreakPoint");
	

		//We have the data. Fill it into the excel file. 
		try {
			excelController.processData(generalData);
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







}
