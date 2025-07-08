//This is the primary java file.
//compile project as a MAVEN Java project
//pom.xml is not standard, adjustments have been made and I've uploaded mine.
//I'm using VSCode, not eclipse as past authors, which came with extra setup
//
//Type the following into command prompt to run current build: mvn javafx:run

package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


//Bare-bones GUI class that initializes window
public class GUI extends Application {
	
	private Stage primaryStage;
	private AnchorPane ap;
			
	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			Parent root = FXMLLoader.load(getClass().getResource("/gui/GUI.fxml"));
			primaryStage.setTitle("TCG Randomizer");
			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to load the GUI");
			alert.setContentText("An error occurred while loading the user interface. The application will now exit.");
			alert.showAndWait();

			Platform.exit();
		}
	}

	public static void main (String[] args) {		
		launch();
	}

	public Stage getPrimaryStage (Event e) {
		return primaryStage;
	}

	public void setPrimaryStage (Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public AnchorPane getAnchorPane() {
		return ap;
	}

	public void setAnchorPane(AnchorPane ap) {
		this.ap = ap;
	}	
}
