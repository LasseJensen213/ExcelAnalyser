package dataTransferObjects;

public class SumAndDeleteDTO {

	private String variableToKeep = null;
	private String variableToDelete = null;

	public SumAndDeleteDTO(String variableToKeep, String variableToDelete) {
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

	@Override
	public String toString() {
		return "SumAndDelete [variableToKeep=" + variableToKeep + ", variableToDelete=" + variableToDelete + "]";
	}

}
