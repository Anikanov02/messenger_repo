package jdbc;
import java.sql.*;


public class Main {
	
	public static final String PASSWORD="12345";
	public static final String USERNAME="root";
	public static final String URL="jdbc:mysql://localhost/table?serverTimezone=UTC";
	static Connection connection = null;
	static Statement statement = null;
	static String[] arguments;
	public static void main(String[] args) {
		arguments = args;
		
		try {
			
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("connection failure: Exception detected ");
			e.printStackTrace();
		}
		System.out.println("DB connection successfully created");
		
		JFXApp.startup(arguments);
		
		
				
	}
	
	
	public static synchronized ResultSet getResult(String Query) {
		try {
			statement = connection.createStatement();
			
			return	statement.executeQuery(Query);
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("getResult() METHOD ERROR");
			e.printStackTrace();
		}

		
		
		
		return null;
	} 
	
	public static synchronized void setMessage(String Query) {
		try {
			
			statement = connection.createStatement();
			statement.executeUpdate(Query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("sendMessage() METHOD ERROR");
			e.printStackTrace();
		}
		
		
	}
	
	public static synchronized void updateDB(String Query) {
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	
}

	

