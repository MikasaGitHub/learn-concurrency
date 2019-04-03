package lock;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public class DemoSeq {

	private MyLock lock = new MyLock();
	
	private int value;
	
	public int getNext() {
		lock.lock();
		value++;
		lock.unlock();
		return value;
	}
	
	public static void main(String[] args) {
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		DemoSeq s = new DemoSeq();
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					System.out.println(s.getNext());
				}
			}
		});
		
		for (int i = 0; i < 10; i++) {
			executor.execute(thread);
		}
		
	}
}
