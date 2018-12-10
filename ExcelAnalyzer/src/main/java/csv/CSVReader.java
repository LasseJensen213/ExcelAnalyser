package csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.FileDTO;

public class CSVReader {

	private BufferedReader reader;

	private String line;
	private String csvSplitBy = ",";
	private char commentChar = '#';

	/**
	 * Reads a csv file and saves the information in the analyticsDTO
	 * 
	 * 
	 * @param fileName
	 *            - String
	 * @param finalPath
	 * @param analyticsDTO
	 *            - AnalyticsDTO
	 * 
	 */
	public void readCSVFile(String fileName, String directoryName, String finalPath, AnalyticsDTO analyticsDTO) {

		// Create a new sheetDTO with the fileName
		analyticsDTO.addDirectory(directoryName);
		FileDTO csvFile = new FileDTO(fileName, directoryName);

		try {
			// Create the reader
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					finalPath), "UTF-8"));

			// Boolean to check if we've reached the first line.
			boolean firstLineReached = false;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				// # Means comment
				// Check if the line is a comment or if it is empty.
				if (line.isEmpty() || line.charAt(0) == commentChar) {
					continue;
				}

				// Check for "". If it has "" consider the text between "" as a part.

				if (line.contains("\"")) {
					// Replace any commas between the "" with a semi colon.

					int startIndex = line.indexOf("\"");
					int endIndex = line.lastIndexOf("\"") + 1;

					String annotatedString = line.substring(startIndex, endIndex);
					String newAnnotatedString = annotatedString.replace(',', ';');
					line = line.replace(annotatedString, newAnnotatedString);

				}

				// Split the current line into individual parts
				String[] row = line.split(csvSplitBy);

				// Change the semi colon back to a comma.
				for (int i = 0; i < row.length; i++) {
					if (row[i].contains(";")) {
						row[i] = row[i].replace(";", ",");
						row[i] = row[i].replaceAll("\"", "");

					}

				}
				// If the value is an integer, change the dot to a comma
				for (int i = 0; i < row.length; i++) {
					if (!Character.isLetter(row[i].charAt(0))) {

						if (row[i].contains(".")) {
							row[i] = row[i].replace(".", "");

						}
					}
				}
				// Add the categories

				// Hardcoding stuff

				if (!firstLineReached) {
					for (String input : row) {
						// if(input.contains("æ")) {
						// input.replace("æ", "æ");
						// }

						if (!analyticsDTO.categoryKnown(input)) {
							analyticsDTO.addCategory(input);
						}
					}
					firstLineReached = true;
					continue;
				}

				// Assign information to variables
				String eventName = row[0] + ";" + row[1] + ";" + row[2];
		

				// if the event name isn't known, add it to the collective list.
				if (!analyticsDTO.eventKnown(eventName)) {
					analyticsDTO.addEventName(eventName);
				}

				ArrayList<String> rowValues = new ArrayList<String>();

				// Run through values in the row.
				for (String rowValue : row) {

					rowValues.add(rowValue);

				}
				// System.out.println("");

				csvFile.addRow(eventName, rowValues);

			}
			analyticsDTO.addFile(csvFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
