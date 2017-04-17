package org.worldbuilders.lottery.bean.excel;

/**
 * Created by brendondugan on 4/14/17.
 */
public class PrizeEntry extends ExcelEntry {
	private String name;
	private int quantity;
	private String type;

	public PrizeEntry() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PrizeEntry{" +
				"rowNumber='" + rowNumber + '\'' +
				", name='" + name + '\'' +
				", quantity=" + quantity +
				", type='" + type + '\'' +
				"} ";
	}
}
