package com.example.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TickMarkDatabaseHelper {
	private static final String url = "http://localhost:3306/";
	private static final String dbName = "Tickmark";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "root"; 
	private static final String password = "";
	
	public static void main(String[] args) {
	    System.out.println("MySQL Connect Example.");
	    Connection conn = null;
	    Statement stmt = null;
	    try {
	      Class.forName(driver).newInstance();
	      conn = DriverManager.getConnection(url+dbName,userName,password);
	      System.out.println("Connected to the database");
	      stmt = conn.createStatement();
	      String sql = "Select * from LOGIN;";
	      ResultSet rs = stmt.executeQuery(sql);
	      while(rs.next())
	      {
	          int id=rs.getInt("LOGINID");
	          System.out.println(id);
	      }
	      conn.close();
	      System.out.println("Disconnected from database");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
