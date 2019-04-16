package com.akira.concurrency.countdownlatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

/**
 * 读取文件将累计求和
 * @author Akira
 *
 */
public class Demo {

	private int [] num;
	
	public Demo(int line) {
		num = new int[line];
	}
	
	public void calc(String line, int index, CountDownLatch latch) {
		String [] number = line.split(","); // 拿到每一行的数
		
		int total = 0;
		for (String string : number) {
			total += Integer.parseInt(string);
		}
		
		num[index] = total; // 把此行计算的总数放到数组中
		System.out.println(Thread.currentThread().getName() + " 执行计算任务... " + line + " 结果为：" + total);
		latch.countDown();
	}
	
	/**
	 * 读取本地文件
	 * @return
	 */
	private static List<String> readFile() {
		
		List<String> contents = new ArrayList<>();
		String line = null;
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader("e:\\nums.txt"));
			while ((line = br.readLine()) != null) {
				contents.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return contents;
	}
	
	public void sum() {
		System.out.println("汇总线程开始执行... ");
		int total = 0;
		for (int i = 0; i < num.length; i++) {
			total += num[i];
		}
		System.out.println("最终的结果为：" + total);
	}
	
	public static void main(String[] args) {
		List<String> contents = readFile();
		int lineCount = contents.size();
		
		CountDownLatch latch = new CountDownLatch(lineCount);
		
		Demo demo = new Demo(lineCount);
		for (int i = 0; i < lineCount; i++) {
			final int j = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					demo.calc(contents.get(j), j, latch);
				}
			}).start();
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		demo.sum();
	}
	
}
