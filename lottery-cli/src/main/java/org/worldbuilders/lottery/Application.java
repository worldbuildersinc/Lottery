package org.worldbuilders.lottery;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;
import org.worldbuilders.lottery.util.DonationReader;
import org.worldbuilders.lottery.util.PrizeReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by brendondugan on 4/12/17.
 */
@Log4j2
public class Application {
	public static void main(String[] args) {
		ClassLoader classLoader = Application.class.getClassLoader();
		File donationsFile = new File(classLoader.getResource("donations.xlsx").getFile());
		File prizesFile = new File(classLoader.getResource("prizes.xlsx").getFile());
		try {
			DonationReader donationsReader = new DonationReader(donationsFile);
			PrizeReader prizesReader = new PrizeReader(prizesFile);
			donationsReader.process();
			prizesReader.process();
			List<DonationEntry> donationEntries = donationsReader.getEntries();
			List<PrizeEntry> prizeEntries = prizesReader.getEntries();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidFormatException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
