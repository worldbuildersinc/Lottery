package org.worldbuilders;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worldbuilders.lottery.Lottery;
import org.worldbuilders.lottery.bean.Prize;
import org.worldbuilders.lottery.bean.RaffleTicket;
import org.worldbuilders.lottery.bean.excel.DonationEntry;
import org.worldbuilders.lottery.bean.excel.PrizeEntry;
import org.worldbuilders.lottery.util.ResultWriter;
import org.worldbuilders.lottery.util.excel.OptimizedDonationMapper;
import org.worldbuilders.lottery.util.excel.OptimizedDonationReader;
import org.worldbuilders.lottery.util.excel.OptimizedPrizeMapper;
import org.worldbuilders.lottery.util.excel.OptimizedPrizeReader;
import org.worldbuilders.task.FileOperationTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@FXML
	private TextField donorField;
	@FXML
	private TextField prizeField;
	@FXML
	private ListView<String> consoleBox;
	@FXML
	private Button runButton;
	@FXML
	private ProgressBar progressBar;
	private ObservableList<String> logMessages;


	private List<RaffleTicket> raffleTickets;
	private List<Prize> prizes;
	private Map<Prize, RaffleTicket> winners;

	public void handleWindowShownEvent() {
		logMessages = FXCollections.observableArrayList();
		logInfoMessage(String.format("Ready to Start at %s", new Date().toString()));
		consoleBox.setItems(logMessages);
	}

	@FXML
	public void handleLotteryStart(ActionEvent event) {
		progressBar.setProgress(0.0);
		Task task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Lottery lottery = new Lottery(raffleTickets, prizes);

				int prizeCount = prizes.size();
				int remainingPrizes;
				updateProgressBar(prizeCount, lottery.getRemainingPrizes());
				while ((remainingPrizes = lottery.getRemainingPrizes()) > 0) {
					lottery.selectWinner();
					updateProgressBar(prizeCount, remainingPrizes);
				}
				winners = lottery.getWinners();
				return null;
			}
		};
		logInfoMessage("Starting Lottery in Background");
		logInfoMessage("This will take a while.");
		logInfoMessage(new Date().toString());
		progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		new Thread(task).start();
		task.setOnSucceeded(event1 -> handleLotteryComplete(event));

	}

	private void handleLotteryComplete(ActionEvent event) {
		progressBar.setProgress(1.0);
		logInfoMessage(String.format("Lottery Completed Successfully with %d winners chosen", winners.size()));
		logInfoMessage(new Date().toString());
		progressBar.setProgress(0.0);
		Node node = (Node) event.getSource();
		File file = showFileChooser(node.getScene().getWindow(), true);
		ResultWriter writer = new ResultWriter(file);
		try {
			writer.processWinners(winners);
			logInfoMessage(String.format("Lottery Complete at %s", new Date().toString()));
			logInfoMessage(String.format("Output at '%s'", file.toString()));
		} catch (IOException e) {
			logErrorMessage(e.getMessage());
		}
	}

	private void updateProgressBar(Integer totalPrizes, Integer remainingPrizes) {
		Integer completed = totalPrizes - remainingPrizes;
		double value = completed.doubleValue() / totalPrizes.doubleValue();
		Platform.runLater(() -> progressBar.setProgress(value));
	}

	@FXML
	public void handleDonationButtonClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		final File file = showFileChooser(node.getScene().getWindow());
		String fileName = file != null ? file.toString() : "";
		if (!StringUtils.isEmpty(fileName)) {
			logInfoMessage(String.format("Parsing targetFile '%s'", fileName));
			FileOperationTask<Void> task = new FileOperationTask<Void>(file) {
				@Override
				protected Void call() throws Exception {
					OptimizedDonationReader donationReader = new OptimizedDonationReader(file);
					donationReader.process();
					final List<DonationEntry> entries = donationReader.getEntries();
					logInfoMessage(String.format("%d lines read from Spreadsheet", entries.size()));
					raffleTickets = OptimizedDonationMapper.mapAll(entries);
					logInfoMessage(String.format("%d Raffle Tickets Generated", raffleTickets.size()));
					return null;
				}
			};
			new Thread(task).run();
			task.setOnSucceeded(event1 -> verifyRunReady());
			donorField.setText(fileName);
		}
	}

	@FXML
	public void handlePrizeButtonClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		final File file = showFileChooser(node.getScene().getWindow());
		String fileName = file != null ? file.toString() : "";
		if (!StringUtils.isEmpty(fileName)) {
			logInfoMessage(String.format("Parsing targetFile '%s'", fileName));
			FileOperationTask<Void> task = new FileOperationTask<Void>(file) {
				@Override
				protected Void call() throws Exception {
					OptimizedPrizeReader prizeReader = new OptimizedPrizeReader(targetFile);
					prizeReader.process();
					final List<PrizeEntry> entries = prizeReader.getEntries();
					logInfoMessage(String.format("%d lines read from Spreadsheet", entries.size()));
					prizes = OptimizedPrizeMapper.mapAll(entries);
					logInfoMessage(String.format("%d Prizes Generated", prizes.size()));
					return null;
				}
			};
			new Thread(task).run();
			task.setOnSucceeded(event1 -> verifyRunReady());
			prizeField.setText(fileName);
		}
	}

	private File showFileChooser(Window window) {
		return showFileChooser(window, false);
	}

	private File showFileChooser(Window window, Boolean saveAction) {
		File file;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		if (saveAction) {
			file = fileChooser.showSaveDialog(window);
		} else {
			file = fileChooser.showOpenDialog(window);
		}

		return file;
	}

	private void verifyRunReady() {
		if ((raffleTickets != null && !raffleTickets.isEmpty()) && (prizes != null && !prizes.isEmpty())) {
			runButton.setDisable(false);
		}
	}

	private void logInfoMessage(String message) {
		logMessages.add(message);
		Platform.runLater(() -> consoleBox.scrollTo(logMessages.size() - 1));
	}

	private void logErrorMessage(String message) {
		logMessages.add(String.format("ERROR: %s", message));
		log.error(message);
		Platform.runLater(() -> consoleBox.scrollTo(logMessages.size() - 1));
	}


}
