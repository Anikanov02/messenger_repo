package jdbc;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class Controller {
	
	@FXML
    private Button searchButton;
	
	@FXML
    private TextField searchTextField;

    @FXML
    private  ResourceBundle resources;

    @FXML
    private  URL location;

    @FXML
    private  Button sendMessageButton;
    
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ListView<Button> listView;

	@FXML
	private ListView<Label> messageList;

    @FXML
    private  TextArea textField;
    
    private static int topMesId=0;
    private static int idsender=-1;
    private static int idreciever;
    private static String city;
    private static String username;
    private static String lastname;
    private static String name;
    private static String gender;   
    private static int limit=100;
    public static boolean running;

	private static enum loadOption{
		MAIN_CHAT_STACK,
		DESIRED_CHATS
	}
    
    public synchronized void refresh(String message) {

		try {
			ResultSet result =Main.getResult("select firstname,lastname from users where idusers = "+String.valueOf(idsender));
			while(result.next()) {
				messageList.getItems().add(new Label("REFRESHER:"+result.getString("firstname")+" "+result.getString("lastname")+": "+ message+"\n"));
			}

		} catch (SQLException e) {
			System.out.println("CONTROLLER refresh() METHOD ERROR");
			e.printStackTrace();
		} 
    	
    }
    public static void setUserData(int idRec) {
    	try {
    	idreciever=idRec;
    	ResultSet set=Main.getResult("select*from users where idusers = "+idreciever);
    		while(set.next()) {
    	
				name=set.getString("firstname");
				lastname=set.getString("lastname");
				username=set.getString("username");
				city = set.getString("location");
				gender=set.getString("gender");
		
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }   
    public static int getIdSender() {
    	return idsender;
    }
    public static int getIdReciever() {
    	return idreciever;
    }
    private void startRefreshing() {
    	new Thread(new Refresher(this),"Refresher").start();
    }
    private void loadChats(@org.jetbrains.annotations.NotNull loadOption lo) {
		try {
			listView.getItems().removeAll(listView.getItems());//delete existing chats to load new
			ResultSet usersChat;
			switch(lo) {

				case MAIN_CHAT_STACK :
					usersChat = Main.getResult("call p_Get_Users_Chats("+idreciever+")");
					break;

				case DESIRED_CHATS:
					usersChat = Main.getResult("call p_Get_Desired_Chats('"+searchTextField.getText().trim()+"')");
					break;

				default:
					usersChat = Main.getResult("");
				break;

			}
			while(usersChat.next()) {
				Button chatButton;
				final int id_sender_temp=usersChat.getInt("id_collocutor");
				chatButton = new Button(usersChat.getString("collocutors_name"));
				listView.getItems().add(chatButton);
				listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				chatButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent mouseEvent) {
						try {
							Refresher.disable();
							messageList.getItems().removeAll(messageList.getItems());
							messageList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
							textField.setDisable(false);
							if(idsender==-1) {
								startRefreshing();
//System.out.println("refreshing started");
							}
							idsender=id_sender_temp;
//System.out.println(idsender);
							ResultSet topId=Main.getResult("select max(messageid) from messages where idsender = "+idsender+" and idreciever = "+idreciever );
							while(topId.next()) {
								topMesId=topId.getInt("max(messageid)");
							}
							topId.close();
							Refresher.setTopMesId(topMesId);

							String s="";
							ResultSet result=Main.getResult("select*from(select* from messages where idsender = "+idsender+" and idreciever = "+idreciever+" or(idsender = "+idreciever+" and idreciever = "+idsender+") order by messageid desc limit "+limit+") t order by messageid asc");
							ResultSet nameres;
							while(result.next()) {

								int id=result.getInt("idsender");
								nameres=Main.getResult("select firstname,lastname from users where idusers = "+id);
								while(nameres.next()) {
									s+=nameres.getString("firstname")+" "+nameres.getString("lastname")+": ";
								}
								nameres.close();
								s+=result.getString("messText")+"\n";
								messageList.getItems().add(new Label(s));
								s="";

							}
							messageList.scrollTo(messageList.getItems().size());


							result.close();

							Refresher.unable();

						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});

			}
			usersChat.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }
    @FXML
    void initialize() {
    	
    	running=true;
    	loadChats(loadOption.MAIN_CHAT_STACK);
    	sendMessageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
          	public void handle(MouseEvent mouseEvent) {
				if (messageList.getItems().isEmpty()){
					Main.updateDB("call p_Register_New_Chat("+idreciever+","+idsender+")");
				}
        	  	String s=textField.getText();
        	  	if(s.compareTo("")!=0) {
        	 	messageList.getItems().add(new Label(name+" "+lastname+": "+ s+"\n"));
        	 	textField.deleteText(0,textField.getText().length());
        	 	Main.setMessage("CALL p_SendMessage("+"'"+s+"'"+","+idreciever+","+idsender+");");
        	 	s="";

        	  }
        	       	  
          	}
      });

    	searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(searchTextField.getText().trim().compareTo("")==0) {
					loadChats(loadOption.MAIN_CHAT_STACK);
					searchButton.setText("Search");
				}else{
					if(searchTextField.getText().trim().compareTo("")!=0) {
						loadChats(loadOption.DESIRED_CHATS);
						searchButton.setText("Back");
					}else{
						searchTextField.setPromptText("Enter username");
					}
				}

			}
		});
      
    }
}

