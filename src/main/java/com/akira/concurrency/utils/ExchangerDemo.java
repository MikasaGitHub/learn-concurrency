package com.akira.concurrency.utils;

import java.util.concurrent.Exchanger;

/**
 * 用于线程间数据的交换。它提供一个同步点，
 * 在这个同步点，两个线程可以交换彼此的数据。
 * 这两个线程通过exchange方法交换数据，如果第一个线程先执行exchange()方法，
 * 它会一直等待第二个线程也执行exchange方法，当两个线程都到达同步点时，
 * 这两个线程就可以交换数据，将本线程生产出来的数据传递给对方。
 * @author Akira
 *
 */
public class ExchangerDemo {
	
	public void a(Exchanger<String> exch) {
		
		System.out.println("a 方法执行");
		
		try {
			System.out.println("a 线程正在抓取数据");
			Thread.sleep(2000);
			System.out.println("a 线程抓取到的数据");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String res = "45";
		System.out.println("a 等待对比结果");
		try {
			exch.exchange(res);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void b (Exchanger<String> exch) {
		System.out.println("b 方法开始执行...");
		try {
			System.out.println("b 方法开始抓取数据...");
			Thread.sleep(4000);
			System.out.println("b 方法抓取数据结束...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String res = "12345";
		
		try {
			// 返回交换过的数据
			String value = exch.exchange(res);
			System.out.println(value);
			System.out.println("开始进行比对...");
			System.out.println("比对结果为：" + value.equals(res));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		ExchangerDemo d = new ExchangerDemo();
		Exchanger<String> exch = new Exchanger<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				d.a(exch);
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				d.b(exch);
			}
		}).start();
		
	}

}
