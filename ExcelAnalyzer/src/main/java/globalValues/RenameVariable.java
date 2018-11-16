package globalValues;

public class RenameVariable {
	
	private String originalName = null;
	private String newName = null;
	
	
	
	
	
	public RenameVariable(String originalName, String newName) {
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
	
	
	
	
}
