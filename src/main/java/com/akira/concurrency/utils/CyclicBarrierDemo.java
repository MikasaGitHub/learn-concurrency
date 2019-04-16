package com.akira.concurrency.utils;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * 阻塞一组线程直到某个事件的发生
 * @author Akira
 *
 */
public class CyclicBarrierDemo {
	
	Random random = new Random();
	
	public void meeting(CyclicBarrier barrier) {
		
		try {
			Thread.sleep(random.nextInt(4000));
			System.out.println(Thread.currentThread().getName() + "到达会议室, 等待开会");
			barrier.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CyclicBarrierDemo demo = new CyclicBarrierDemo();
		
		CyclicBarrier barrier = new CyclicBarrier(10, new Runnable() {
			@Override
			public void run() {
				System.out.println("开始开会！");
			}
		});
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					demo.meeting(barrier);
				}
			}).start();
		}
		
		// 监控等待线程数
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(true) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("等待的线程数 " + barrier.getNumberWaiting());
							System.out.println("is broken " + barrier.isBroken());
						}
					}
				}).start();
	}

}
