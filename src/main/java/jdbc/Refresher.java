package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Refresher implements Runnable{
	private Controller controller;
	private static int lastMesId=0;
	private static ResultSet topId;
	private static ResultSet result;
	private static int idsender=-1;
	private static int idreciever=-1;
	private static volatile boolean access;
	public Refresher(Controller controller) {
		this.controller= controller;
	}
public  void run() {
	int temp=-1;
	
		while(Controller.running) {
			
			idsender=Controller.getIdSender();
			idreciever=Controller.getIdReciever();
			
			if(access&&idsender!=-1) {
				
				
				
				try {
				
					topId =Main.getResult("select max(messageid) from messages where idsender = "+String.valueOf(idsender)+" and idreciever = "+String.valueOf(idreciever) );
	    			while(topId.next()) {
	    				temp=topId.getInt("max(messageid)");
//System.out.println(temp);
	    			}
	    			topId=null;
	    		
//System.out.println(lastMesId);
	    		
    			

	    			if(temp>lastMesId) {
//System.out.println("CHECKPOINT");	

						result= Main.getResult("select * from messages where idsender = "+String.valueOf(idsender)+" and idreciever = "+String.valueOf(idreciever)+" and messageid > "+String.valueOf(lastMesId)+" and messageid <= "+String.valueOf(temp));

	    				while(result.next()) {
//System.out.println("YOU ARE WINNNNNN!!!!");
	    					String message=result.getString("messText");
//System.out.println(message);
	    					controller.refresh(message);
	    				}    			  			   			
	    			
	    				result=null;
	    			}
	    			lastMesId=temp;
					Thread.sleep(200);
				} catch(SQLException e) {
					System.out.println("REFRESHER SGL ERROR");
					e.printStackTrace();
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			
				
			}
			
		}
	
	}
	
	public static void setTopMesId(int topMesId) {
		lastMesId=topMesId;
	}
	public static void unable() {
//System.out.println("unabled");
		access=true;
	}
	public static void disable() {
		access=false;
//System.out.println("disabled");
	}
	
}
