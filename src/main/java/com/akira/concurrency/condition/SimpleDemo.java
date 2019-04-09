package com.akira.concurrency.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * condition的作用
 * @author xiaoming
 *
 */
public class SimpleDemo {
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Condition a = lock.newCondition();
	private Condition b = lock.newCondition();
	private Condition c = lock.newCondition();
	
	private int signal;
	
	public void a() {
		lock.lock();
		while(signal != 0) {
			try {
				a.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("a");
		signal ++;
		b.signal();
		lock.unlock();
	}

	public void b() {
		lock.lock();
		while(signal != 1) {
			try {
				b.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("b");
		signal ++;
		c.signal();
		lock.unlock();
	}
	
	public void c() {
		lock.lock();
		while(signal != 2) {
			try {
				c.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("c");
		signal = 0;
		a.signal();
		lock.unlock();
	}
	
	static class A implements Runnable {
		
		private SimpleDemo demo;
		
		public A(SimpleDemo demo) {
			this.demo = demo;
		}

		@Override
		public void run() {
			while (true) {
				demo.a();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class B implements Runnable {
		
		private SimpleDemo demo;
		
		public B(SimpleDemo demo) {
			this.demo = demo;
		}

		@Override
		public void run() {
			while (true) {
				demo.b();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class C implements Runnable {
		
		private SimpleDemo demo;
		
		public C(SimpleDemo demo) {
			this.demo = demo;
		}

		@Override
		public void run() {
			while (true) {
				demo.c();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		SimpleDemo demo = new SimpleDemo();
		
		A a = new A(demo);
		B b = new B(demo);
		C c = new C(demo);
		
		new Thread(a).start();
		new Thread(b).start();
		new Thread(c).start();
		
	}
}
