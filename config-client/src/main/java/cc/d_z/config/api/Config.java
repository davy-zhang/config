package cc.d_z.config.api;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import cc.d_z.config.exception.ConfigException;
import cc.d_z.config.utils.ClientUtils;
import cc.d_z.config.utils.DZMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;
import com.github.zkclient.ZkClientUtils;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 配置核心类.
 * 
 * @author davy <br>
 *         2014年3月30日 下午2:27:52 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class Config implements Closeable {

	/** 配置的根路径,默认是/configuration */
	protected final String ROOT_PATH;

	/** 配置的字符集,默认是UTF-8. */
	protected final Charset CHARSET;

	/** 配置的参数,在使用过程中请不要对它进行修改. */
	protected final DZMap<String, Object> parameters = new DZMap<String, Object>();

	/** 配置类的缓存. */
	private static final Cache<String, Config> configCache = CacheBuilder.newBuilder().build();

	/** zookeeper客户端. */
	protected final ZkClient zkClient;

	/** 要订阅的路径. */
	protected final String path;

	/** zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183. */
	protected final String zookeeperServer;

	/**
	 * 当配置有变化或者第一次订阅配置时,是否在控制台输出配置,-Dconfig.log.show=true为输出,-Dconfig.log.show=
	 * false为不输出,默认为输出.
	 */
	protected final boolean showLog = BooleanUtils.toBoolean(System.getProperty("config.log.show", "true"));

	/**
	 * Config的构造器,因为每个Config实例都会有与zookeeper的链接和配置的缓存,所以对于同一个订阅路径的配置类请使用同一个实例.
	 * 
	 * @param path
	 *            要订阅的路径
	 * @param zookeeperServer
	 *            zookeeper的地址
	 * @param rootPath
	 *            配置的根路径
	 * @param charset
	 *            配置的字符集
	 */
	private Config(String path, String zookeeperServer, String rootPath, Charset charset) {
		this.path = path;
		this.zookeeperServer = zookeeperServer;
		this.ROOT_PATH = rootPath;
		this.CHARSET = charset;
		this.zkClient = new ZkClient(zookeeperServer);
		checkPath();
		subscribe();
		load();
		regist();
	}

	/**
	 * 在zookeeper中注册一个临时节点,znode的名字为hostname:index,hostname为自动获取或者-Dzkclient.
	 * hostname.overwritten指定,index为-Dindex指定.
	 */
	private void regist() {
		String ipOrHostName = ZkClientUtils.getLocalhostName();
		String index = System.getProperty("index", "0");
		zkClient.createEphemeral(path + "/~" + ipOrHostName + ":" + index);
	}

	/**
	 * 订阅,当订阅的路径的数据变化了,自动刷新配置.
	 */
	private void subscribe() {
		List<String> paths = ClientUtils.getEachOnePath(ROOT_PATH, path);
		for (String _path : paths) {
			this.zkClient.subscribeDataChanges(_path, new IZkDataListener() {
				@Override
				public void handleDataChange(String dataPath, byte[] data) throws Exception {
					load();
				}

				@Override
				public void handleDataDeleted(String dataPath) throws Exception {
					load();
				}

			});
		}
	}

	/**
	 * 检测路径,路径必须以ROOT_PATH开头并且路径必须存在.
	 * 
	 * @throws cc.d_z.config.exception.ConfigException
	 *             检测失败会抛出cc.d_z.config.exception.ConfigException
	 */
	private void checkPath() {
		if (!StringUtils.startsWith(this.path, ROOT_PATH))
			throw new ConfigException(this.zookeeperServer, this.path, Optional.fromNullable((String) null), "路径必须是以" + ROOT_PATH + "开头");
		if (!this.zkClient.exists(path))
			throw new ConfigException(this.zookeeperServer, this.path, Optional.fromNullable((String) null), "此路径不存在，请去配置管理中心配一下");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.zkclient.ZkClient#close()
	 */
	@Override
	public void close() {
		zkClient.close();
	}

	/**
	 * 加载配置,每次调用都将重新获取配置.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void load() {
		List<String> paths = ClientUtils.getEachOnePath(ROOT_PATH, path);
		Collections.reverse(paths);
		ObjectMapper mapper = new ObjectMapper();
		for (String _path : paths) {
			byte[] data = this.zkClient.readData(_path);
			if (ArrayUtils.isNotEmpty(data)) {
				Optional<String> parameter = Optional.of(new String(data, CHARSET));
				try {
					Map<String, Object> parameterMap = mapper.readValue(parameter.get(), Map.class);
					this.parameters.clear();
					this.parameters.putAll(parameterMap);
				} catch (Exception e) {
					throw new ConfigException(zookeeperServer, _path, parameter, "获取配置时出错", e);
				}
			}
		}
		log(zookeeperServer, "中的", path, "下的配置是:", parameters);
	}

	/**
	 * 打印日志.
	 * 
	 * @param logs
	 *            要打印的日志
	 */
	private void log(Object... logs) {
		if (showLog) {
			StringBuffer logBuffer = new StringBuffer();
			for (Object log : logs) {
				logBuffer.append(log);
			}
			System.err.println(logBuffer);
		}
	}

	public DZMap<String, Object> getParameters() {
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Config [" + (parameters != null ? "parameters=" + parameters + ", " : "") + (path != null ? "path=" + path + ", " : "") + (zookeeperServer != null ? "zookeeperServer=" + zookeeperServer : "") + "]";
	}

	/**
	 * @see cc.d_z.config.api.Config#build(String, String, String, Charset)
	 * 
	 * 如果你的要使用默认的根路径,和默认的字符集,请调用此方法.
	 * 
	 * @param zookeeperServer
	 *           zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183
	 * @param path
	 *            要订阅的路径
	 * @return the config
	 */
	public static Config build(final String zookeeperServer, final String path) {
		return build(zookeeperServer, path, "/configuration", Charset.forName("UTF-8"));
	}

	/**
	 * 因为每个Config实例都会有与zookeeper的链接和配置的缓存,所以对于同一个订阅路径的配置类请使用同一个实例.<br>
	 * 使用此方法将根据要订阅的路径自动返回已存在的或新的Config实例.
	 * 
	 * @param zookeeperServer
	 *           zookeeper的地址,如127.0.0.1:2182,127.0.0.1:2182,127.0.0.1:2183
	 * @param path
	 *            要订阅的路径
	 * @param rootPath
	 *            配置的根路径
	 * @param charset
	 *            配置的字符集
	 * @return the config
	 * 			  Config实例
	 */
	public static Config build(final String zookeeperServer, final String path, final String rootPath, final Charset charset) {
		Config config = null;
		try {
			config = configCache.get(path, new Callable<Config>() {
				@Override
				public Config call() throws Exception {
					return new Config(path, zookeeperServer, rootPath, charset);
				}
			});
		} catch (Exception e) {
			Optional<String> parameter = Optional.fromNullable(null);
			throw new ConfigException(zookeeperServer, path, parameter, "创建Config对象失败", e);
		}
		return config;

	}

}
