package org.worldbuilders.lottery.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by brendondugan on 6/23/17.
 */
@Slf4j
public class OptimizedPrizeMapper {

	private static List<Prize> map(PrizeEntry entry) {
		ArrayList<Prize> prizes = new ArrayList<>();
		for (int i = 0; i < entry.getQuantity(); i++) {
			prizes.add(mapSingle(entry));
		}
		return prizes;
	}

	private static Prize mapSingle(PrizeEntry entry) {
		Prize prize = new Prize();
		prize.setId(UUID.randomUUID());
		prize.setName(entry.getName());
		prize.setType(entry.getType());
		prize.setAllowDuplicates(entry.isAllowDuplicates());
		return prize;
	}

	public static List<Prize> mapAll(List<PrizeEntry> entries) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<List<Prize>>> futures = new ArrayList<>(entries.size());
		ArrayList<Prize> prizes = new ArrayList<>(entries.size()*2);
		for (PrizeEntry entry : entries) {
			futures.add(executorService.submit(() -> map(entry)));
		}
		executorService.shutdown();
		while (!futures.isEmpty()){
			for(int j = 0; j < futures.size(); j++){
				Future<List<Prize>> f = futures.get(j);
				if(f.isDone()){
					try {
						List<Prize> entry = f.get();
						prizes.addAll(entry);
						futures.remove(f);
					} catch (InterruptedException | ExecutionException e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		return prizes;
	}
}
