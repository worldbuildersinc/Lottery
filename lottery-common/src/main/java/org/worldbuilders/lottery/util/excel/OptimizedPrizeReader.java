package org.worldbuilders.lottery.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;
import org.worldbuilders.lottery.bean.excel.headermapping.PrizeHeaderMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by brendondugan on 6/22/17.
 */
@Slf4j
public class OptimizedPrizeReader {
	private List<PrizeEntry> entries;
	private PrizeHeaderMapping headerMapping;
	private OptimizedExcelReader excelReader;

	public OptimizedPrizeReader(File inputFile) throws IOException, InvalidFormatException {
		this(inputFile, new PrizeHeaderMapping("Prize Name", "Qty", "Type"));
	}

	public OptimizedPrizeReader(File inputFile, PrizeHeaderMapping headerMapping) throws IOException, InvalidFormatException {
		this.headerMapping = headerMapping;
		this.excelReader = new OptimizedExcelReader(inputFile);
		this.entries = new ArrayList<>(excelReader.getRows().size());
	}

	public List<PrizeEntry> getEntries() {
		return entries;
	}

	public void process(){
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<PrizeEntry>> futures = new ArrayList<>();
		List<OptimizedExcelRow> rows = excelReader.getRows();
		for(OptimizedExcelRow row : rows){
			futures.add(executorService.submit(() -> map(row)));
//			entries.add(map(row));
		}
		executorService.shutdown();
		while(!futures.isEmpty()){
			for(int j = 0; j < futures.size(); j++){
				Future<PrizeEntry> f = futures.get(j);
				if(f.isDone()){
					try {
						PrizeEntry entry = f.get();
						entries.add(entry);
						futures.remove(f);
					} catch (InterruptedException | ExecutionException e) {
						log.error(e.getMessage());
					}
				}
			}
		}
	}

	private PrizeEntry map(OptimizedExcelRow row){
		PrizeEntry prizeEntry = new PrizeEntry();
		prizeEntry.setName(row.getStringValue(headerMapping.getNameHeader()));
		prizeEntry.setType(row.getStringValue(headerMapping.getTypeHeader()));
		prizeEntry.setQuantity(row.getIntegerValue(headerMapping.getQuantityHeader()));
		return null;
	}
}