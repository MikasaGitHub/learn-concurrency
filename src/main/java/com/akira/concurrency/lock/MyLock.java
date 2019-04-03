package com.akira.concurrency.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author xiaoming
 */
public class MyLock implements Lock{
	
	private boolean lock = false;
	
	private Thread currentT = null;
	
	private int lockcount = 0;

	@Override
	public synchronized void lock() {
		
		Thread currentThread = Thread.currentThread();
		
		while(lock && currentT != currentThread) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		currentT = currentThread;
		lock = true;
		lockcount ++;
	}
	
	@Override
	public synchronized void unlock() {
		if(currentT == Thread.currentThread()) {
			lockcount --;
			
			if(lockcount == 0) {
				notify();
				lock = false;
			}
		}
		
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
