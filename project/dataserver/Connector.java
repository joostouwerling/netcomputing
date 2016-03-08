package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
	static Connection conn = null;
	
	public Connector() throws Exception { 
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/abc?autoReconnect=true&useSSL=false";
		String username = ""; 
		String password = ""; 
		Class.forName(driver); 
		conn = DriverManager.getConnection(url, username, password); 
		System.out.println("Connection\n"); 
	}
	
	public void storePackage(String name, String date, float magnitude, float longitude) throws Exception{
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement("insert into  locations values (default, ?, ?, ?, ?)");
		// "name, date, magnitude, longitude");
		// Parameters start with 1
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, date);
		preparedStatement.setFloat(3, magnitude);
		preparedStatement.setFloat(4, longitude);
		preparedStatement.executeUpdate();
}
	
	public void printDatabase() throws SQLException { 
		Statement stmt = null; 
		System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    String sql;
	    sql = "SELECT * FROM locations";
     	ResultSet rs = stmt.executeQuery(sql);

     	while(rs.next()){
     		int id  = rs.getInt("idLocations");
     		String name = rs.getString("name");
     		String date = rs.getString("date"); 
     		Long magnitude = rs.getLong("magnitude");
     		Long longitude = rs.getLong("longitude");
     		
     		System.out.print("idLocations: " + id + ", " + "name: " + name + ", "+ "date: " +  date + ", " + "magnitude" + magnitude + ", "+ "longitude: "+longitude+ "\n");
     	 }
     	rs.close();
	}
	
	public void closeConnection() throws SQLException {
     	conn.close();
	}
}