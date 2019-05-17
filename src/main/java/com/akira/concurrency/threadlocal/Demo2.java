package com.akira.concurrency.threadlocal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Demo2 {
	
	private static ReentrantLock lock = new ReentrantLock();
	
	private static Integer index = 0;
	
	private static List<AI> lists  = build();
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		lists.parallelStream().forEach(r -> {
			
			lock.lock();
			Integer roomId = r.getId();
			r.setPhone(String.valueOf(roomId));
			System.out.println(Thread.currentThread().getName() + r);
			lock.unlock();
			
		});
		
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}
	
	private static Runnable handle() {
		
		return new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				
					while(!index.equals(lists.size())) {
						lock.lock();
						AI ra = lists.get(index);
						Integer roomId = ra.getId();
						ra.setPhone(String.valueOf(roomId));
						System.out.println(Thread.currentThread().getName() + ra);
						index ++;
						lock.unlock();
					}
					
					
					long endTime = System.currentTimeMillis();
					System.out.println(endTime - startTime);
					executor.shutdown();
				}
		};
	}
	
	private static List<AI> build() {
		
		List<AI> lists = new ArrayList<>();
		
		for (int i = 1; i <= 1000000; i++) {
			AI ai = new AI();
			ai.setId(i);
			lists.add(ai);
		}
		return lists;
	}
	
}
