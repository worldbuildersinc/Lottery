package org.worldbuilders;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


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
	private boolean donorsChosen = false;
	private boolean prizesChosen = false;
	private ObservableList<String> logMessages;

	public void handleWindowShownEvent() {
		logMessages = FXCollections.observableArrayList("Ready to start");
		consoleBox.setItems(logMessages);
	}

	@FXML
	public void handleLotteryStart(ActionEvent event) {
		logInfoMessage("Inside handleLotteryStart");
	}

	@FXML
	public void handleDonationButtonClick(ActionEvent event) {
		logInfoMessage("handleDonationButtonClick");
		Node node = (Node) event.getSource();
		String fileName = showFileChooser(node.getScene().getWindow());
		logInfoMessage(String.format("Parsing file '%s'", fileName));
		if (!StringUtils.isEmpty(fileName)) {
			donorsChosen = true;
			donorField.setText(fileName);
		}
		verifyRunReady();
	}

	@FXML
	public void handlePrizeButtonClick(ActionEvent event) {
		logInfoMessage("handlePrizeButtonClick");
		Node node = (Node) event.getSource();
		String fileName = showFileChooser(node.getScene().getWindow());
		logInfoMessage(String.format("Parsing file '%s'", fileName));
		if (!StringUtils.isEmpty(fileName)) {
			prizesChosen = true;
			prizeField.setText(fileName);
		}
		verifyRunReady();
	}

	private String showFileChooser(Window window) {
		return showFileChooser(window, false);
	}

	private String showFileChooser(Window window, Boolean saveAction) {
		File file;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
		fileChooser.getExtensionFilters().add(extFilter);
		if (saveAction) {
			file = fileChooser.showSaveDialog(window);
		} else {
			file = fileChooser.showOpenDialog(window);
		}

		return file.toString();
	}

	private void verifyRunReady() {
		if (donorsChosen && prizesChosen) {
			runButton.setDisable(false);
		}
	}

	private void logInfoMessage(String message) {
		logMessages.add(message);
		log.debug(message);
		Platform.runLater(() -> consoleBox.scrollTo(logMessages.size() - 1));
	}


}
