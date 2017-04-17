package org.worldbuilders.lottery.bean.excel.headermapping;

/**
 * Created by brendondugan on 4/16/17.
 */
public class PrizeHeaderMapping {
	private String nameHeader;
	private String quantityHeader;
	private String typeHeader;

	public PrizeHeaderMapping(String nameHeader, String quantityHeader, String typeHeader) {
		this.nameHeader = nameHeader;
		this.quantityHeader = quantityHeader;
		this.typeHeader = typeHeader;
	}

	public String getNameHeader() {
		return nameHeader;
	}

	public void setNameHeader(String nameHeader) {
		this.nameHeader = nameHeader;
	}

	public String getQuantityHeader() {
		return quantityHeader;
	}

	public void setQuantityHeader(String quantityHeader) {
		this.quantityHeader = quantityHeader;
	}

	public String getTypeHeader() {
		return typeHeader;
	}

	public void setTypeHeader(String typeHeader) {
		this.typeHeader = typeHeader;
	}
}
