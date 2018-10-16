package configReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import dataTransferObjects.ConfigDTO;
import exceptions.IncorrectConfigFileException;

public class ConfigReader {


	private final String configFilePath = "./config.txt";
	private final String commentChar = "#";
	private final String splitLineBy = ":";
	private final String splitDataBy = ",";


	private ConfigDTO configDTO;


	private BufferedReader reader;





	public ConfigReader() {
		super();
		this.configDTO = new ConfigDTO();
	}


	public void readConfigFile() throws IncorrectConfigFileException{

		//
		String line = null;
		try {
			Reader reader = new InputStreamReader(new FileInputStream(this.configFilePath),"CP1252");
			this.reader = new BufferedReader(reader);
			while ((line = this.reader.readLine()) != null) {
				System.out.println(line);
				//#Means comment
				if(line.startsWith(commentChar) || line.isEmpty()) {
					continue;
				}
				String[] inputLine = line.split(this.splitLineBy);
				final String dir = System.getProperty("user.dir");

				switch (configFileElement.fromString(inputLine[0].trim())) {
				case FOLDER_GENERAL_DATA:


					//Get the path of the folder
					String toSortFolderPath = dir + "\\files\\" + inputLine[1].trim() + "\\";
					
					File toSortFolder = new File(toSortFolderPath);
					File[] filesToSort = toSortFolder.listFiles();
					
					if(filesToSort.length == 0) {
						System.out.println("No data to sort");
						configDTO.setGeneralDataPresent(false);
						continue;
					}
					
					configDTO.setGeneralDataFolderName(inputLine[1].trim());
					for(int i = 0;i<filesToSort.length;i++) {
						if(filesToSort[i].isFile()) {
							System.out.println("File: " + filesToSort[i].getName());
							configDTO.addGeneralPathName(filesToSort[i].getName());
						}
					}

					break;
				case FOLDER_ACTIVE_USERS:
					//Get the path of the folder
					String activeUsersFolderPath = dir + "\\files\\" + inputLine[1].trim() + "\\";
					File activeUserFolder = new File(activeUsersFolderPath);
					File[] activeUsers = activeUserFolder.listFiles();
					
					if(activeUsers.length == 0) {
						System.out.println("No user files present");
						configDTO.setUserDataPresent(false);
						continue;
					}
					
					configDTO.setUserDataFolderName(inputLine[1].trim());

					for(int i = 0;i<activeUsers.length;i++) {
						if(activeUsers[i].isFile()) {
							System.out.println("File: " + activeUsers[i].getName());
							configDTO.addUsersPathName(activeUsers[i].getName());
						}
					}
					break;
				case FOLDER_SESSIONS:
					//Get the path of the folder
					String sessionsFolderPath = dir + "\\files\\" + inputLine[1].trim() + "\\";

					File sessionsFolder = new File(sessionsFolderPath);
					File[] sessions = sessionsFolder.listFiles();
					if(sessions.length == 0) {
						System.out.println("No sessions files here");
						configDTO.setSessionDataPresent(false);
						continue;
					}
					configDTO.setSessionDataFolderName(inputLine[1].trim());

					for(int i = 0;i<sessions.length;i++) {
						if(sessions[i].isFile()) {
							System.out.println("File: " + sessions[i].getName());
							configDTO.addSessionPathName(sessions[i].getName());
						}
					}
					break;
				case SORT_BY:
					if(inputLine.length == 1) {
						continue;
					}
					String[] sortBy = inputLine[1].split(this.splitDataBy);
					String firstPartSortBy = null;
					String secondPartSortBy = null;
					for(String toSortBy: sortBy) {
						if(toSortBy.contains("\"")) {
							//First we remove the "
							toSortBy = toSortBy.replace("\"", "");

							//Then we save it to the correct part
							if(firstPartSortBy == null) {
								while(toSortBy.startsWith(" ")) {
									toSortBy = toSortBy.replaceFirst(" ","");

								}
								firstPartSortBy = toSortBy;
							}
							else {
								secondPartSortBy = toSortBy;
								this.configDTO.addSortBy(firstPartSortBy + "," + secondPartSortBy);
							}
							continue;

						}
						while(toSortBy.startsWith(" ")) {
							toSortBy = toSortBy.replaceFirst(" ","");

						}
						this.configDTO.addSortBy(toSortBy);
					}
					break;
				default:

					//It's impossible to reach this state unless 
					throw new IncorrectConfigFileException(line);
				}

			}




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


	public void resetConfigFile() {

	}






	public ConfigDTO getConfigDTO() {
		return configDTO;
	}










}
