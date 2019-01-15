package dataTransferObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SumAndDeleteLabelDTO {
	private String newVariableName = null;
	private String newName = null;
	private ArrayList<String> excludeSuffixes = null;

	public SumAndDeleteLabelDTO(String originalName, String newName, ArrayList<String> excludeSuffixes) {
		super();
		this.newVariableName = originalName;
		this.newName = newName;
		this.excludeSuffixes = excludeSuffixes;
		if (this.excludeSuffixes != null) {
			Collections.sort(excludeSuffixes, new SumAndDeleteLabelComparator());
			System.out.println(this.excludeSuffixes);
		}
	}

	public String getNewVariableName() {
		return newVariableName;
	}

	public void setOriginalName(String originalName) {
		this.newVariableName = originalName;
	}

	public String getStartWithString() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public ArrayList<String> getExcludeSuffixes() {
		return excludeSuffixes;
	}

	public void setExcludeSuffixes(ArrayList<String> excludeSuffixes) {
		this.excludeSuffixes = excludeSuffixes;
	}

	class SumAndDeleteLabelComparator implements Comparator<String> {

		public int compare(String o1, String o2) {
			return o2.length() - o1.length();
		}

	}

}
