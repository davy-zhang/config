package cc.d_z.config.exception;

import com.google.common.base.Optional;

/**
 * @author davy <br>
 *         2014年4月12日 下午3:18:15 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 50590802577541241L;
	private String zookeeperServer;
	private String path;
	private Optional<String> parameter;

	public ConfigException(String zookeeperServer, String path, Optional<String> parameter) {
		super();
		this.zookeeperServer = zookeeperServer;
		this.path = path;
		this.parameter = parameter;
	}

	public ConfigException(String zookeeperServer, String path, Optional<String> parameter, String message, Throwable cause) {
		super(message, cause);
		this.zookeeperServer = zookeeperServer;
		this.path = path;
		this.parameter = parameter;
	}

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
