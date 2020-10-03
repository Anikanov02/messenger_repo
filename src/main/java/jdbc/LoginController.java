package jdbc;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController {
	
    @FXML
	private CheckBox checkBox;
	
	@FXML
    private Label infoLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane signInPane;

    @FXML
    private Button backToSignUpButton;

    @FXML
    private PasswordField signInPasswordField;

    @FXML
    private Button signInButton;

    @FXML
    private TextField signInLoginField;

    @FXML
    void initialize() {
    	final String filePath="src/main/java/jdbc/Cache.txt";
    	try {
			Scanner scanner = new Scanner(new File(filePath));
			if(scanner.hasNext()) {
			signInLoginField.insertText(0, scanner.next());
			signInPasswordField.insertText(0, scanner.next());
			}
			scanner.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	
    	backToSignUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                
            	try {
            		Stage st=(Stage)backToSignUpButton.getScene().getWindow();
            		st.hide();
            		Stage stage=new Stage();
					Parent root = FXMLLoader.load(getClass().getResource("/signUp.fxml"));
					stage.setScene(new Scene(root));
					stage.show();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	  
            		  
            }
        });
    	
    	
    	signInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		public void handle(MouseEvent mouseEvent) {
    			try {
    				String login=signInLoginField.getText();
    				String password=signInPasswordField.getText();
    				
    			if(login.compareTo("")!=0&&password.compareTo("")!=0) {
    				
    				
    				//ResultSet checklogin=Main.getResult("Select f_CheckPwd('"+login+"','"+password+"') as idusers");
    				ResultSet checklogin=Main.getResult("call p_CheckPwd('"+login+"','"+password+"')");
    				if(checklogin.next()) {//such user exists
    				
    					if(checkBox.isSelected()) {  							 
    						FileWriter fw= new FileWriter(new File(filePath),false);
    						fw.write(login+" ");
    						fw.write(password);
    						fw.close();
    					}
    					
    				Controller.setUserData(checklogin.getInt("idusers"));
    				Stage prefStage= (Stage)signInButton.getScene().getWindow();
    				prefStage.hide();
    				Stage stage= new Stage();
    				Parent root;
				
					root = FXMLLoader.load(getClass().getResource("/mainWindow.fxml"));
				
					stage.setScene(new Scene(root));
					stage.setTitle("Some Messenger");
    				stage.show();
				
    				stage.setOnCloseRequest(
    					new EventHandler<WindowEvent>() {
    							public void handle(WindowEvent event) {
    								Controller.running=false;
    								try {
    									Main.statement.close();
    								} catch (SQLException e) {
    									// TODO Auto-generated catch block
    									System.out.println("setOnCloseError");
    									e.printStackTrace();
    								}
    							}
    	                }
    			
    					);
    			
    					
    				}
    				else {
    					infoLabel.setText("login or password is incorrect");
    				}
    			}else {
    				if(password.compareTo("")==0) {
    				signInPasswordField.setPromptText("Password field is empty!!!");
    				}
    				if(login.compareTo("")==0) {
    				signInLoginField.setPromptText("Login field is empty!!!");
    				}
    			}
    			
    			
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			} 
    			
    			
    			
    			
    			
    		}
    	});
    	
    	
    	
    }
}

