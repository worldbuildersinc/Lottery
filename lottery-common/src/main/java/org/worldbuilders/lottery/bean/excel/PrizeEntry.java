package org.worldbuilders.lottery.bean.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by brendondugan on 4/14/17.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrizeEntry extends ExcelEntry {
	private String name;
	private int quantity;
	private String type;

}
