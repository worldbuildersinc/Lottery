package org.worldbuilders.lottery.bean.excel.headermapping;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by brendondugan on 4/16/17.
 */
@Data
@AllArgsConstructor
public class PrizeHeaderMapping {
	private String nameHeader;
	private String quantityHeader;
	private String typeHeader;
	private String allowDuplicatesHeader;

}
