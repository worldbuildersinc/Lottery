package org.worldbuilders.lottery.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.headermapping.DonationHeaderMapping;
import org.worldbuilders.lottery.util.excel.OptimizedExcelReader;
import org.worldbuilders.lottery.util.excel.OptimizedExcelRow;

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
public class OptimizedDonationReader {
	private OptimizedExcelReader excelReader;
	private List<DonationEntry> entries;
	private DonationHeaderMapping headerMapping;

	public OptimizedDonationReader(File inputFile) throws IOException, InvalidFormatException {
		this(
				inputFile,
				new DonationHeaderMapping(
				"# tickets",
				"Email Address",
				"Shipping Name",
				"Shipping Address 1",
				"Shipping Address 2",
				"Shipping City",
				"Shipping State",
				"Shipping Postal-Code",
				"Shipping Country",
				"JoCoPref",
				"BooksPref",
				"GamesPref",
				"ComicsPref",
				"JewelryPref"
		));
	}

	public OptimizedDonationReader(File inputFile, DonationHeaderMapping donationHeaderMapping) throws IOException, InvalidFormatException {
		this.headerMapping = donationHeaderMapping;
		this.excelReader = new OptimizedExcelReader(inputFile);
		this.entries = new ArrayList<>(excelReader.getRows().size());
	}

	public List<DonationEntry> getEntries() {
		return entries;
	}

	public void process(){
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<DonationEntry>> futures = new ArrayList<>();
		List<OptimizedExcelRow> rows = excelReader.getRows();
		for(OptimizedExcelRow row : rows){
			futures.add(executorService.submit(() -> map(row)));
//			entries.add(map(row));
		}
		executorService.shutdown();
		while(!futures.isEmpty()){
			for(int j = 0; j < futures.size(); j++){
				Future<DonationEntry> f = futures.get(j);
				if(f.isDone()){
					try {
						DonationEntry entry = f.get();
						entries.add(entry);
						futures.remove(f);
					} catch (InterruptedException | ExecutionException e) {
						log.error(e.getMessage());
					}
				}
			}
		}
	}

	private DonationEntry map(OptimizedExcelRow row){
		DonationEntry donationEntry = new DonationEntry();
		donationEntry.setNumberOfTickets(row.getIntegerValue(headerMapping.getNumberOfTicketsHeader()));
		donationEntry.setEmailAddress(row.getStringValue(headerMapping.getEmailAddressHeader()));
		donationEntry.setShippingName(row.getStringValue(headerMapping.getShippingNameHeader()));
		donationEntry.setShippingAddress1(row.getStringValue(headerMapping.getShippingAddress1Header()));
		if (row.stringValueExistsForHeader(headerMapping.getShippingAddress2Header())) {
			donationEntry.setShippingAddress2(row.getStringValue(headerMapping.getShippingAddress2Header()));
		}
		donationEntry.setShippingCity(row.getStringValue(headerMapping.getShippingCityHeader()));
		donationEntry.setShippingState(row.getStringValue(headerMapping.getShippingStateHeader()));
		donationEntry.setShippingPostCode(row.getStringValue(headerMapping.getShippingPostCodeHeader()));
		donationEntry.setShippingCountry(row.getStringValue(headerMapping.getShippingCountryHeader()));
		donationEntry.setJocoPref(row.stringValueExistsForHeader(headerMapping.getJocoPrefHeader()));
		donationEntry.setBooksPref(row.stringValueExistsForHeader(headerMapping.getBooksPrefHeader()));
		donationEntry.setGamesPref(row.stringValueExistsForHeader(headerMapping.getGamesPrefHeader()));
		donationEntry.setComicsPref(row.stringValueExistsForHeader(headerMapping.getComicsPrefHeader()));
		donationEntry.setJewelryPref(row.stringValueExistsForHeader(headerMapping.getJewelryPrefHeader()));
		return donationEntry;
	}
}
