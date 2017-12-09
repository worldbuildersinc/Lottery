package org.worldbuilders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.worldbuilders.lottery.util.HibernateUtil;

public class MainApp extends Application {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) throws Exception {
		HibernateUtil.getSessionFactory();
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		log.info("Starting Application");


		String fxmlFile = "/fxml/lottery.fxml";
		FXMLLoader loader = new FXMLLoader();
		Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

		final MainController controller = loader.getController();
		stage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> controller.handleWindowShownEvent());
		Scene scene = new Scene(rootNode);
		scene.getStylesheets().add("/styles/styles.css");
		stage.setTitle("WorldBuilders Lottery");
		stage.setScene(scene);
		stage.show();
	}
}
