package com.akira.concurrency.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 
 * @author xiaoming
 * @date 2019-04-03
 *
 */
public class MyLock implements Lock {
	
	private Helper helper = new Helper();
	
	private class Helper extends AbstractQueuedSynchronizer {
		
		@Override
		protected boolean tryAcquire(int arg) {
			// 获取状态
			int state = getState();
			// 当前线程
			Thread t = Thread.currentThread();
			
			if(state ==0) {
				// 状态为0 可以拿到锁
				if(compareAndSetState(0, arg)) {
					setExclusiveOwnerThread(t);
					return true;
				}
				// 锁的可重入。如果还是当前线程，依然可以拿到锁
			} else if(getExclusiveOwnerThread() == t) {
				setState(state + 1);
				return true;
			}
			 return false;
		}
		
		@Override
		protected boolean tryRelease(int arg) {
			// 不是当前线程 抛异常
			if(Thread.currentThread() != getExclusiveOwnerThread()) {
				throw new RuntimeException();
			}
			
			// 是当前线程 将当前的状态-1
			int state = getState() - arg;
			
			boolean flag = false;
			
			// 如果为0 说明没有线程拿锁，所有锁已经释放。可以将当前线程置为空
			if(state == 0) {
				setExclusiveOwnerThread(null);
				flag = true;
			}
			
			setState(state);
			
			return flag;
		}
		
		Condition newCondition() {
			return new ConditionObject();
		}
	}
	

	@Override
	public void lock() {
		helper.acquire(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		helper.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return helper.tryAcquire(1); 
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return helper.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		helper.release(1);
	}

	@Override
	public Condition newCondition() {
		return helper.newCondition();
	}

}
