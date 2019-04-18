package com.akira.concurrency.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 * @author Akira
 *
 */
public class SemaphoreDemo {

	private static final Semaphore semaphore = new Semaphore(3);
	private static final ThreadPoolExecutor threadpool = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	private static class Person extends Thread {
		private final String name;
		private final int age;
		
		public Person(String name,int age) {
			this.name=name;
			this.age=age;
		}
		
		@Override
		public void run() {
			try {
				semaphore.acquire();
				System.out.println(Thread.currentThread().getName()+":大家好，我是"+name+"我今年"+age+"岁");
				Thread.sleep(1000);
				semaphore.release();
				System.out.println(name+"释放许可证");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		String [] name = {"张三","李四","王五","赵云","关羽","吕布","张飞"};
		int[] age= {26,27,33,45,19,23,41};
		
		for(int i=0;i<7;i++) {
			Thread t1=new Person(name[i],age[i]);
			threadpool.execute(t1);
		}
		
	}
	
	
	
}
