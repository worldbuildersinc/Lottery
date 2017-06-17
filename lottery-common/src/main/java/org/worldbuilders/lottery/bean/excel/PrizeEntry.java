package org.worldbuilders.lottery.bean.excel;

import lombok.Data;

/**
 * Created by brendondugan on 4/14/17.
 */
@Data
public class PrizeEntry extends ExcelEntry {
	private String name;
	private int quantity;
	private String type;

}
