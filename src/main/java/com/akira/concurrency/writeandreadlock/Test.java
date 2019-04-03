package com.akira.concurrency.writeandreadlock;

public class Test {
	public static void main(String[] args) {
		Read();
	}
	
	/**
	 *  三个线程同时写(写写互斥)
	 *  线程:Thread-0写操作正在进行value1
		线程:Thread-0写操作完成
		线程:Thread-1写操作正在进行value2
		线程:Thread-1写操作完成
		线程:Thread-2写操作正在进行value3
		线程:Thread-2写操作完成
	 */
	public static void threeThreadToWrite() {
		
		Demo d = new Demo();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				d.put("key1", "value1");
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				d.put("key2", "value2");
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				d.put("key3", "value3");
			}
		}).start();
	}
	
	/**
	 *  线程:Thread-0写操作正在进行value1(读写也互斥)
		线程:Thread-0写操作完成
		线程:Thread-1开始读
		线程:Thread-1读结束
		value1
	 */
	public static void writeAndRead() {
		
		Demo d = new Demo();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				d.put("key1", "value1");
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(d.get("key1"));
			}
		}).start();
		
	}
	
	/**
	 *  线程:main写操作正在进行value)
		线程:main写操作完成
		线程:Thread-0开始读
		线程:Thread-1开始读  (读读不互斥，同时进行)
		线程:Thread-2开始读
		线程:Thread-0读结束
		value1
		线程:Thread-1读结束
		线程:Thread-2读结束
		value1
		value1
	 */
	public static void Read() {
		
		Demo d = new Demo();
		d.put("key1", "value1");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(d.get("key1"));
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(d.get("key1"));
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(d.get("key1"));
			}
		}).start();
		
	}
}
