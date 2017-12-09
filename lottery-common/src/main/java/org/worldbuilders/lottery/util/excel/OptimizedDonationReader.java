package org.worldbuilders.lottery.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.headermapping.DonationHeaderMapping;

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
						"Campaign",
						"Received On",
						"Combined donation amount",
						"Eligible amount",
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
				)
		);
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
		if(row.stringValueExistsForHeader(headerMapping.getCampaignHeader())){
			donationEntry.setCampaign(row.getStringValue(headerMapping.getCampaignHeader()));
		}
		if(row.stringValueExistsForHeader(headerMapping.getReceivedDateHeader())){
			donationEntry.setReceivedDate(row.getStringValue(headerMapping.getReceivedDateHeader()));
		}
		donationEntry.setCombinedDonationAmount(row.getDoubleValue(headerMapping.getCombinedDonationAmountHeader()));
		donationEntry.setEligibleAmount(row.getIntegerValue(headerMapping.getEligibleAmountHeader()));
		donationEntry.setNumberOfTickets(row.getIntegerValue(headerMapping.getNumberOfTicketsHeader()));
		donationEntry.setEmailAddress(row.getStringValue(headerMapping.getEmailAddressHeader()));
		if (row.stringValueExistsForHeader(headerMapping.getShippingNameHeader())) {
			donationEntry.setShippingName(row.getStringValue(headerMapping.getShippingNameHeader()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingAddress1Header())) {
			donationEntry.setShippingAddress1(row.getStringValue(headerMapping.getShippingAddress1Header()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingAddress2Header())) {
			donationEntry.setShippingAddress2(row.getStringValue(headerMapping.getShippingAddress2Header()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingCityHeader())) {
			donationEntry.setShippingCity(row.getStringValue(headerMapping.getShippingCityHeader()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingStateHeader())) {
			donationEntry.setShippingState(row.getStringValue(headerMapping.getShippingStateHeader()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingPostCodeHeader())) {
			donationEntry.setShippingPostCode(row.getStringValue(headerMapping.getShippingPostCodeHeader()));
		}

		if (row.stringValueExistsForHeader(headerMapping.getShippingCountryHeader())) {
			donationEntry.setShippingCountry(row.getStringValue(headerMapping.getShippingCountryHeader()));
		}

		donationEntry.setJocoPref(row.stringValueExistsForHeader(headerMapping.getJocoPrefHeader()));
		donationEntry.setBooksPref(row.stringValueExistsForHeader(headerMapping.getBooksPrefHeader()));
		donationEntry.setGamesPref(row.stringValueExistsForHeader(headerMapping.getGamesPrefHeader()));
		donationEntry.setComicsPref(row.stringValueExistsForHeader(headerMapping.getComicsPrefHeader()));
		donationEntry.setJewelryPref(row.stringValueExistsForHeader(headerMapping.getJewelryPrefHeader()));
		return donationEntry;
	}
}
