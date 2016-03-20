package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beans.Events;

public class Account {
	private final String dbName = "lokshala";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/";
	private final String user="root";
	private final String password = "";
	private final String TABLE_EVENT = "events";
	private final String TABLE_USERS = "users";
	Connection con;
	private String msg = "";
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	private void dbConnect() throws ClassNotFoundException, SQLException{
		Class.forName(driver);
		
		con = DriverManager.getConnection(url+dbName, user, password);
	}
	
	
	public boolean doLogin(String email , String password) throws ClassNotFoundException, SQLException{
		dbConnect();
		String query = "Select count(*) as count from "+TABLE_USERS+" where email = ? and password = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		
		pstmt.setString(1, email);
		pstmt.setString(2, password);
		
		ResultSet rs = pstmt.executeQuery();
		
		int count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}
		rs.close();
		dbClose();
		if(count > 0){
			return true;
		}
		return false;
	}
	
	public boolean doRegister(String email,String password ,String name,String type) throws ClassNotFoundException, SQLException{
		dbConnect();
		
		String sql = "select count(*) as count from users where email = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, email);
		ResultSet rsCount = pstmt.executeQuery();
		int count = 0;
		while(rsCount.next()){
			count = rsCount.getInt("count");
		}
		System.out.println(count);
		if(count > 0){
			dbClose();
			pstmt.close();
			rsCount.close();
			setMsg("Email id already exists!!");
			return false;
		}
		String query = "insert into users (email,password,name,type) values (?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, password);
		ps.setString(3, name);
		ps.setString(4, type);
		System.out.println(ps);
		int i = ps.executeUpdate();
		dbClose();
		if(i > 0){
			setMsg("Sucessfully Registered");
			return true;
		}
		setMsg("Error Registring");
		return false;
	}
	
	
	public ArrayList<Events> getEvents() throws ClassNotFoundException, SQLException{
		
		dbConnect();
		String sql = "select * from "+TABLE_EVENT;
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<Events> events = new ArrayList<Events>();
 		while( rs.next() ){
			Events event = new Events();
			
			event.setEvent_title(rs.getString("title"));
			event.setEvent_description(rs.getString("description"));
			event.setEvent_image(rs.getString("image"));
			events.add(event);
		}
		
		
		dbClose();
		return events;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void dbClose() throws SQLException{
		con.close();
	}
}
