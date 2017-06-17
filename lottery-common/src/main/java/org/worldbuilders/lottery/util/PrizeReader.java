package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.worldbuilders.lottery.bean.excel.ExcelEntry;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;
import org.worldbuilders.lottery.bean.excel.headermapping.PrizeHeaderMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendondugan on 4/12/17.
 */
@Slf4j
public class PrizeReader extends ExcelReader {

	private int nameIndex;
	private int quantityIndex;
	private int typeIndex;
	private PrizeHeaderMapping headerMapping;
	private List<PrizeEntry> entries;

	public PrizeReader(File inputFile) throws IOException, InvalidFormatException {
		this(inputFile, new PrizeHeaderMapping("Prize Name", "Qty", "Type"));
	}

	public PrizeReader(File inputFile, PrizeHeaderMapping headerMapping) throws IOException, InvalidFormatException {
		super(inputFile);
		this.headerMapping = headerMapping;
		this.entries = new ArrayList<PrizeEntry>();
	}

	public void setHeaderMapping(PrizeHeaderMapping headerMapping) {
		this.headerMapping = headerMapping;
	}

	protected void readHeaderRow(Row headerRow) {
		log.trace("Processing the header row of the Prize spreadsheet");
		for (Cell cell : headerRow) {
			String cellValue = cell.getStringCellValue();
			int columnIndex = cell.getColumnIndex();
			if (headerMapping.getNameHeader().equals(cellValue)) {
				nameIndex = columnIndex;
			} else if (headerMapping.getQuantityHeader().equals(cellValue)) {
				quantityIndex = columnIndex;
			} else if (headerMapping.getTypeHeader().equals(cellValue)) {
				typeIndex = columnIndex;
			} else {
				log.trace("Found ignored column header '{}' at index {}", cellValue, columnIndex);
			}
		}
	}

	protected <T extends ExcelEntry> void addEntry(T entry) throws IllegalArgumentException {
		if (entry == null || entry.getClass() != PrizeEntry.class) {
			throw new IllegalArgumentException();
		}
		log.trace("Prize found: {}", entry.toString());
		entries.add((PrizeEntry) entry);

	}

	public List<PrizeEntry> getEntries() {
		return entries;
	}


	protected PrizeEntry parseRow(Row row) {
		PrizeEntry prizeEntry = new PrizeEntry();
		prizeEntry.setRowNumber(row.getRowNum());
		prizeEntry.setName(row.getCell(nameIndex).getStringCellValue());
		prizeEntry.setQuantity((int) row.getCell(quantityIndex).getNumericCellValue());
		prizeEntry.setType(row.getCell(typeIndex).getStringCellValue());
		return prizeEntry;
	}
}
