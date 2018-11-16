package globalValues;

public class SumAndDelete {
	
	
	private String variableToKeep = null;
	private String variableToDelete = null;
	
	
	
	
	
	public SumAndDelete(String variableToKeep, String variableToDelete) {
		super();
		this.variableToKeep = variableToKeep;
		this.variableToDelete = variableToDelete;
	}
	
	
	
	
	
	public String getVariableToKeep() {
		return variableToKeep;
	}
	public void setVariableToKeep(String variableToKeep) {
		this.variableToKeep = variableToKeep;
	}
	public String getVariableToDelete() {
		return variableToDelete;
	}
	public void setVariableToDelete(String variableToDelete) {
		this.variableToDelete = variableToDelete;
	}
	

}
