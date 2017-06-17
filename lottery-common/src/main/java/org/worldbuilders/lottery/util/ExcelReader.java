package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.worldbuilders.lottery.bean.excel.ExcelEntry;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by brendondugan on 4/12/17.
 */
@Slf4j
public abstract class ExcelReader {
	protected XSSFSheet worksheet;
	private File inputFile;

	ExcelReader(File inputFile) throws IOException, InvalidFormatException {
		this.inputFile = inputFile;
		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		this.worksheet = workbook.getSheetAt(0);
		log.trace("Successfully loaded {}", inputFile.getName());
	}

	public void process() {
		Iterator<Row> rowIterator = worksheet.iterator();
		log.trace("Begin Row iteration in process() method");
		if (rowIterator.hasNext()) {
			Row headerRow = rowIterator.next();
			log.trace("Reading Header Row in process() method");
			readHeaderRow(headerRow);
			while (rowIterator.hasNext()) {
				Row currentRow = rowIterator.next();

				log.trace("Adding row {} in process() method", currentRow.getRowNum());
				addEntry(parseRow(currentRow));
			}
		}
		log.trace("End Row iteration in process() method");
	}


	protected abstract void readHeaderRow(Row headerRow);

	protected abstract <T extends ExcelEntry> T parseRow(Row row);

	protected abstract <T extends ExcelEntry> void addEntry(T entry) throws IllegalArgumentException;

	public abstract List<? extends ExcelEntry> getEntries();
}
