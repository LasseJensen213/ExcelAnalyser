package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import csv.CSVReader;
import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.FileDTO;
import dataTransferObjects.FileInformationDTO;
import dataTransferObjects.RecurringData;
import dataTransferObjects.RecurringDataEntry;
import dataTransferObjects.RenameVariableDTO;
import dataTransferObjects.SumAndDeleteDTO;
import excel.ExcelInputController;
import excel.ExcelOutputController;
import exceptions.IncorrectDirectoryException;
import exceptions.UnknownSettingsVariableNameException;
import filePathReader.FilePathReader;
import globalValues.GlobalValues;

public class Controller {

	// Folder values
	private String folderPrefix = "./%s/";

	public static final String activeUsersKey = "activeUsersData";
	public static final String generalDataKey = "generalData";
	public static final String sessionsKey = "sessionData";

	// CSVReader
	private CSVReader csvReader;

	// ExcelController
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
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	public void analyze() throws EncryptedDocumentException, InvalidFormatException, IOException {
		// Read the control document
		ExcelInputController inputController = new ExcelInputController();
		try {
			inputController.readControlDocoument();
		} catch (UnknownSettingsVariableNameException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		this.folderPrefix = String.format(this.folderPrefix, values.getNameDataFolder());

		// Get the file names and the path.
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

		AnalyticsDTO generalData = readDataFiles();

		renameDataVariables(generalData);

		sumAndDelete(generalData);

		addRecurringData(generalData);

		if (generalData.isMultipleFolders()) {
			collectAppData(generalData);
		}

		// We have the data. Fill it into the excel file.
		try {
			excelController.processData(generalData);

			excelController.closeWorkbook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Reads all the CSV files and returns the data in an AnalyticsDTO object
	 * 
	 * @return AnalyticsDTO
	 */
	private AnalyticsDTO readDataFiles() {
		// Get the file names and the path.
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

		if (pathReader.isFolder()) {
			// All folders
			for (FileInformationDTO path : pathReader.getFileNames()) {

				String finalPath = folderPrefix + path.getFolder() + "/" + path.getName();
				String fileName = path.getName().replace(this.fileType, "");
				csvReader.readCSVFile(fileName, path.getFolder(), finalPath, generalData);
			}

			generalData.setMultipleFolders(true);
		}

		else {
			// All files
			for (FileInformationDTO path : pathReader.getFileNames()) {
				String finalPath = folderPrefix + path.getName();
				String fileName = path.getName().replace(this.fileType, "");
				csvReader.readCSVFile(fileName, null, finalPath, generalData);
			}
			generalData.setMultipleFolders(false);

		}
		return generalData;
	}

	/**
	 * Renames the "hændelsesetiket"
	 * 
	 * @param analyticsDTO
	 */
	private void renameDataVariables(AnalyticsDTO analyticsDTO) {
		for (FileDTO data : analyticsDTO.getFileList()) {
			for (String key : data.getKeys()) {
				ArrayList<String> rows = data.getRow(key);
				String row = rows.get(2);
				for (RenameVariableDTO renameVariable : values.getDataModificationDTO().getRenameVariableList()) {
					if (row.equals(renameVariable.getOriginalName())) {
						rows.set(2, renameVariable.getNewName()); 
					}
				}

				data.updateRow(key, rows);

			}
		}
	}

	/**
	 * Adds to variables together and deletes the last.
	 * 
	 * @param analyticsDTO
	 */
	private void sumAndDelete(AnalyticsDTO analyticsDTO) {
		for (FileDTO data : analyticsDTO.getFileList()) {
			ArrayList<String> keysToSkip = new ArrayList<String>();

			for (String key : data.getKeys()) {
				if (keysToSkip.contains(key)) {
					continue;
				}
				for (SumAndDeleteDTO sumAndDeleteDTO : values.getDataModificationDTO().getSumAndDeleteList()) {
					if (key.contains(sumAndDeleteDTO.getVariableToKeep())) {
						for (String deleteKey : data.getKeys()) {
							if (deleteKey.contains(sumAndDeleteDTO.getVariableToDelete()) && !key.equals(deleteKey)) {
								ArrayList<String> sum = addVariables(data.getRow(key), data.getRow(deleteKey));
								data.deleteRow(deleteKey);
								data.updateRow(key, sum);
								keysToSkip.add(deleteKey);
								break;
							}
						}
					}
				}

			}
		}
	}

	private ArrayList<String> addVariables(ArrayList<String> variable1, ArrayList<String> variable2) {
		ArrayList<String> finalElement;

		int[] row = new int[variable1.size()];
		String[] rowString = new String[row.length];
		boolean[] rowBoolean = new boolean[row.length]; // if int true, if string false

		for (int i = 0; i < variable1.size(); i++) {
			if (!Character.isLetter(variable1.get(i).charAt(0)) && Character.isDigit(variable1.get(i).charAt(0))) {

				rowBoolean[i] = true;
				row[i] += Integer.parseInt(variable1.get(i));
				row[i] += Integer.parseInt(variable2.get(i));

			} else {
				rowString[i] = variable1.get(i);
			}

		}
		for (int i = 0; i < row.length; i++) {
			if (rowBoolean[i]) {
				rowString[i] = "" + row[i];
			}
		}
		finalElement = new ArrayList<String>(Arrays.asList(rowString));

		return finalElement;
	}

	private void addRecurringData(AnalyticsDTO analyticsDTO) {
		for (FileDTO data : analyticsDTO.getFileList()) {
			System.out.println("asfd");
			for (RecurringData recurringData : values.getRecurringData()) {
				if (recurringData.getFolder().equals(data.getDirectoryName())) {
					for (RecurringDataEntry entry : recurringData.getRecurringData()) {
						String entryID = entry.getMonth() + " " + entry.getYear();
						if (entryID.equals(data.getSheetName())) {
							for (String key : data.getKeys()) {
								for (String recurringDataKey : entry.getKeys()) {

									ArrayList<String> rows = data.getRow(key);
									if(rows == null) {
										continue;
									}
									String toBeAdded = entry.getData().get(recurringDataKey).toString();
									rows.add(toBeAdded);
									if (!analyticsDTO.categoryKnown(recurringDataKey)) {
										analyticsDTO.addCategory(recurringDataKey);
									}

								}

							}
							break;
						}
					}
			
				}
			}
			// if(data.getSheetName().equals(values.getRecurringData().g))

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
		boolean[] rowBoolean = new boolean[row.length]; // if int true, if string false
		boolean rowChanged = false;

		ArrayList<String> dataRow = null;

		// Run through all of the files
		for (FileDTO fileNameDTO : data.getFileList()) {
			fileName = fileNameDTO.getSheetName();
			if (usedFiles.contains(fileName)) {
				continue;
			} else {
				usedFiles.add(fileName);
			}
			entry = new FileDTO(fileName, finalFolderName);

			// Run through all of the events
			for (String event : data.getColumnNameList()) {
				// Run through all of the directories
				for (String directory : data.getDirectoryNameList()) {
					for (FileDTO dto : data.getFileList()) {

						if (dto.getDirectoryName().equals(directory) && dto.getSheetName().equals(fileName)) {

							dataRow = dto.getRow(event);
							if (dataRow != null) {
								rowChanged = true;

								for (int i = 0; i < dataRow.size(); i++) {
									if (!Character.isLetter(dataRow.get(i).charAt(0))
											&& Character.isDigit(dataRow.get(i).charAt(0))) {

										rowBoolean[i] = true;
										row[i] += Integer.parseInt(dataRow.get(i));
									} else {

										rowString[i] = dataRow.get(i);
									}

								}
							}

						}

					}

				}

				if (rowChanged) {
					rowChanged = false;
					for (int i = 0; i < row.length; i++) {
						if (rowBoolean[i]) {
							rowString[i] = "" + row[i];
						}
					}
					entry.addRow(event, new ArrayList<String>(Arrays.asList(rowString)));
					row = new int[data.getCategoryList().size()];
					rowBoolean = new boolean[row.length];
					rowString = new String[row.length];
				}
			}

			entry.setKeys((ArrayList<String>) data.getColumnNameList());
			returnDTO.addFile(entry);

		}
		// for(FileDTO dto : data.getFileList()) {
		// returnDTO.addFile(dto);
		// }

		for (FileDTO dto : returnDTO.getFileList()) {
			data.addFile(dto);
		}

	}

}
