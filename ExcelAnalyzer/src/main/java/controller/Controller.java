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



	private final String fileType = ".csv";

	GlobalValues values;

	public Controller() throws EncryptedDocumentException, InvalidFormatException, IOException {
		csvReader = new CSVReader();
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
		System.out.println("Indlæser...");

		ExcelInputController inputController = new ExcelInputController();
		try {
			System.out.println("Læser kontrol dokument");

			inputController.readControlDocoument();
		} catch (UnknownSettingsVariableNameException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		this.folderPrefix = String.format(this.folderPrefix, values.getNameDataFolder());

		// Get the file names and the path.
		FilePathReader pathReader = new FilePathReader();
		try {
			System.out.println("Læser fil navne");
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

		System.out.println("Læser .csv filer");
		AnalyticsDTO generalData = readDataFiles();

		System.out.println("Omdøber hændelsesetiketer");
		renameDataVariables(generalData);

		System.out.println("Summérer og sletter variable");
		generalData = sumAndDelete(generalData);

		System.out.println("Tilføjer gengående data");
		addRecurringData(generalData);

		if (generalData.isMultipleFolders()) {
			System.out.println("Lægger data sammen");
			collectAppData(generalData);
		}

		ExcelOutputController excelController = new ExcelOutputController();

		// We have the data. Fill it into the excel file.
		try {
			System.out.println("Indsætter data i excel ark");
			excelController.processData(generalData);

			System.out.println("Gemmer excel ark");
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
		for(int i = 0;i<analyticsDTO.getFileList().size();i++) {
			FileDTO data = analyticsDTO.getFileList().get(i);
			for (String key : data.getKeys()) {
				ArrayList<String> rows = data.getRow(key);
				if(rows == null) {
					continue;
				}
				String row = rows.get(2);
				for (RenameVariableDTO renameVariable : values.getDataModificationDTO().getRenameVariableList()) {
					if (row.equals(renameVariable.getOriginalName())) {
						rows.set(2, renameVariable.getNewName());
					}
				}

				data.updateRow(key, rows);

			}
			analyticsDTO.getFileList().set(i, data);

		}
	}

	/**
	 * Adds to variables together and deletes the last.
	 * 
	 * @param analyticsDTO
	 */
	private AnalyticsDTO sumAndDelete(AnalyticsDTO analyticsDTO) {
		for(SumAndDeleteDTO sumAndDeleteDTO : values.getDataModificationDTO().getSumAndDeleteList()) {
			values.getDataModificationDTO().getRenameVariableList().add(new RenameVariableDTO(sumAndDeleteDTO.getVariableToDelete(), sumAndDeleteDTO.getVariableToKeep()));
		}

		for(int i = 0;i<analyticsDTO.getFileList().size();i++) {
			FileDTO data = analyticsDTO.getFileList().get(i);

			ArrayList<String> keysToSkip = new ArrayList<String>();
			ArrayList<String> toBeKept;
			String keepKey = null;
			String deleteKey = null;
			ArrayList<String> toBeDeleted;
			for (SumAndDeleteDTO sumAndDeleteDTO : values.getDataModificationDTO().getSumAndDeleteList()) {
				keepKey = null;
				deleteKey = null;
				for (String key : data.getKeys()) {
					if(keysToSkip.contains(key)) {
						continue;
					}
					String splitKey[] = key.split(";");
					if(splitKey[2].equals(sumAndDeleteDTO.getVariableToKeep())) {
						toBeKept = data.getRow(key);
						keepKey = key;
						break;
					}
				}

				for (String key : data.getKeys()) {
					String splitKey[] = key.split(";");
					if(splitKey[2].equals(sumAndDeleteDTO.getVariableToDelete())) {
						toBeDeleted = data.getRow(key);
						deleteKey = key;
						break;
					}
				}
				if(deleteKey == null || keepKey == null) {
					continue;
				}
				else {
					
					ArrayList<String> sum = addVariables(data.getRow(keepKey), data.getRow(deleteKey));
					data.deleteRow(deleteKey);
					data.removeKey(keepKey);
					data.addRow(keepKey, sum);
					keysToSkip.add(deleteKey);
					analyticsDTO.getFileList().set(i, data);
				}
			}


		}
		renameDataVariables(analyticsDTO);
		return analyticsDTO;
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
		for(int i = 0;i<analyticsDTO.getFileList().size();i++) {
			FileDTO data = analyticsDTO.getFileList().get(i);
			if(data.getDirectoryName() == null) {
				for (RecurringData recurringData : values.getRecurringData()) {
					for (RecurringDataEntry entry : recurringData.getRecurringData()) {
						String entryID = entry.getFileName();
						if (entryID.equals(data.getSheetName())) {
							for (String key : data.getKeys()) {
								for (String recurringDataKey : entry.getKeys()) {

									ArrayList<String> rows = data.getRow(key);
									if (rows == null) {
										continue;
									}
									String toBeAdded = entry.getData().get(recurringDataKey).toString();
									if(toBeAdded == null) {
										System.out.println("asdf");
									}
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
			else {
				for (RecurringData recurringData : values.getRecurringData()) {
					if (recurringData.getFolder().equals(data.getDirectoryName())) {
						for (RecurringDataEntry entry : recurringData.getRecurringData()) {
							String entryID = entry.getFileName();
							if (entryID.equals(data.getSheetName())) {
								for (String key : data.getKeys()) {
									for (String recurringDataKey : entry.getKeys()) {

										ArrayList<String> rows = data.getRow(key);
										if (rows == null) {
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
			}
			// if(data.getSheetName().equals(values.getRecurringData().g))
			analyticsDTO.getFileList().set(i, data);

		}
	}

	public void collectAppData(AnalyticsDTO data) {
		AnalyticsDTO returnDTO = new AnalyticsDTO();
		String finalFolderName = values.getTypeNameForSumOfVariables();
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
