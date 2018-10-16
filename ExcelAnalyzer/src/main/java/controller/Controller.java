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
import exceptions.IncorrectConfigFileException;
import exceptions.NoMatchingIdentifiersException;

public class Controller {



	//Folder values
	private final String folderPrefix = "./files/";


	private ConfigDTO config;
	private Map<String,AnalyticsDTO> data;
	public static final String activeUsersKey = "activeUsersData";
	public static final String generalDataKey = "generalData";
	public static final String sessionsKey = "sessionData";


	//ConfigReader
	private ConfigReader configReader;

	//CSVReader
	private CSVReader csvReader;

	//ExcelController
	private ExcelController excelController;




	public Controller() throws EncryptedDocumentException, InvalidFormatException, IOException {
		configReader = new ConfigReader();
		csvReader = new CSVReader();
		excelController = new ExcelController();
		data = new HashMap<String, AnalyticsDTO>();
		config = new ConfigDTO();


	}



	/**
	 * Main function. 
	 */
	public void analyze() {
		//Read the config file
		try {
			configReader.readConfigFile();
			this.config = configReader.getConfigDTO();

		} catch (IncorrectConfigFileException e) {

			e.printStackTrace();
			configReader.resetConfigFile();
			System.out.println("Config file incorrect. It has been reset.");

		}


		//Read the general data. 
		if(!config.isGeneralDataPresent()) {
			System.out.println("No general data present");
			System.exit(1);
		}
		AnalyticsDTO generalData = new AnalyticsDTO();
		generalData.setConfigDTO(config);
		for(String path : config.getGeneralPathNames()) {
			String finalPath = folderPrefix + config.getGeneralDataFolderName() + "/"+  path;
			String fileName = path.replace(".csv", "");

			
			csvReader.readCSVFile(fileName, finalPath, generalData);
		}
		data.put(Controller.generalDataKey, generalData);
		System.out.println(data.get(Controller.generalDataKey).getSheetList().size());


		//Read the user data
		if(config.isUserDataPresent()) {
			AnalyticsDTO userData = new AnalyticsDTO();
			userData.setConfigDTO(config);
			for(String path : config.getUsersPathNames()) {
				String finalPath = folderPrefix + config.getUserDataFolderName() + "/" + path;
				String fileName = path.replace(".csv", "");

				csvReader.readCSVFile(fileName, finalPath, userData);
			}
			data.put(Controller.activeUsersKey, userData);

		}


		//Read the session data
		if(config.isSessionDataPresent()) {
			AnalyticsDTO sessionData = new AnalyticsDTO();
			sessionData.setConfigDTO(config);
			for(String path : config.getSessionPathName()) {
				String finalPath = folderPrefix + config.getSessionDataFolderName() + "/" + path;
				String fileName = path.replace(".csv", "");
				
				csvReader.readCSVFile(fileName, finalPath, sessionData);
			}
			data.put(Controller.sessionsKey, sessionData);

		}






		//We have the data. Fill it into the excel file. 
		try {
			
			//Collect all of the data in a sheet with no modifications
			excelController.sortData("TOTAL", data.get(generalDataKey));
			
			
			
			for(String sortBy : configReader.getConfigDTO().getSortBy()) {
				excelController.sortData(sortBy, data.get(generalDataKey));
				//excelController.sortDataByCategory(sortBy, "Unikke hændelser", data);
			}
			
			
			excelController.sortDataByPercentage("TOTAL", data);
			for(String sortBy : configReader.getConfigDTO().getSortBy()) {
				excelController.sortDataByPercentage(sortBy, data);
				//excelController.sortDataByCategory(sortBy, "Unikke hændelser", data);
			}
			









			excelController.closeWorkbook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoMatchingIdentifiersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}







}
