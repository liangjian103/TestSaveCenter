package com.ctfo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ctfo.statement.SystemStatement;

/**
 * 读取配置文件信息
 * 
 * @author LiangJian 2012-10-13 16:56:51
 */
public enum PropertiesUtil {
	// 枚举方式单例模式
	PROPERTIES;
	private PropertiesUtil() {
	}

	/**
	 * 获取资源配置文件中的值
	 * 
	 * @param path
	 *            配置文件名称
	 * @param key
	 *            配置文件中的key
	 * @return
	 */
	public String readResource(String path, String key) {
		// 读取配置文件
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
		Properties p = new Properties();
		
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 取得配置文件中的值
		return p.getProperty(key);
	}
	
	/**
	 * 获取指定配置文件中的值
	 * 
	 * @param path
	 *            配置文件路径及名称
	 * @param key
	 *            配置文件中的key
	 * @return
	 */
	public String readPhysicalAddress(String path, String key) {
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 取得配置文件中的值
		return p.getProperty(key);
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String value = PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "localhost_port");
			System.out.println(value);
			Thread.sleep(5000);
		}
		
	}
}
