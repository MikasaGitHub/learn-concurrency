package com.akira.concurrency.productandcustomer;

public class Customer implements Runnable{
	
	private Tmall tmall;
	
	public Customer(Tmall tmall) {
		this.tmall = tmall;
	}

	@Override
	public void run() {
		while(true)
			try {
				Thread.sleep(1000);
				tmall.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	
}
