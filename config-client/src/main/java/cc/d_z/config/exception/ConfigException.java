package cc.d_z.config.exception;

import com.google.common.base.Optional;

/**
 * The Class ConfigException.
 * 
 * @author davy <br>
 *         2014年4月12日 下午3:18:15 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ConfigException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 50590802577541241L;
	
	/** zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183. */
	private String zookeeperServer;
	
	/** 要订阅的路径. */
	private String path;
	
	/** 配置的参数,这是一个Map结构的Json. */
	private Optional<String> parameter;

	/**
	 * 构造器.
	 * 
	 * @param zookeeperServer
	 *            zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183.
	 * @param path
	 *            要订阅的路径.
	 * @param parameter
	 *            配置的参数,这是一个Map结构的Json.
	 */
	public ConfigException(String zookeeperServer, String path, Optional<String> parameter) {
		super();
		this.zookeeperServer = zookeeperServer;
		this.path = path;
		this.parameter = parameter;
	}

	/**
	 * 构造器.
	 * 
	 * @param zookeeperServer
	 *           zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183.
	 * @param path
	 *             要订阅的路径.
	 * @param parameter
	 *            配置的参数,这是一个Map结构的Json.
	 * @param message
	 *            异常的信息.
	 * @param cause
	 *            发生的异常.
	 */
	public ConfigException(String zookeeperServer, String path, Optional<String> parameter, String message, Throwable cause) {
		super(message, cause);
		this.zookeeperServer = zookeeperServer;
		this.path = path;
		this.parameter = parameter;
	}

	/**
	 * 构造器.
	 * 
	 * @param zookeeperServer
	 *           zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183.
	 * @param path
	 *             要订阅的路径.
	 * @param parameter
	 *            配置的参数,这是一个Map结构的Json.
	 * @param message
	 *            异常的信息.
	 */
	public ConfigException(String zookeeperServer, String path, Optional<String> parameter, String message) {
		super(message);
		this.zookeeperServer = zookeeperServer;
		this.path = path;
		this.parameter = parameter;
	}

	public String getZookeeperServer() {
		return zookeeperServer;
	}

	public String getPath() {
		return path;
	}

	public Optional<String> getParameter() {
		return parameter;
	}

}
