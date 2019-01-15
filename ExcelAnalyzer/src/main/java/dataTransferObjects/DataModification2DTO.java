package dataTransferObjects;

import java.util.ArrayList;

public class DataModification2DTO {

	ArrayList<SumAndDeleteLabelDTO> renameVariableList = new ArrayList<SumAndDeleteLabelDTO>();
	ArrayList<ThresholdDTO> columnCalculationList = new ArrayList<ThresholdDTO>();

	public DataModification2DTO() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<ThresholdDTO> getColumnCalculationList() {
		return columnCalculationList;
	}

	public void setColumnCalculationList(ArrayList<ThresholdDTO> columnCalculationList) {
		this.columnCalculationList = columnCalculationList;
	}

	public ArrayList<SumAndDeleteLabelDTO> getSumAndDeleteLabelsList() {
		return renameVariableList;
	}

	public void setRenameVariableList(ArrayList<SumAndDeleteLabelDTO> renameVariableList) {
		this.renameVariableList = renameVariableList;
	}

	@Override
	public String toString() {
		return "DataModification2DTO [columnCalculationList=" + columnCalculationList + ", renameVariableList="
				+ renameVariableList + "]";
	}

}
