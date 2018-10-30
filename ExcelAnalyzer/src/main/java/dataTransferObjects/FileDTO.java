package dataTransferObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileDTO {


	private String sheetName;
	private String directoryName;
	private Map<String, ArrayList<String>> rows;
	private ArrayList<String> keys;


	public FileDTO(String sheetName, String directoryName) {
		rows = new HashMap<String, ArrayList<String>>();
		this.sheetName = sheetName;
		this.directoryName = directoryName;
		this.keys = new ArrayList<String>();
	}


	public String getFileName() {
		return sheetName;
	}



	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}



	public void addRow(String key, ArrayList<String> element) {
		String roadWorkKey = "MAP;SELECT;MAP:SELECT:Vejarbejde"; 
		String trafficInfoKey = "MAP;SELECT;MAP:SELECT:Trafik";
		if(key.contains(roadWorkKey)) {
			key = roadWorkKey;
			ArrayList<String> finalElement;
			if(rows.containsKey(key)) {
				ArrayList<String> dataRow = rows.get(key);
				int[] row = new int[dataRow.size()];
				String[] rowString = new String[row.length];
				boolean[] rowBoolean = new boolean[row.length];	//if int true, if string false

				for(int i = 0;i<dataRow.size();i++) {
					if(!Character.isLetter(dataRow.get(i).charAt(0)) && Character.isDigit(dataRow.get(i).charAt(0))) {
						if(i==6 || i==7) {
							rowBoolean[i] = true;
							row[i] += Integer.parseInt(dataRow.get(i));
						}
						else {
							rowBoolean[i] = true;
							row[i] += Integer.parseInt(dataRow.get(i));
							row[i] += Integer.parseInt(element.get(i));
						}
					}
					else {
						if(dataRow.get(i).contains("MAP:SELECT:Vejarbejde")) {
							rowString[i] = "MAP:SELECT:Vejarbejde";
						}
						rowString[i] = dataRow.get(i);
					}

				}
				for(int i = 0;i<row.length;i++) {
					if(rowBoolean[i]) {
						rowString[i] = "" + row[i];
					}
				}
				finalElement = new ArrayList<String>(Arrays.asList(rowString));
				rows.replace(key, finalElement);

			}
			else {
				for(int i = 0;i < element.size();i++) {
					if(element.get(i).contains("MAP:SELECT:Vejarbejde")) {
						element.set(i,"MAP:SELECT:Vejarbejde");
					}
				}
				rows.put(key, element);
			}
		}
		else if(key.contains(trafficInfoKey)) {
			key = trafficInfoKey;
			ArrayList<String> finalElement;
			if(rows.containsKey(key)) {
				ArrayList<String> dataRow = rows.get(key);
				int[] row = new int[dataRow.size()];
				String[] rowString = new String[row.length];
				boolean[] rowBoolean = new boolean[row.length];	//if int true, if string false

				for(int i = 0;i<dataRow.size();i++) {
					if(!Character.isLetter(dataRow.get(i).charAt(0)) && Character.isDigit(dataRow.get(i).charAt(0))) {
						if(i==6 || i==7) {
							rowBoolean[i] = true;
							row[i] += Integer.parseInt(dataRow.get(i));
						}
						else {
							rowBoolean[i] = true;
							row[i] += Integer.parseInt(dataRow.get(i));
							row[i] += Integer.parseInt(element.get(i));
						}

					}
					else {
						if(dataRow.get(i).contains("MAP:SELECT:Trafik")) {
							rowString[i] = "MAP:SELECT:Trafikinfo";
						}
						rowString[i] = dataRow.get(i);
					}

				}
				for(int i = 0;i<row.length;i++) {
					if(rowBoolean[i]) {
						rowString[i] = "" + row[i];
					}
				}
				finalElement = new ArrayList<String>(Arrays.asList(rowString));
				rows.replace(key, finalElement);

			}
			else {
				for(int i = 0;i < element.size();i++) {
					if(element.get(i).contains("MAP:SELECT:Trafik")) {
						element.set(i,"MAP:SELECT:Trafikinfo");
					}
				}
				rows.put(key, element);
			}
		}
		else {
			if(!rows.containsKey(key)) {
				this.rows.put(key, element);

			}
		}
		this.keys.add(key);


		//this.keys.add(key);
		//		this.rows.put(key, element);
	}

	public boolean contains(String event) {
		return rows.containsKey(event);
	}



	public ArrayList<String> getRow(String key) {
		return rows.get(key);
	}

	public int getNumberOfRows() {
		return this.rows.size();
	}


	public String getDirectoryName() {
		return directoryName;
	}


	@Override
	public String toString() {
		return "FileDTO [sheetName=" + sheetName + ", directoryName=" + directoryName + "]";
	}


	public ArrayList<String> getKeys() {
		return keys;
	}


	public void setKeys(ArrayList<String> keys) {
		this.keys = keys;
	}






}
