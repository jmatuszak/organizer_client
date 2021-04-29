package org.organizerClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.organizerClient.gui.utilities.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

@SpringBootApplication
public class OrganizerClientApplication extends Application {

	// Define your offsets here
	private double xOffset = 0;
	private double yOffset = 0;

	public ConfigurableApplicationContext springContext;
	private FXMLLoader fxmlLoader;


	public static void main(String[] args) {
		Application.launch();
	}

	@Override
	public void init(){
		springContext = SpringApplication.run(OrganizerClientApplication.class);
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(springContext::getBean);
	}

	@Override
	public void start(Stage stage) throws Exception {

		URL path = getClass().getResource(Constants.FXML_HOME);
		if (path != null) {
			Parent root = fxmlLoader.load(path);
			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.setTitle(Constants.APP_TITLE);
			stage.initStyle(StageStyle.TRANSPARENT);

			// Grab your root here
			root.setOnMousePressed((MouseEvent event) -> {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			});

			// Move around here
			root.setOnMouseDragged((MouseEvent event) -> {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			});

			stage.show();
		} else {
			System.exit(-1);
		}


	}
}
