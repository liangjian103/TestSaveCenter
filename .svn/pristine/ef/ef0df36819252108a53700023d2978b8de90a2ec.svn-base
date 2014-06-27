package com.ctfo.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctfo.statement.SystemStatement;

/**
 * 多种指令数据枚举(协议数据组装)
 * 
 * @author James Date:2014-3-18 11:36:58
 */
public interface DataEnum {
	public String buildCommand(Map<String, Object> map);

	/** (协议数据组装-业务类) */
	enum DataTypeSevice implements DataEnum {
		/** 注册指令数据 */
		REG_DATA(SystemStatement.SEND_REG_SIZE, SystemStatement.SEND_REG_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_0 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:36,40:23,41:23,202:2,104:");
				sb.append(map.get("VEHICLE_NO"));
				sb.append(",42:70104,43:ZD-V02H,44:");
				sb.append(map.get("TMAC"));
				sb.append(",45:0}");
				return sb.toString();
			}
		},
		/** 鉴权指令数据 */
		AUTH_DATA(SystemStatement.SEND_AUTH_SIZE, SystemStatement.SEND_AUTH_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_0 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:38,47:7473834802,48:1}");
				return sb.toString();
			}
		},
		/** 上线指令数据 */
		ISONLINE_DATA(SystemStatement.SEND_ISONLINE_SIZE, SystemStatement.SEND_ISONLINE_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_0 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:5,18:1/0/0/10002}");
				return sb.toString();
			}
		},
		/** 下线指令数据 */
		ISOFFLINE_DATA(SystemStatement.SEND_ISOFFLINE_SIZE, SystemStatement.SEND_ISOFFLINE_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_0 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:5,18:1/0/0/10002}");
				return sb.toString();
			}
		},
		/** 位置指令数据 */
		TRACK_DATA(SystemStatement.SEND_TRACK_SIZE, SystemStatement.SEND_TRACK_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				SimpleDateFormat gpsDateFormat = new SimpleDateFormat("yyyyMMdd/HHmmss");
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_1 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:0,RET:0,1:69807550,2:23986841,20:0,24:100,3:600,4:").append(gpsDateFormat.format(new Date()));
				sb.append(",5:30,6:500,7:600,8:528387,9:15200,999:").append(System.currentTimeMillis() / 1000).append("}");
				return sb.toString();
			}
		},
		/** 位置报警指令数据 */
		TRACK_ALARM_DATA(SystemStatement.SEND_TRACK_ALARM_SIZE, SystemStatement.SEND_TRACK_ALARM_DATA_SLEEP) {
			public String buildCommand(Map<String, Object> map) {
				SimpleDateFormat gpsDateFormat = new SimpleDateFormat("yyyyMMdd/HHmmss");
				StringBuffer sb = new StringBuffer();
				sb.append("CAITS 0_1 ").append(map.get("OEM_CODE")).append("_").append(map.get("COMMADDR"));
				sb.append(" 0 U_REPT {TYPE:0,RET:0,1:69807550,2:23986841,20:");
				sb.append(SystemStatement.ALARM_CODE);// 报警编码2:超速,4:疲劳,536870912:碰撞,536870918:疲劳+超速+碰撞
				sb.append(",24:100,3:600,4:").append(gpsDateFormat.format(new Date()));
				sb.append(",5:30,6:500,7:600,8:528387,9:15200,999:").append(System.currentTimeMillis() / 1000).append("}");
				return sb.toString();
			}
		};

		private static Logger logger = Logger.getLogger(DataTypeSevice.class);

		/** 每个车上报某指令的次数 */
		private final long sendDataSize;
		/** 每个车上报位置后休眠时间 */
		private final long sendDataSleep;

		/** 构造方法: 每个车上报多少次当前类型的位置,每个车上报位置后休眠时间 */
		private DataTypeSevice(long sendDataSize, long sendDataSleep) {
			this.sendDataSize = sendDataSize;
			this.sendDataSleep = sendDataSleep;
		}

		/** 返回每个车上报某指令的次数 */
		public long getSendDataSize() {
			return sendDataSize;
		}

		/** 返回每个车上报位置后休眠时间 */
		public long getSendDataSleep() {
			return sendDataSleep;
		}

		/** 休眠(不同指令数据休眠时间不同) */
		public void sendSleep() {
			try {
				Thread.sleep(this.sendDataSleep);
			} catch (Exception e) {
				logger.error("Thread.sendSleep " + this.name() + " ERROR!", e);
			}
		}

		/** 休眠(不同指令数据休眠时间不同) */
		public void sendSleep(long sendAverageSleep) {
			try {
				Thread.sleep(sendAverageSleep);
			} catch (Exception e) {
				logger.error("Thread.sendAverageSleep " + this.name() + " ERROR!", e);
			}
		}
	}
}
