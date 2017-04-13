package org.worldbuilders.lottery.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

/**
 * Created by brendondugan on 4/12/17.
 */
public class ExcelReader {
	private File inputFile;
	protected XSSFSheet worksheet;

	public ExcelReader(File inputFile) throws IOException, InvalidFormatException {
		this.inputFile = inputFile;
		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		this.worksheet = workbook.getSheetAt(0);
	}
}
