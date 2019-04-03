package com.akira.concurrency.lock;

public class Demo1 {

	MyLock lock = new MyLock();
	
	public void a() {
		lock.lock();
		System.out.println("a");
		b();
		lock.unlock();
	}
	
	public void b() {
		lock.lock();
		System.out.println("b");
		lock.unlock();
	}
	
	public static void main(String[] args) {
		
		Demo1 demo1 = new Demo1();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				demo1.a();
			}
		}).start();
	}
}
