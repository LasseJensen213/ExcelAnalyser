package csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataTransferObjects.AnalyticsDTO;
import dataTransferObjects.FileDTO;

public class CSVReader {

	private BufferedReader reader;

	private String line;
	private String csvSplitBy = ",";
	private char commentChar = '#';

	private String[] androidUsers = {"821","745","803","697","713","678","635","646","681"};
	private String[] iosUsers = {"1543","1514","1825","1452","1513","1483","1342","1352","1398"};
	private String[] androidSessions = {"3976","3334","3812","2774","3443","3290","2231","2957","3802"};
	private String[] iosSessions = {"13071","11226","15920","11481","12468","11678","8044","11427","12382"};

	private String[] months = {"Januar 2018","Februar 2018","Marts 2018","April 2018", "Maj 2018", "Juni 2018", "Juli 2018", "August 2018", "September 2018"};

	
	
	

	/**
	 * Reads a csv file and saves the information in the analyticsDTO
	 * 
	 * 
	 * @param fileName - String 
	 * @param finalPath 
	 * @param analyticsDTO - AnalyticsDTO
	 * 
	 */
	public void readCSVFile(String fileName, String directoryName, String finalPath, AnalyticsDTO analyticsDTO) {
		
		//Create a new sheetDTO with the fileName
		analyticsDTO.addDirectory(directoryName);
		FileDTO csvFile = new FileDTO(fileName,directoryName);

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
				//If the value is an integer, change the dot to a comma
				for(int i = 0;i<row.length;i++) {				
					if(!Character.isLetter(row[i].charAt(0))) {


						if(row[i].contains(".")) {
							row[i] = row[i].replace(".", "");

						}
					}
				}
				//Add the categories
				
				//Hardcoding stuff
				
				
				if(!firstLineReached) {
					for(String input : row) {
//						if(input.contains("æ")) {
//							input.replace("æ", "æ");
//						}

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
				if(eventName.contains("MAP;SELECT;MAP:SELECT:Vejarbejde")) {
					eventName = "MAP;SELECT;MAP:SELECT:Vejarbejde";
				} else if(eventName.contains("MAP;SELECT;MAP:SELECT:Trafik"))  {
					eventName = "MAP;SELECT;MAP:SELECT:Trafikinfo";

				}
				if(!analyticsDTO.eventKnown(eventName)) {
					analyticsDTO.addEventName(eventName);
				}

				ArrayList<String> rowValues = new ArrayList<String>();

				//Run through values in the row.
				for(String rowValue : row) {
					if(rowValue.contains("section_")) {
						String replacement = rowValue.replace("section_", "section:");
						rowValues.add(replacement);
						continue;
					}
					rowValues.add(rowValue);

				}
				//System.out.println("");
				
				for(int i = 0;i<this.months.length;i++) {
					if(fileName.contains(this.months[i])) {
						if(directoryName.equals("Android"))  {
							rowValues.add(androidUsers[i]);
							rowValues.add(androidSessions[i]);
						}
						else if(directoryName.equals("IOS")) {
							rowValues.add(iosUsers[i]);
							rowValues.add(iosSessions[i]);
						}
						
						break;
					}
				}
				csvFile.addRow(eventName, rowValues);

			}
			//We know have all the rows - Add the sheet to the analyticsDTO
			if(!analyticsDTO.categoryKnown("Total - Brugere")) {
				analyticsDTO.addCategory("Total - Brugere");
			}
			if(!analyticsDTO.categoryKnown("Total - Sessioner")) {
				analyticsDTO.addCategory("Total - Sessioner");
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
