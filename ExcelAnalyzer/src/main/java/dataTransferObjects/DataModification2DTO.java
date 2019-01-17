package dataTransferObjects;

import java.util.ArrayList;

public class DataModification2DTO {

	ArrayList<SumAndDeleteCategoryDTO> renameCategoryList = new ArrayList<SumAndDeleteCategoryDTO>();
	ArrayList<SumAndDeleteLabelDTO> renameLabelList = new ArrayList<SumAndDeleteLabelDTO>();

	public DataModification2DTO() {
		// TODO Auto-generated constructor stub
	}


	public ArrayList<SumAndDeleteCategoryDTO> getRenameCategoryList() {
		return renameCategoryList;
	}


	public void setRenameCategoryList(ArrayList<SumAndDeleteCategoryDTO> renameCategoryList) {
		this.renameCategoryList = renameCategoryList;
	}


	public ArrayList<SumAndDeleteLabelDTO> getRenameLabelList() {
		return renameLabelList;
	}


	public void setRenameLabelList(ArrayList<SumAndDeleteLabelDTO> renameLabelList) {
		this.renameLabelList = renameLabelList;
	}


	@Override
	public String toString() {
		return "DataModification2DTO [columnCalculationList=" + renameLabelList + ", renameVariableList="
				+ renameCategoryList + "]";
	}

}
