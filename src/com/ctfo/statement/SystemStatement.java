package com.ctfo.statement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.ctfo.util.PropertiesUtil;

/**
 * Title: 平台常量配置类
 * 
 * @author James
 */
public class SystemStatement {

	// public static final List<String> ADDIN_CLASS =
	// Arrays.asList("getCarDataInfoReq","putAutoDataScheduleReq","getOrderFormInfoReq","getTransportOrderFormInfoReq");

	/** 配置文件名称 */
	public static String SYSTEM_PROPERTIES = "system.properties";
	/** SQL配置文件名称 */
	public static String SQL_PROPERTIES = "sql.properties";

	/** 当前服务管理端口 */
	public static int LOCALHOST_MANAGE_PORT = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "localhost_manage_port"));

	/** 当前服务数据推送端口 */
	public static int LOCALHOST_PORT = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "localhost_port"));

	/** 开始手机号 */
	public static Long START_SIM = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "start_sim"));

	/** 结束手机号 */
	public static Long END_SIM = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SYSTEM_PROPERTIES, "end_sim"));

	/** 每个队列的长度 */
	public static int QUEUE_SIZE = Integer.parseInt(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "queue_size"));

	/** 多少毫秒发送一次注册 */
	public static long SEND_REG_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_reg_data_sleep"));
	/** 多少毫秒发送一次鉴权 */
	public static long SEND_AUTH_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_auth_data_sleep"));
	/** 多少毫秒发送一次上线 */
	public static long SEND_ISONLINE_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_isonline_data_sleep"));
	/** 多少毫秒发送一次下线 */
	public static long SEND_ISOFFLINE_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_isoffline_data_sleep"));
	/** 多少毫秒发送一次位置 */
	public static long SEND_TRACK_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_track_data_sleep"));
	/** 多少毫秒发送一次报警 */
	public static long SEND_TRACK_ALARM_DATA_SLEEP = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_track_alarm_data_sleep"));

	/** 每个车上报多少次位置 */
	public static long SEND_TRACK_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_track_size"));

	/** 每个车上报多少次报警 */
	public static long SEND_TRACK_ALARM_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_track_alarm_size"));

	/** 每个车上报多少次注册 */
	public static long SEND_REG_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_reg_size"));

	/** 每个车上报多少次鉴权 */
	public static long SEND_AUTH_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_auth_size"));

	/** 每个车上报多少次上线 */
	public static long SEND_ISONLINE_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_isonline_size"));

	/** 每个车上报多少次下线 */
	public static long SEND_ISOFFLINE_SIZE = Long.parseLong(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_isoffline_size"));

	/** 所有车辆信息 */
	public static List<Map<String, Object>> RS_VEHICLE = null;

	/** 服务启动开关队列 */
	public static ArrayBlockingQueue<String> SERVER_RUN = new ArrayBlockingQueue<String>(1);

	/** 当前服务器CPU个数 */
	public static int SYS_CPU_COUNT = Runtime.getRuntime().availableProcessors();

	/** 报警编码 */
	public static String ALARM_CODE = PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "alarm_code");

	/** 是否允许动态添加或移除客户端并将数据振荡继续分发(true:允许,false:不允许) */
	public static boolean SYS_DYNAMIC = Boolean.parseBoolean(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "sys_dynamic"));

	/** 是否允许限速,限速后会将数据按上报时间间隔匀速推送数据(true:允许,false:不允许) */
	public static boolean SEND_AVERAGE_SLEEP = Boolean.parseBoolean(PropertiesUtil.PROPERTIES.readResource(SystemStatement.SYSTEM_PROPERTIES, "send_average_sleep"));
}
