package jdbc;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SignUpController {
	
	@FXML
    private TextField genderTextField;
	
	@FXML
    private TextField nameTextField;
	
	@FXML
    private TextField lastNameField;
	
	@FXML
    private TextField cityTextField;
	
	@FXML
    private TextField usernameTextField;

	@FXML
    private Label warningLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
	private Pane signUpPane;

    @FXML
    private TextField signUpLoginField;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField signUpPasswordField;

    @FXML
    private Button signUpSignInButton;

    @FXML
    private Label signUpLabel;

    @FXML
    void initialize() {
    	
    	signUpSignInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                
            	try {
            		Stage st=(Stage)signUpSignInButton.getScene().getWindow();
            		st.hide();
            		Stage stage=new Stage();
					Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
					stage.setScene(new Scene(root));
					stage.show();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	  
          	       	  
            }
        });
    	
    	signUpButton.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent mouseEvent) {
    			String password=signUpPasswordField.getText();
    			String login=signUpLoginField.getText();
    			String name= nameTextField.getText();
    			String lastname= lastNameField.getText();
    			String username=usernameTextField.getText();
    			String gender=genderTextField.getText();
    			if(gender.compareTo("")==0) {
    				gender="-";
    			}
    			String location=cityTextField.getText();
    			if(location.compareTo("")==0) {
    				location="-";
    			}
    			
    			
					
					if(login.compareTo("")!=0&&password.compareTo("")!=0&&name.compareTo("")!=0&&lastname.compareTo("")!=0) {
						try {
							ResultSet result= Main.getResult("select login from users where login ="+"'"+login+"'");
							ResultSet unResult=Main.getResult("select username from users where username="+"'"+username+"'");
							if(result.next()) {
								signUpLoginField.setText("");
								signUpLoginField.setPromptText("Such login is already taken");
							}
							else if(unResult.next()) {
								usernameTextField.setText("");
								usernameTextField.setText("Such username is already taken");
							}
							else {
								if(username.compareTo("")!=0) {
								Main.updateDB("insert into users ( firstname,lastname,username,location,gender,login,password) values("+
							"'"+name+"'"+","+"'"+lastname+"'"+","+"'"+username+"'"+","+"'"+location+"'"+","+"'"+gender+"'"+","+"'"+login+"'"+","+"'"+password+"'"+")");
								}else {
									Main.updateDB("insert into users ( firstname,lastname,location,gender,login,password) values("+
											"'"+name+"'"+","+"'"+lastname+"'"+","+"'"+location+"'"+","+"'"+gender+"'"+","+"'"+login+"'"+","+"'"+password+"'"+")");
								}
								try {
				            		Stage st=(Stage)signUpButton.getScene().getWindow();
				            		st.hide();
				            		Stage stage=new Stage();
									Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
									stage.setScene(new Scene(root));
									stage.show();
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else {
						warningLabel.setVisible(true);
					}
					
					
					
 		
				
    			
    		}
    	});
    	
	
    	
    }
}

