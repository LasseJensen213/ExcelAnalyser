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
	 * @param filEName - String 
	 * @param finalPath 
	 * @param analyticsDTO - AnalyticsDTO
	 * 
	 */
	public void readCSVFile(String fileName, String finalPath, AnalyticsDTO analyticsDTO) {
		SheetDTO sheet = new SheetDTO(fileName);

		try {
			reader = new BufferedReader(new FileReader(finalPath));
			boolean firstLineReached = false;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				//#Means comment

				if(line.isEmpty() || line.charAt(0) == commentChar) {
					continue;
				}

				//Split the current line into individual parts
				String[] row = line.split(csvSplitBy);

				if(!firstLineReached) {
					String categoryToBeAdded = null;
					String firstPart = null;
					String secondPart = null;
					for(String input : row) {
						if(input.contains("\"")) {
							input = input.replace("\"", "");
							
							if(firstPart == null) {
								firstPart = input;
							}
							else if(secondPart == null) {
								secondPart = input;
								
								categoryToBeAdded = firstPart + "," + secondPart;
								firstPart = null;
								secondPart = null;
							}
							
						}
						else {
							categoryToBeAdded = input;
						}
						if(!analyticsDTO.categoryKnown(categoryToBeAdded)) {
							analyticsDTO.addCategory(categoryToBeAdded);
						}
					}
					firstLineReached = true;
					
					continue;
				}

				//Assign information to variables
				String eventName = row[0];
				if(eventName.isEmpty()) {
					break;
				}

				//if the event name isn't known, add it to the collective list. 
				if(!analyticsDTO.eventKnown(eventName)) {
					analyticsDTO.addEventName(eventName);
				}

				ArrayList<String> rowValues = new ArrayList<String>();
				//Run through values in the row. If it contains a " it means that a value was split up.
				String firstPart = null;
				String secondPart = null;
				for(String rowValue : row) {
					if(rowValue.contains("\"")) {
						//First we remove the "
						rowValue = rowValue.replace("\"", "");

						//Then we save it to the correct part
						if(firstPart == null) {
							firstPart = rowValue;
						}
						else {
							secondPart = rowValue;
							rowValues.add(firstPart + "." + secondPart);
						}
						continue;

					}
					rowValues.add(rowValue);

				}
				sheet.addRow(rowValues);

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
