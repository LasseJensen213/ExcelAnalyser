package dataTransferObjects;

import java.util.ArrayList;

public class DataModificationDTO {

	
	ArrayList<ColumnCalculationDTO> columnCalculationList = new ArrayList<ColumnCalculationDTO>();
	ArrayList<RenameVariableDTO> renameVariableList = new ArrayList<RenameVariableDTO>();
	ArrayList<SumAndDeleteDTO> sumAndDeleteList = new ArrayList<SumAndDeleteDTO>();
	
	
	
	
	public ArrayList<ColumnCalculationDTO> getColumnCalculationList() {
		return columnCalculationList;
	}
	public void setColumnCalculationList(ArrayList<ColumnCalculationDTO> columnCalculationList) {
		this.columnCalculationList = columnCalculationList;
	}
	public ArrayList<RenameVariableDTO> getRenameVariableList() {
		return renameVariableList;
	}
	public void setRenameVariableList(ArrayList<RenameVariableDTO> renameVariableList) {
		this.renameVariableList = renameVariableList;
	}
	public ArrayList<SumAndDeleteDTO> getSumAndDeleteList() {
		return sumAndDeleteList;
	}
	public void setSumAndDeleteList(ArrayList<SumAndDeleteDTO> sumAndDeleteList) {
		this.sumAndDeleteList = sumAndDeleteList;
	}
	
	
	
	@Override
	public String toString() {
		return "DataModificationDTO [columnCalculationList=" + columnCalculationList + ", renameVariableList="
				+ renameVariableList + ", sumAndDeleteList=" + sumAndDeleteList + "]";
	}	


	
	
	
	
	

}
