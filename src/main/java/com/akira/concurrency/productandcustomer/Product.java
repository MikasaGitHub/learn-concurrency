package com.akira.concurrency.productandcustomer;

public class Product implements Runnable{
	
	private Tmall tmall;
	
	public Product(Tmall tmall) {
		this.tmall = tmall;
	}

	@Override
	public void run() {
		while(true)
			try {
				tmall.push();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

}
