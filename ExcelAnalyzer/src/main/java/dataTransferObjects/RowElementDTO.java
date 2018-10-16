package dataTransferObjects;

public class RowElementDTO {
	
	private String name;
	private int users;
	private int quantity;
	
	
	/**
	 * Constructor for RowElementDTO
	 * @param name - String
	 * @param users - int
	 * @param quantity - int
	 */
	
	public RowElementDTO(String name, int users, int quantity) {
		super();
		this.name = name;
		this.users = users;
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public int getUsers() {
		return users;
	}
	public int getQuantity() {
		return quantity;
	}
	
	
	
	
}
