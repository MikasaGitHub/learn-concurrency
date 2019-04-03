package com.akira.concurrency.writeandreadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Demo {
	
	private Map<String, String> maps = new HashMap<>();
	
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	
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
	
}
