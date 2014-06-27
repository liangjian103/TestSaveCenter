package com.ctfo.test;

import com.ctfo.manage.DataEnum;

public class TestEnumSleep {

	public static void main(String[] args) throws InterruptedException {
		double sleep = 30000.0 / (200000/ 4);
		long millis = (long) sleep;// 毫秒
		double millis2 = sleep - millis;
		int nanos = (int) (millis2 * 1000 * 1000);// 纳秒
		System.out.println("millis:"+millis+"--nanos:"+nanos);
		
		long time = System.nanoTime();
		System.out.println(time);
		long time2 = System.currentTimeMillis();
		//1000125
		//0-999999
		for (int i = 0; i < 5000; i++) {
			Thread.sleep(1, 999999);
		}
//		System.out.println((System.nanoTime()-time)+"nano");
		System.out.println((System.currentTimeMillis()-time2)+"ms");
		
//		System.out.println(DataEnum.DataTypeSevice.REG_DATA.getSendDataSleep());
//		System.out.println(DataEnum.DataTypeSevice.TRACK_DATA.getSendDataSleep());
//		System.out.println(DataEnum.DataTypeSevice.TRACK_ALARM_DATA.getSendDataSleep());
		
//		for (int i = 0; i < 1000; i++) {
//			test();
//		}
//		long startTime = System.currentTimeMillis();
//		DataEnum.DataTypeSevice.REG_DATA.sendSleep(3000);
//		System.out.println("AAA" + (System.currentTimeMillis() - startTime));
	}

	public static void test() {
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 10000; i++) {
					long startTime = System.currentTimeMillis();
					DataEnum.DataTypeSevice.REG_DATA.sendSleep(10000);
					System.out.println("BBB" + (System.currentTimeMillis() - startTime));
				}
			}

		}.start();
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 10000; i++) {
					long startTime = System.currentTimeMillis();
					DataEnum.DataTypeSevice.REG_DATA.sendSleep(5000);
					System.out.println("CCC" + (System.currentTimeMillis() - startTime));
				}
			}

		}.start();
	}

}
