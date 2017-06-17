package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.ExcelEntry;
import org.worldbuilders.lottery.bean.excel.headermapping.DonationHeaderMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendondugan on 4/12/17.
 */
@Slf4j
public class DonationReader extends ExcelReader {
	private List<DonationEntry> entries;
	private DonationHeaderMapping headerMapping;
	private int numberOfTicketsIndex;
	private int emailAddressIndex;
	private int shippingNameIndex;
	private int shippingAddress1Index;
	private int shippingAddress2Index;
	private int shippingCityIndex;
	private int shippingStateIndex;
	private int shippingPostCodeIndex;
	private int shippingCountryIndex;
	private int jocoPrefIndex;
	private int booksPrefIndex;
	private int gamesPrefIndex;
	private int comicsPrefIndex;
	private int jewelryPrefIndex;


	public DonationReader(File inputFile) throws IOException, InvalidFormatException {
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
				)
		);
	}

	public DonationReader(File inputFile, DonationHeaderMapping headerMapping) throws IOException, InvalidFormatException {
		super(inputFile);
		this.entries = new ArrayList<DonationEntry>();
		this.headerMapping = headerMapping;
	}

	protected void readHeaderRow(Row headerRow) {
		log.trace("Processing the header row of the Prize spreadsheet");
		for (Cell cell : headerRow) {
			String cellValue = cell.getStringCellValue();
			int columnIndex = cell.getColumnIndex();
			if (headerMapping.getNumberOfTicketsHeader().equals(cellValue)) {
				numberOfTicketsIndex = columnIndex;
			} else if (headerMapping.getEmailAddressHeader().equals(cellValue)) {
				emailAddressIndex = columnIndex;
			} else if (headerMapping.getShippingNameHeader().equals(cellValue)) {
				shippingNameIndex = columnIndex;
			} else if (headerMapping.getShippingAddress1Header().equals(cellValue)) {
				shippingAddress1Index = columnIndex;
			} else if (headerMapping.getShippingAddress2Header().equals(cellValue)) {
				shippingAddress2Index = columnIndex;
			} else if (headerMapping.getShippingCityHeader().equals(cellValue)) {
				shippingCityIndex = columnIndex;
			} else if (headerMapping.getShippingStateHeader().equals(cellValue)) {
				shippingStateIndex = columnIndex;
			} else if (headerMapping.getShippingPostCodeHeader().equals(cellValue)) {
				shippingPostCodeIndex = columnIndex;
			} else if (headerMapping.getShippingCountryHeader().equals(cellValue)) {
				shippingCountryIndex = columnIndex;
			} else if (headerMapping.getJocoPrefHeader().equals(cellValue)) {
				jocoPrefIndex = columnIndex;
			} else if (headerMapping.getBooksPrefHeader().equals(cellValue)) {
				booksPrefIndex = columnIndex;
			} else if (headerMapping.getGamesPrefHeader().equals(cellValue)) {
				gamesPrefIndex = columnIndex;
			} else if (headerMapping.getComicsPrefHeader().equals(cellValue)) {
				comicsPrefIndex = columnIndex;
			} else if (headerMapping.getJewelryPrefHeader().equals(cellValue)) {
				jewelryPrefIndex = columnIndex;
			} else {
				log.trace("Found ignored column header '{}' at index {}", cellValue, columnIndex);
			}
		}
		log.info("Header Parsing complete");
	}

	protected <T extends ExcelEntry> void addEntry(T entry) throws IllegalArgumentException {
		if (entry == null || entry.getClass() != DonationEntry.class) {
			throw new IllegalArgumentException();
		}
		log.trace("Donation found: {}", entry.toString());
		entries.add((DonationEntry) entry);
	}

	public List<DonationEntry> getEntries() {
		return entries;
	}

	protected DonationEntry parseRow(Row row) {
		DonationEntry donationEntry = new DonationEntry();
		donationEntry.setRowNumber(row.getRowNum());
		donationEntry.setNumberOfTickets((int) row.getCell(numberOfTicketsIndex).getNumericCellValue());
		donationEntry.setEmailAddress(row.getCell(emailAddressIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingName(row.getCell(shippingNameIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingAddress1(row.getCell(shippingAddress1Index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingAddress2(row.getCell(shippingAddress2Index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingCity(row.getCell(shippingCityIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingState(row.getCell(shippingStateIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingPostCode(row.getCell(shippingPostCodeIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setShippingCountry(row.getCell(shippingCountryIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
		donationEntry.setJocoPref(!StringUtils.isEmpty(row.getCell(jocoPrefIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
		donationEntry.setBooksPref(!StringUtils.isEmpty(row.getCell(booksPrefIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
		donationEntry.setGamesPref(!StringUtils.isEmpty(row.getCell(gamesPrefIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
		donationEntry.setComicsPref(!StringUtils.isEmpty(row.getCell(comicsPrefIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
		donationEntry.setJewelryPref(!StringUtils.isEmpty(row.getCell(jewelryPrefIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()));
		return donationEntry;
	}
}
