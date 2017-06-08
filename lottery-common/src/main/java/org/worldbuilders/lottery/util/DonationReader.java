package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
	private int numberOfTicketsHeaderIndex;
	private int emailAddressHeaderIndex;
	private int shippingNameHeaderIndex;
	private int shippingAddress1HeaderIndex;
	private int shippingAddress2HeaderIndex;
	private int shippingCityHeaderIndex;
	private int shippingStateHeaderIndex;
	private int shippingPostCodeHeaderIndex;
	private int shippingCountryHeaderIndex;
	private int jocoPrefHeaderIndex;
	private int booksPrefHeaderIndex;
	private int gamesPrefHeaderIndex;
	private int comicsPrefHeaderIndex;
	private int jewelryPrefHeaderIndex;


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
						"JoCo",
						"Books",
						"Games",
						"Comics/graphicnovels",
						"Jewelry"
				)
		);
	}

	public DonationReader(File inputFile, DonationHeaderMapping headerMapping) throws IOException, InvalidFormatException {
		super(inputFile);
		this.entries = new ArrayList<DonationEntry>();
		this.headerMapping = headerMapping;
	}

	protected void readHeaderRow(Row headerRow) {

	}

	protected <T extends ExcelEntry> void addEntry(T entry) throws IllegalArgumentException {
		if (entry == null || entry.getClass() != DonationEntry.class) {
			throw new IllegalArgumentException();
		}
		entries.add((DonationEntry) entry);
	}

	public List<DonationEntry> getEntries() {
		return entries;
	}

	protected DonationEntry parseRow(Row row) {
		return new DonationEntry();
	}
}
