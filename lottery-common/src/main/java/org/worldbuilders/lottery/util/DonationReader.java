package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.ExcelEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendondugan on 4/12/17.
 */
@Slf4j
public class DonationReader extends ExcelReader {
	List<DonationEntry> entries;

	public DonationReader(File inputFile) throws IOException, InvalidFormatException {
		super(inputFile);
		this.entries = new ArrayList<DonationEntry>();
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
