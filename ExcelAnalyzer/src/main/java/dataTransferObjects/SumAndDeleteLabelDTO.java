package dataTransferObjects;

import java.util.ArrayList;

public class SumAndDeleteLabelDTO {

	private String eventCategory = null;
	private String eventLabel = null;
	private String replacementName = null;
	private ArrayList<String> excludeSuffixes = null;
	
	
	public SumAndDeleteLabelDTO(String eventCategory, String eventLabel, String replacementName,ArrayList<String> excludeSuffixes) {
		super();
		this.eventCategory = eventCategory;
		this.eventLabel = eventLabel;
		this.replacementName = replacementName;
		this.excludeSuffixes = excludeSuffixes;
	}


	public String getEventCategory() {
		return eventCategory;
	}


	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}


	public String getEventLabel() {
		return eventLabel;
	}


	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}


	public String getReplacementName() {
		return replacementName;
	}


	public void setReplacementName(String replacementName) {
		this.replacementName = replacementName;
	}


	public ArrayList<String> getExcludeSuffixes() {
		return excludeSuffixes;
	}


	public void setExcludeSuffixes(ArrayList<String> excludeSuffixes) {
		this.excludeSuffixes = excludeSuffixes;
	}


	@Override
	public String toString() {
		return "SumAndDeleteLabelDTO [eventCategory=" + eventCategory + ", eventLabel=" + eventLabel
				+ ", replacementName=" + replacementName + ", excludeSuffixes=" + excludeSuffixes + "]";
	}
	
	
	
	
}