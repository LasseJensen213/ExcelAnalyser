package dataTransferObjects;

import java.util.ArrayList;
import java.util.List;

public class ConfigDTO {

	
	//Folder names
	private String generalDataFolderName;
	private String userDataFolderName;
	private String sessionDataFolderName;

	
	//Paths of files in folder
	private List<String> generalPathNames;
	private List<String> sessionPathName;
	private List<String> usersPathNames;

	//Categories to create a sheet.
	private List<String> sortBy;

	
	//Act on whether or whether not there's data present in the given folder. 
	private boolean generalDataPresent = true;
	private boolean sessionDataPresent = true;
	private boolean userDataPresent = true;


	public ConfigDTO() {
		super();
		generalPathNames = new ArrayList<String>();
		sessionPathName = new ArrayList<String>();
		usersPathNames = new ArrayList<String>();

		sortBy = new ArrayList<String>();

	}




	/**
	 * Add new pathname to the arrayList
	 * @param pathName
	 */
	public void addGeneralPathName(String pathName) {
		this.generalPathNames.add(pathName);
	}
	/**
	 * Add new pathname to the arrayList
	 * @param pathName
	 */
	public void addSessionPathName(String pathName) {
		this.sessionPathName.add(pathName);
	}/**
	 * Add new pathname to the arrayList
	 * @param pathName
	 */
	public void addUsersPathName(String pathName) {
		this.usersPathNames.add(pathName);
	}
	public List<String> getGeneralPathNames() {
		return generalPathNames;
	}
	public List<String> getSessionPathName() {
		return sessionPathName;
	}
	public List<String> getUsersPathNames() {
		return usersPathNames;
	}




	/**
	 * Add new sortBy to the arrayList
	 * @param sortBy
	 */
	public void addSortBy(String sortBy) {
		this.sortBy.add(sortBy);
	}
	public List<String> getSortBy() {
		return this.sortBy;
	}




	public boolean isGeneralDataPresent() {
		return generalDataPresent;
	}
	public void setGeneralDataPresent(boolean generalDataPresent) {
		this.generalDataPresent = generalDataPresent;
	}
	public boolean isSessionDataPresent() {
		return sessionDataPresent;
	}
	public void setSessionDataPresent(boolean sessionDataPresent) {
		this.sessionDataPresent = sessionDataPresent;
	}
	public boolean isUserDataPresent() {
		return userDataPresent;
	}
	public void setUserDataPresent(boolean userDataPresent) {
		this.userDataPresent = userDataPresent;
	}




	/**
	 * @return the generalDataFolderName
	 */
	public String getGeneralDataFolderName() {
		return generalDataFolderName;
	}




	/**
	 * @param generalDataFolderName the generalDataFolderName to set
	 */
	public void setGeneralDataFolderName(String generalDataFolderName) {
		this.generalDataFolderName = generalDataFolderName;
	}




	/**
	 * @return the userDataFolderName
	 */
	public String getUserDataFolderName() {
		return userDataFolderName;
	}




	/**
	 * @param userDataFolderName the userDataFolderName to set
	 */
	public void setUserDataFolderName(String userDataFolderName) {
		this.userDataFolderName = userDataFolderName;
	}




	/**
	 * @return the sessionDataFolderName
	 */
	public String getSessionDataFolderName() {
		return sessionDataFolderName;
	}




	/**
	 * @param sessionDataFolderName the sessionDataFolderName to set
	 */
	public void setSessionDataFolderName(String sessionDataFolderName) {
		this.sessionDataFolderName = sessionDataFolderName;
	}







}
