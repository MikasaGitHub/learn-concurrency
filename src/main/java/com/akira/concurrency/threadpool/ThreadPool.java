package com.akira.concurrency.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的使用
 * @author xiaoming
 * 
ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler)


 * 第一个参数:corePoolSize,核心线程数,默认情况下核心线程会一直存活,即使处于闲置状态下也不会受keepAliveTime限制。除非将allowCoreThreadTimeOUt设置为true
 * 第二个参数:maximumPoolSize 线程池所能容纳的最大线程数。超过这个数量的线程将会被阻塞。当任务队列为没有设置大小的LinkedBlockingQueue时，这个值无效
 * 第三个参数:keepAliveTime,非核心线程的闲置超时时间，超过这个时间就会被回收。
 * 第四个参数:unit,指定keepAliveTime的单位,如TimeUnit.SECONDS。当将allowCoreThreadTimeOut设置为true时对corePoolSize生效。
 * 第五个参数:workQueue 线程中的任务队列，常用的有三种队列，SynchronousQueue,LinkedBlockingDeque,ArrayBlockingQueue。
 * 第六个参数:threadFactory 线程工厂，提供创建新线程的功能。ThreadFactory是一个接口，只有一个方法
 * 第七个参数:RejectedExecutionHandler，当线程池中的资源已经全部使用，添加新线程被拒绝时，会调用RejectedExecutionHandler的rejectedExecution方法。
 * 
 * 
 * 
 * 
 *  线程池的线程执行规则跟任务队列有很大的关系。
 *
 *	下面都假设任务队列没有大小限制：
	
	如果线程数量<=核心线程数量，那么直接启动一个核心线程来执行任务，不会放入队列中。
	如果线程数量>核心线程数，但<=最大线程数，并且任务队列是LinkedBlockingDeque的时候，超过核心线程数量的任务会放在任务队列中排队。
	如果线程数量>核心线程数，但<=最大线程数，并且任务队列是SynchronousQueue的时候，线程池会创建新线程执行任务，这些任务也不会被放在任务队列中。这些线程属于非核心线程，在任务完成后，闲置时间达到了超时时间就会被清除。
	如果线程数量>核心线程数，并且>最大线程数，当任务队列是LinkedBlockingDeque，会将超过核心线程的任务放在任务队列中排队。也就是当任务队列是LinkedBlockingDeque并且没有大小限制时，线程池的最大线程数设置是无效的，他的线程数最多不会超过核心线程数。
	如果线程数量>核心线程数，并且>最大线程数，当任务队列是SynchronousQueue的时候，会因为线程池拒绝添加任务而抛出异常。
	任务队列大小有限时
	
	当LinkedBlockingDeque塞满时，新增的任务会直接创建新线程来执行，当创建的线程数量超过最大线程数量时会抛异常。
	SynchronousQueue没有数量限制。因为他根本不保持这些任务，而是直接交给线程池去执行。当任务数量超过最大线程数时会直接抛异常。

 * 
 * 
 */
public class ThreadPool {
	
	
	public static void main(String[] args) {
				
		Runnable myRunnable = new Runnable() {
		    @Override
		    public void run() {
		        try {
		            Thread.sleep(2000);
		            System.out.println(Thread.currentThread().getName() + " run");
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }

		    }
		};
		
		test4(myRunnable);
		
	}
	
	/**
	 * 核心线程数为6，最大线程数为10。超时时间为5秒
	 * 
	 * result:
	 *  ---先开三个---
		核心线程数6
		线程池数3
		队列任务数0
		---再开三个---
		核心线程数6
		线程池数6
		队列任务数0
		pool-1-thread-2 run
		pool-1-thread-1 run
		pool-1-thread-3 run
		pool-1-thread-4 run
		pool-1-thread-6 run
		pool-1-thread-5 run
		----8秒之后----
		核心线程数6
		线程池数6
		队列任务数0
		
		每个任务都是是直接启动一个核心线程来执行任务，
		一共创建了6个线程，不会放入队列中。
		8秒后线程池还是6个线程，核心线程默认情况下不会被回收，不收超时时间限制。
	 * 
	 */
	public static void test1(Runnable myRunnable) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 10, 5, TimeUnit.SECONDS,  new SynchronousQueue<Runnable>());
		
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---先开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---再开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("----8秒之后----");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
	}
	
	/**
	 * ---先开三个---
		核心线程数3
		线程池数3
		队列任务数0
		---再开三个---
		核心线程数3
		线程池数3
		队列任务数3
		pool-1-thread-3 run
		pool-1-thread-1 run
		pool-1-thread-2 run
		pool-1-thread-3 run
		pool-1-thread-2 run
		pool-1-thread-1 run
		----8秒之后----
		核心线程数3
		线程池数3
		队列任务数0
	 * @param myRunnable
	 * 
	 * 当任务数超过核心线程数时，会将超出的任务放在队列中，只会创建3个线程重复利用。
	 */
	public static void test2(Runnable myRunnable) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---先开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---再开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("----8秒之后----");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		
	}
	
	/**
	 *  
	 *  ---先开三个---
		核心线程数3
		线程池数3
		队列任务数0
		---再开三个---
		核心线程数3
		线程池数6
		队列任务数0
		pool-1-thread-3 run
		pool-1-thread-1 run
		pool-1-thread-2 run
		pool-1-thread-4 run
		pool-1-thread-6 run
		pool-1-thread-5 run
		----8秒之后----
		核心线程数3
		线程池数3
		队列任务数0
	 *  
	 *  当队列是SynchronousQueue时，超出核心线程的任务会创建新的线程来执行，看到一共有6个线程。
	 *  但是这些线程是费核心线程，收超时时间限制，在任务完成后限制超过5秒就会被回收。所以最后看到线程池还是只有三个线程。
	 *  
	 * @param myRunnable
	 */
	public static void test3(Runnable myRunnable) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---先开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---再开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("----8秒之后----");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
	}
	
	/**
	 * LinkedBlockingDeque根本不受最大线程数影响。
	 * 但是当LinkedBlockingDeque有大小限制时就会受最大线程数影响
	 * 
	 * 
	 * @param myRunnable
	 */
	public static void test4(Runnable myRunnable) {
		// 不受影响 ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		// 设置大小限制 会有影响
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(2));
		/**
		 * 首先为三个任务开启了三个核心线程1，2，3，
		 * 然后第四个任务和第五个任务加入到队列中，第六个任务因为队列满了，
		 * 就直接创建一个新线程4，这是一共有四个线程，没有超过最大线程数。
		 * 8秒后，非核心线程收超时时间影响回收了，因此线程池只剩3个线程了。
		 */
		
		// ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1));
		// 报错 直接出错在第6个execute方法上。
		// 因为核心线程是3个，当加入第四个任务的时候，就把第四个放在队列中。
		// 加入第五个任务时，因为队列满了，就创建新线程执行，创建了线程4。
		// 当加入第六个线程时，也会尝试创建线程，但是因为已经达到了线程池最大线程数，所以直接抛异常了。
		
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---先开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		executor.execute(myRunnable);
		System.out.println("---再开三个---");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("----8秒之后----");
		System.out.println("核心线程数" + executor.getCorePoolSize());
		System.out.println("线程池数" + executor.getPoolSize());
		System.out.println("队列任务数" + executor.getQueue().size());
	}
	
	public static void test5(Runnable thread) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 5, TimeUnit.SECONDS, new SynchronousQueue<>());
		
		executor.execute(thread);
	}
}
