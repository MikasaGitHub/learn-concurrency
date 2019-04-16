package com.akira.concurrency.threadlocal;

public class Demo {

	private ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return new Integer(0);
		}; 
	};

	public int getNext() {
		Integer value = count.get();
		value ++;
		count.set(value);
		return value;
	}
	
	/** 运行结果：每个线程都会存储自己的变量
	 * Thread 对象中有ThreadMap 属性。 
	 * ThreadMap 的key 为当前ThreadLocal对象
	 * ThreadMap 的 value 就是所set的值
	 * 获取就是获取当前线程的ThreadMap 通过key(ThreadLocal), 获取相应的值
	Thread-0---->1
	Thread-1---->1
	Thread-3---->1
	Thread-2---->1
	Thread-0---->2
	Thread-2---->2
	Thread-1---->2
	Thread-3---->2
	Thread-0---->3
	Thread-1---->3
	Thread-2---->3
	Thread-3---->3
	Thread-0---->4
	Thread-2---->4
	Thread-1---->4
	Thread-3---->4
	**/
	public static void main(String[] args) {
		Demo demo = new Demo();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName() + "---->" + demo.getNext());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName() + "---->" + demo.getNext());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName() + "---->" + demo.getNext());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println(Thread.currentThread().getName() + "---->" + demo.getNext());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
