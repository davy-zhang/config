package cc.d_z.config.utils;

import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.BooleanUtils;

/**
 * 
 * The Class Map. The default character set is UTF-8
 * 
 * <p>
 * 这个一个HashMap的子类，提供了一些方便的获取value的方式，但前提是你必须知道这个key下的value是什么类型的。<br>
 * </p>
 * 
 * @param <K>
 *            key的类型.
 * @param <V>
 *            value的类型.
 * 
 * @author davy <br>
 *         2014年3月30日 下午2:27:52 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class DZMap<K, V> extends ConcurrentHashMap<K, V> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8620925508472752103L;

	/**
	 * 构建一个新的Map.
	 * 
	 * @param map
	 *            map
	 */
	public DZMap(java.util.Map<? extends K, ? extends V> map) {
		super(map);
	}

	/**
	 * 构建一个新的Map.
	 */
	public DZMap() {
		super();
	}

	/**
	 * <p>
	 * 返回一个value的String类型的值，为空返回null。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the string value
	 */
	public String getStringValue(String key) {
		return getStringValue(key, null);
	}

	/**
	 * 返回一个value的String类型的值，为空返回defaultValue。<br>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the string value
	 */
	public String getStringValue(String key, String defaultValue) {
		Object value = get(key);
		String strValue = defaultValue;
		if (value != null) {
			strValue = value.toString();
		}
		return strValue;
	}

	/**
	 * <p>
	 * 返回一个value的boolean类型的值，为空或者转成boolean有异常时返回false。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the bool value
	 */
	public boolean getBoolValue(String key) {
		return getBoolValue(key, false);
	}

	/**
	 * 返回一个value的boolean类型的值，为空或者转成boolean有异常时返回defaultValue。<br>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the bool value
	 */
	public boolean getBoolValue(String key, boolean defaultValue) {
		return getStringValue(key) == null ? defaultValue : BooleanUtils.toBoolean(getStringValue(key));
	}

	/**
	 * <p>
	 * 返回一个value的int类型的值，为空或者解析成int有异常返回0。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the int value
	 */
	public int getIntValue(String key) {
		return getIntValue(key, 0);
	}

	/**
	 * <p>
	 * 返回一个value的int类型的值，为空或者解析成int有异常返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the int value
	 */
	public int getIntValue(String key, int defaultValue) {
		return toInt(getStringValue(key), defaultValue);
	}

	/**
	 * <p>
	 * 返回一个value的long类型的值，为空或者解析成long有异常时返回0。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the long value
	 */
	public long getLongValue(String key) {
		return getLongValue(key, 0);
	}

	/**
	 * <p>
	 * 返回一个value的long类型的值，为空或者解析成long有异常时返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the long value
	 */
	public long getLongValue(String key, long defaultValue) {
		return toLong(getStringValue(key), defaultValue);
	}

	/**
	 * <p>
	 * 返回一个value的double类型的值，为空或者解析成double有异常时返回0。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the double value
	 */
	public double getDoubleValue(String key) {
		return getDoubleValue(key, 0);
	}

	/**
	 * <p>
	 * 返回一个value的double类型的值，为空或者解析成double有异常时返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the double value
	 */
	public double getDoubleValue(String key, double defaultValue) {
		return toDouble(getStringValue(key), defaultValue);
	}

	/**
	 * <p>
	 * 返回一个value的Timestamp类型的值，为空或转成Timestamp有异常时返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the timestamp value
	 */
	public Timestamp getTimestampValue(String key, Timestamp defaultValue) {
		try {
			defaultValue = (Timestamp) get(key);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * <p>
	 * 返回一个value的Timestamp类型的值，为空或转成Timestamp有异常时返回null。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the timestamp value
	 */
	public Timestamp getTimestampValue(String key) {
		return getTimestampValue(key, null);
	}

	/**
	 * <p>
	 * 返回一个value的java.sql.Date类型的值，为空或转成java.sql.Date有异常时返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the sql date value
	 */
	public java.sql.Date getSqlDateValue(String key, java.sql.Date defaultValue) {
		try {
			defaultValue = (java.sql.Date) get(key);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * <p>
	 * 返回一个value的java.sql.Date类型的值，为空或转成java.sql.Date有异常时返回null。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the sql date value
	 */
	public java.sql.Date getSqlDateValue(String key) {
		return getSqlDateValue(key, null);
	}

	/**
	 * <p>
	 * 返回一个value的java.util.Date类型的值，为空或转成java.util.Date有异常时返回defaultValue。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the util date value
	 */
	public java.util.Date getUtilDateValue(String key, java.util.Date defaultValue) {
		try {
			defaultValue = (java.util.Date) get(key);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * <p>
	 * 返回一个value的java.util.Date类型的值，为空或转成java.util.Date有异常时返回null。<br>
	 * </p>
	 * 
	 * @param key
	 *            the key
	 * @return the util date value
	 */
	public java.util.Date getUtilDateValue(String key) {
		return getUtilDateValue(key, null);
	}

}
