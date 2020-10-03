package jdbc;

import java.sql.SQLException;



import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JFXApp extends Application {
	
	public static void startup(String[] args) {
		Application.launch(args);	
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		Parent signUpRoot = FXMLLoader.load(getClass().getResource("/signUp.fxml"));
		primaryStage.setScene(new Scene(signUpRoot));
		primaryStage.show();
		

		
		
	}
	
	
	
	
	
	
	
	

}
