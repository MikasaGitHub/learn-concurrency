package com.akira.concurrency.aqs;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 自行实现的锁测试
 * @author xiaoming
 *
 */
public class TestMyLock {

	private Integer count = 1;
	private MyLock lock = new MyLock();
	
	public Integer getNext() {
		lock.lock();
		try {
			Thread.sleep(100);
			return count++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return count;
	}
	
	public Runnable getThread() {
		return new Runnable() {
			@Override
			public void run() {
				while(true){
					System.out.println("线程名称:" + Thread.currentThread().getName() + "数字:" + getNext());
				}
			}
		};
	}
	
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		TestMyLock test = new TestMyLock();
		
		executor.execute(test.getThread());
		executor.execute(test.getThread());
		executor.execute(test.getThread());
		executor.execute(test.getThread());
		executor.execute(test.getThread());
	}
	
}
