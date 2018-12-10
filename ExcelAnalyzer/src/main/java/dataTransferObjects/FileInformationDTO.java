package dataTransferObjects;

public class FileInformationDTO {
	private String name;
	private String folder;

	public FileInformationDTO(String name, String folder) {
		super();
		this.name = name;
		this.folder = folder;
	}

	public String getName() {
		return name;
	}

	public String getFolder() {
		return folder;
	}

}
