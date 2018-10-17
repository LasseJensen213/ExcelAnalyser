package csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.RowElementDTO;
import dataTransferObjects.SheetDTO;

public class CSVReader {

	private BufferedReader reader;

	private String line;
	private String csvSplitBy = ",";
	private char commentChar = '#';



	/**
	 * Reads a csv file and saves the information in the analyticsDTO
	 * 
	 * 
	 * @param fileName - String 
	 * @param finalPath 
	 * @param analyticsDTO - AnalyticsDTO
	 * 
	 */
	public void readCSVFile(String fileName, String finalPath, AnalyticsDTO analyticsDTO) {
		//Create a new sheetDTO with the fileName
		SheetDTO sheet = new SheetDTO(fileName);

		try {
			//Create the reader
			reader = new BufferedReader(new FileReader(finalPath));

			//Boolean to check if we've reached the first line. 
			boolean firstLineReached = false;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				//# Means comment

				//Check if the line is a comment or if it is empty. 
				if(line.isEmpty() || line.charAt(0) == commentChar) {
					continue;
				}

				//Check for "". If it has "" consider the text between "" as a part. 

				if(line.contains("\"")) {
					//Replace any commas between the "" with a semi colon. 

					int startIndex = line.indexOf("\"");
					int endIndex = line.lastIndexOf("\"")+1;

					String annotatedString = line.substring(startIndex, endIndex);
					String newAnnotatedString = annotatedString.replace(',', ';');
					line = line.replace(annotatedString, newAnnotatedString);

				}

				//Split the current line into individual parts
				String[] row = line.split(csvSplitBy);

				//Change the semi colon back to a comma. 
				for(int i = 0; i< row.length;i++) {
					if(row[i].contains(";")) {
						row[i] = row[i].replace(";", ",");
						row[i] = row[i].replaceAll("\"", "");
						
					}
					
				}
				for(int i = 3;i<row.length;i++) {
					if(row[i].contains(".")) {
						row[i] = row[i].replace(".", "");

					}
				}
				//Add the categories
				if(!firstLineReached) {
					for(String input : row) {
						if(!analyticsDTO.categoryKnown(input)) {
							analyticsDTO.addCategory(input);
						}
					}
					firstLineReached = true;
					continue;
				}

				//Assign information to variables
				String eventName = row[0] + ";" + row[1] + ";" + row[2];
				if(eventName.isEmpty()) {
					break;
				}

				//if the event name isn't known, add it to the collective list. 
				if(!analyticsDTO.eventKnown(eventName)) {
					analyticsDTO.addEventName(eventName);
				}

				ArrayList<String> rowValues = new ArrayList<String>();
				//Run through values in the row.
				for(String rowValue : row) {
					
					rowValues.add(rowValue);

				}
				sheet.addRow(eventName, rowValues);

			}
			//We know have all the rows - Add the sheet to the analyticsDTO
			analyticsDTO.addSheet(sheet);




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
