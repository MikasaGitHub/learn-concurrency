package com.akira.concurrency.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyDataSource {
	
	private LinkedList<Connection> pool = new LinkedList<>();
	
	private static final int INIT_CONNECTIONS = 10;
	
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	
	private static final String USER_NAME = "";
	
	private static final String PASSWORD = "";
	
	private static final String URL = "";
	
	private static Lock lock = new ReentrantLock();
	
	private static Condition p1 = lock.newCondition();
	
	static {
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public MyDataSource() {
		for (int i = 0; i < INIT_CONNECTIONS; i++) {
			try {
				Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
				pool.addLast(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnect() {
		Connection result = null;
		lock.lock();
		while(pool.size() <= 0) {
			try {
				p1.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		}
		if (!pool.isEmpty()) {
			result = pool.removeFirst();
		}
		return result;
	}
	
	public static Integer test(int count) {
		
		
		try {
			System.out.println("1");
			return count;
		} catch (Exception e) {
			
		}
		finally {
			count = 2;
			System.out.println("2");
		}
		System.out.println("3");
		return count;
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(test(1));
		
		
	}
	
}
