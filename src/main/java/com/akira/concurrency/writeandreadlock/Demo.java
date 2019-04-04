package com.akira.concurrency.writeandreadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Demo {
	
	private Map<String, String> maps = new HashMap<>();
	
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	
	private volatile boolean isUpdate;
	
	// 读锁
	private Lock r = rw.readLock();
	// 写锁
	private Lock w = rw.writeLock();
	
	public String get(String key) {
		r.lock();
		System.out.println("线程:" + Thread.currentThread().getName() + "开始读" );
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			r.unlock();
			System.out.println("线程:" + Thread.currentThread().getName() + "读结束");
		}
		return maps.get(key);
	}
	
	public void put(String key, String value) {
		w.lock();
		System.out.println("线程:" + Thread.currentThread().getName() + "写操作正在进行" + value);
		try {
			Thread.sleep(3000);
			maps.put(key, value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			w.unlock();
			System.out.println("线程:" + Thread.currentThread().getName() + "写操作完成");
		}
	}
	
	/**
	 * 锁降级
	 * 线程进入读锁的前提条件
	 * 1.没有其他线程的写锁。
	 * 2.没有写请求，或者有写请求但调用线程和持有锁的线程是同一个线程
	 * 
	 * 进入写锁的条件:
	 * 1.没有其他的线程的读锁
	 * 2.没有其他线程的写锁
	 * @param vlaue
	 */
	public void readWrite(String vlaue) {
		//1上读锁,防止其他线程修改
		r.lock();
		if(!isUpdate) {
			// 2.要进行修改，首先释放读锁
			r.unlock();
			// 对当前线程上写锁。防止其他线程访问
			w.lock();
			// 修改
			maps.put("xxx", vlaue);
			isUpdate = true;
			// 这里释放写锁之前要先给自己加读锁，
			// 目的主要是为了防止另一个线程（记作线程T）获取了写锁并修改了数据，
			// 那么当前线程无法感知线程T的数据更新。如果当前线程获取读锁，即遵循锁降级的步骤，
			// 则线程T将会被阻塞，直到当前线程使用数据并释放读锁之后，线程T才能获取写锁进行数据更新。
			r.lock();
			
			// 释放写锁
			w.unlock();
		}
		
		String obj = maps.get("xxx");
		System.out.println(obj);
		
		// 释放读锁。
		r.unlock();
	}
	
}
