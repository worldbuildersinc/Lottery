package org.worldbuilders.lottery.util;

import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by brendondugan on 6/13/17.
 */
@Deprecated
public class PrizeMapper {
	@Deprecated
	public static List<Prize> map(PrizeEntry entry) {
		ArrayList<Prize> prizes = new ArrayList<>();
		for (int i = 0; i < entry.getQuantity(); i++) {
			prizes.add(mapSingle(entry));
		}
		return prizes;
	}

	@Deprecated
	public static Prize mapSingle(PrizeEntry entry) {
		Prize prize = new Prize();
		prize.setId(UUID.randomUUID());
		prize.setName(entry.getName());
		prize.setType(entry.getType());
		return prize;
	}

	@Deprecated
	public static List<Prize> mapAll(List<PrizeEntry> entries) {
		ArrayList<Prize> prizes = new ArrayList<>();
		for (PrizeEntry entry : entries) {
			prizes.addAll(map(entry));
		}
		return prizes;
	}
}
