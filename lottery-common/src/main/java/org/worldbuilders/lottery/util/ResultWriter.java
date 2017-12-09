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
	private final File destination;
	private XSSFWorkbook workbook;
	private Sheet worksheet;

	public ResultWriter(File destination) {
		this.destination = destination;
		this.initialize();
	}

	private void initialize() {
		workbook = new XSSFWorkbook();
		CreationHelper creationHelper = workbook.getCreationHelper();
		worksheet = workbook.createSheet("Lottery Winners");
		Row row = worksheet.createRow(0);
		row.createCell(0).setCellValue("Ticket ID");
		row.createCell(1).setCellValue("Campaign");
		row.createCell(2).setCellValue("Received Date");
		row.createCell(3).setCellValue("Combined Donation Amount");
		row.createCell(4).setCellValue("Eligible Amount");
		row.createCell(5).setCellValue("Number of Tickets");
		row.createCell(6).setCellValue("Email Address");
		row.createCell(7).setCellValue("Shipping Name");
		row.createCell(8).setCellValue("Shipping Address 1");
		row.createCell(9).setCellValue("Shipping Address 2");
		row.createCell(10).setCellValue("Shipping City");
		row.createCell(11).setCellValue("Shipping State");
		row.createCell(12).setCellValue("Shipping Post Code");
		row.createCell(13).setCellValue("Shipping Country");
		row.createCell(14).setCellValue("Prize ID");
		row.createCell(15).setCellValue("Prize Name");

	}

	public void processWinners(Map<Prize, RaffleTicket> winners) throws IOException {
		winners.forEach(new BiConsumer<Prize, RaffleTicket>() {
			private int rowIndex = 1;

			@Override
			public void accept(Prize prize, RaffleTicket raffleTicket) {
				Row row = worksheet.createRow(rowIndex);
				row.createCell(0).setCellValue(raffleTicket.getId().toString());
				row.createCell(1).setCellValue(raffleTicket.getCampaign());
				row.createCell(2).setCellValue(raffleTicket.getReceivedDate());
				row.createCell(3).setCellValue(raffleTicket.getCombinedDonationAmount());
				row.createCell(4).setCellValue(raffleTicket.getEligibleAmount());
				row.createCell(5).setCellValue(raffleTicket.getNumberOfTickets());
				row.createCell(6).setCellValue(raffleTicket.getEmailAddress());
				row.createCell(7).setCellValue(raffleTicket.getShippingName());
				row.createCell(8).setCellValue(raffleTicket.getShippingAddress1());
				row.createCell(9).setCellValue(raffleTicket.getShippingAddress2());
				row.createCell(10).setCellValue(raffleTicket.getShippingCity());
				row.createCell(11).setCellValue(raffleTicket.getShippingState());
				row.createCell(12).setCellValue(raffleTicket.getShippingPostCode());
				row.createCell(13).setCellValue(raffleTicket.getShippingCountry());
				row.createCell(14).setCellValue(prize.getId().toString());
				row.createCell(15).setCellValue(prize.getName());
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
