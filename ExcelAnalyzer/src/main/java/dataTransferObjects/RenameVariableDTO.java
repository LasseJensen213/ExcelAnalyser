package dataTransferObjects;

public class RenameVariableDTO {
	
	private String originalName = null;
	private String newName = null;
	
	
	
	
	
	public RenameVariableDTO(String originalName, String newName) {
		super();
		this.originalName = originalName;
		this.newName = newName;
	}
	
	
	
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}



	@Override
	public String toString() {
		return "RenameVariable [originalName=" + originalName + ", newName=" + newName + "]";
	}
	
	
	
	
}
