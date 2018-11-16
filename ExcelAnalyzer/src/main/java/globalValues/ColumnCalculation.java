package globalValues;

public class ColumnCalculation {
	
	private String column1Name = null;
	private String column2Name = null;
	private String operator = null;
	private String columnHeaderName = null;
	
	
	
	public ColumnCalculation(String column1Name, String column2Name, String operator) {
		super();
		this.column1Name = column1Name;
		this.column2Name = column2Name;
		this.operator = operator;
	}



	public String getColumn1Name() {
		return column1Name;
	}



	public void setColumn1Name(String column1Name) {
		this.column1Name = column1Name;
	}



	public String getColumn2Name() {
		return column2Name;
	}



	public void setColumn2Name(String column2Name) {
		this.column2Name = column2Name;
	}



	public String getOperator() {
		return operator;
	}



	public void setOperator(String operator) {
		this.operator = operator;
	}



	public String getColumnHeaderName() {
		return columnHeaderName;
	}



	public void setColumnHeaderName(String columnHeaderName) {
		this.columnHeaderName = columnHeaderName;
	}
	
	
	
	
	
	

}
