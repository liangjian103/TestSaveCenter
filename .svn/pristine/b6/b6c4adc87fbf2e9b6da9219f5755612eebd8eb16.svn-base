package com.ctfo.test;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		t(20000L, 30001L);
		System.out.println(t2(20000L, 30001L));
	}

	/**
	 * 
	 * @param m 条
	 * @param n 秒
	 */
	public static void t(long m, long n) {
		if (m >= n) {
			if (m % n == 0) {
				System.out.println("每毫秒 " + m / n + "条");
				System.out.println("" + m + "/" + (m / n) + "=" + (m / (m / n)));
			} else {
				int x = (int) (m / n + 1);
				System.out.println("前" + (m - ((x * n - m) * (x - 1))) + "条，每毫秒 " + x + "条");
				long t1 = ((m - ((x * n - m) * (x - 1))) / x);
				System.out.println("" + (m - ((x * n - m) * (x - 1))) + "/" + x + "=" + t1);
				System.out.println("后" + (x * n - m) * (x - 1) + "条，每毫秒 " + (x - 1) + "条");
				long t2 = (x * n - m) * (x - 1) / (x - 1);
				System.out.println("" + (x * n - m) * (x - 1) + "/" + (x - 1) + "=" + t2);
				System.out.println("" + t1 + "+" + t2 + "=" + (t1 + t2));
			}
		} else {
			if (n % m == 0) {
				System.out.println("每 " + n / m + "毫秒1条");
				System.out.println("" + m + "*" + (n / m) + "=" + (m * (n / m)));
			} else {
				int x = (int) (n / m);
				System.out.println("前" + (n - x * m) + "条，每" + (x + 1) + "毫秒 1条");
				long t1 = (n - x * m) * (x + 1);
				System.out.println("" + (n - x * m) + "*" + (x + 1) + "=" + t1);
				System.out.println("后" + (m - (n - x * m)) + "条，每" + x + "毫秒 1条");
				long t2 = (m - (n - x * m)) * x;
				System.out.println("" + (m - (n - x * m)) + "*" + x + "=" + t2);
				System.out.println("" + t1 + "+" + t2 + "=" + (t1 + t2));
			}
		}
	}

	/**
	 * 抽取匀速规则
	 * 
	 * @param m
	 *            总耗时
	 * @param n
	 *            处理条数
	 * @return 匀速规则
	 */
	public static Map<String, String> t2(long m, long n) {
		Map<String, String> map = new HashMap<String, String>();
		if (m >= n) {
			map.put("flag", "1");// 每毫秒?条
			if (m % n == 0) {
				map.put("before", (m / n) + "");// 每毫秒?条
			} else {
				int x = (int) (m / n + 1);
				map.put("before", ((m - ((x * n - m) * (x - 1)))) + "_" + x);// 前?条，每毫秒?条
				map.put("after", ((x * n - m) * (x - 1)) + "_" + (x - 1));// 后?条，每毫秒?条
			}
		} else {
			map.put("flag", "2");// 每?毫秒1条
			if (n % m == 0) {
				map.put("before", (n / m) + "");// 每?毫秒1条
			} else {
				int x = (int) (n / m);
				map.put("before", (n - x * m) + "_" + (x + 1));// 前?条，每?毫秒1条
				map.put("after", (m - (n - x * m)) + "_" + x);// 后?条，每?毫秒1条
			}
		}
		return map;
	}

}
