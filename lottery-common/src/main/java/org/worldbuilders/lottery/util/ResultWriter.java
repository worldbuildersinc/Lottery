package org.worldbuilders.lottery.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.RaffleTicket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by brendondugan on 6/19/17.
 */
@Slf4j
public class ResultWriter {
	private File destination;
	private XSSFWorkbook workbook;
	private CreationHelper creationHelper;
	private Sheet worksheet;

	public ResultWriter(File destination) {
		this.destination = destination;
		this.initialize();
	}

	private void initialize() {
		workbook = new XSSFWorkbook();
		creationHelper = workbook.getCreationHelper();
		worksheet = workbook.createSheet("Lottery Winners");
		Row row = worksheet.createRow(0);
		row.createCell(0).setCellValue("Ticket ID");
		row.createCell(1).setCellValue("Email Address");
		row.createCell(2).setCellValue("Shipping Name");
		row.createCell(3).setCellValue("Shipping Address 1");
		row.createCell(4).setCellValue("Shipping Address 2");
		row.createCell(5).setCellValue("Shipping City");
		row.createCell(6).setCellValue("Shipping State");
		row.createCell(7).setCellValue("Shipping Post Code");
		row.createCell(8).setCellValue("Shipping Country");
		row.createCell(9).setCellValue("Prize ID");
		row.createCell(10).setCellValue("Prize Name");

	}

	public void processWinners(Map<Prize, RaffleTicket> winners) throws IOException {
		winners.forEach(new BiConsumer<Prize, RaffleTicket>() {
			private int rowIndex = 1;

			@Override
			public void accept(Prize prize, RaffleTicket raffleTicket) {
				log.debug("Writing Winner");
				Row row = worksheet.createRow(rowIndex);
				row.createCell(0).setCellValue(raffleTicket.getId().toString());
				row.createCell(1).setCellValue(raffleTicket.getEmailAddress());
				row.createCell(2).setCellValue(raffleTicket.getShippingName());
				row.createCell(3).setCellValue(raffleTicket.getShippingAddress1());
				row.createCell(4).setCellValue(raffleTicket.getShippingAddress2());
				row.createCell(5).setCellValue(raffleTicket.getShippingCity());
				row.createCell(6).setCellValue(raffleTicket.getShippingState());
				row.createCell(7).setCellValue(raffleTicket.getShippingPostCode());
				row.createCell(8).setCellValue(raffleTicket.getShippingCountry());
				row.createCell(9).setCellValue(prize.getId().toString());
				row.createCell(10).setCellValue(prize.getName());
				rowIndex++;
			}
		});
		for (int i = 0; i < 11; i++) {
			worksheet.autoSizeColumn(i);
		}
		this.save();
	}

	private void save() throws IOException {
		if (!destination.exists()) {
			destination.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(destination);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
	}
}
