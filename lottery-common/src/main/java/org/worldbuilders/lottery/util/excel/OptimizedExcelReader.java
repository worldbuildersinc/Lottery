package org.worldbuilders.lottery.util.excel;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * Created by brendondugan on 6/22/17.
 */
@Slf4j
public class OptimizedExcelReader {
	private Map<Integer, String> headers;
	private List<OptimizedExcelRow> rows;

	public OptimizedExcelReader(File inputFile) throws IOException, InvalidFormatException {
		headers = new HashMap<>();

		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		rows = new ArrayList<>(worksheet.getLastRowNum());
		Iterator<Row> iterator = worksheet.iterator();
		if(iterator.hasNext()){
			Row headerRow = iterator.next();
			mapHeaders(headerRow);
			while (iterator.hasNext()){
				Row row = iterator.next();
				rows.add(new OptimizedExcelRow(mapRow(row)));
			}
		}
		workbook.close();
	}

	public Map<Integer, String> getHeaders() {
		return headers;
	}

	public List<OptimizedExcelRow> getRows() {
		return rows;
	}

	private Map<String,Object> mapRow(Row row) {
		Map<String, Object> rowMap = new HashMap<>();

		for(Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
			Cell cell = cellIterator.next();

			switch (cell.getCellTypeEnum()) {
				case STRING:
					rowMap.put(headers.get(cell.getColumnIndex()), cell.getStringCellValue());
					break;
				case NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						rowMap.put(headers.get(cell.getColumnIndex()), cell.getDateCellValue());
					} else {
						rowMap.put(headers.get(cell.getColumnIndex()), cell.getNumericCellValue());
					}
					break;
				default:
					log.trace("Skipping cell");
			}
		}
		return rowMap;
	}

	private void mapHeaders(Row headerRow){
		for(Iterator<Cell> cellIterator = headerRow.cellIterator(); cellIterator.hasNext();){
			Cell cell = cellIterator.next();
			headers.put(cell.getColumnIndex(), cell.getStringCellValue());
		}
	}
}