package com.akira.concurrency.productandcustomer;

public class Tmall {
	
	private int count;
	
	private final int MAX_COUNT = 10;
	
	public synchronized void push() {
		
		// 这是因为，如果采用if判断，当线程从wait中唤醒时，那么将直接执行处理其他业务逻辑的代码，
		// 但这时候可能出现另外一种可能，条件谓词已经不满足处理业务逻辑的条件了，从而出现错误的结果，于是有必要进行再一次判断
		while(count >= MAX_COUNT) {
			try {
				System.out.println(Thread.currentThread().getName() + "库存量已满停止生产");
				wait();
				// 被唤醒的线程因为while需要重新唤醒一次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		count ++;
		System.out.println(Thread.currentThread().getName() + "生产者生产,库存量:" + count);
		notifyAll();
	}
	
	public synchronized void take() {
		while(count <= 0) {
			try {
				System.out.println(Thread.currentThread().getName() + "库存量为零,消费者等待");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		count --;
		System.out.println(Thread.currentThread().getName() + "消费者消费 当前库存" + count);
		notifyAll();
	}
	
	public static void main(String[] args) {
		Tmall tmall = new Tmall();
		
		new Thread(new Product(tmall)).start();
		new Thread(new Product(tmall)).start();
		new Thread(new Product(tmall)).start();
		
		new Thread(new Customer(tmall)).start();
		new Thread(new Customer(tmall)).start();
		new Thread(new Customer(tmall)).start();
		new Thread(new Customer(tmall)).start();
	}

}
