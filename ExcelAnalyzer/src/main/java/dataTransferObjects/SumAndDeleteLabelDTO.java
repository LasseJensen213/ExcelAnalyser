package dataTransferObjects;

public class SumAndDeleteLabelDTO {
	private String originalName = null;
	private String newName = null;

	
	public SumAndDeleteLabelDTO(String originalName, String newName) {
		super();
		this.originalName = originalName;
		this.newName = newName;
	}


	public String getNewVariableName() {
		return originalName;
	}


	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}


	public String getStartWithString() {
		return newName;
	}


	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	
	

}
