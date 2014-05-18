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
 * @author davy <br>
 *         2014年3月30日 下午2:27:52 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class Config implements Closeable {
	protected final String ROOT_PATH;
	protected final Charset CHARSET;
	protected final DZMap<String, Object> parameters = new DZMap<String, Object>();
	private static final Cache<String, Config> configCache = CacheBuilder.newBuilder().build();

	protected final ZkClient zkClient;
	protected final String path;
	protected final String zookeeperServer;
	protected final boolean showLog = BooleanUtils.toBoolean(System.getProperty("config.log.show", "true"));

	private Config(String path, String zookeeperServer, String rootPath, Charset charset) throws Exception {
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

	private void regist() {
		String ipOrHostName = ZkClientUtils.getLocalhostName();
		String port = System.getProperty("port", "0");
		zkClient.createEphemeral(path + "/~" + ipOrHostName + ":" + port);
	}

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
	 * @param zkClient
	 * @param path
	 * @throws Exception
	 */
	private void checkPath() throws Exception {
		if (!StringUtils.startsWith(this.path, ROOT_PATH))
			throw new Exception("path:" + this.path + ",路径必须是以" + ROOT_PATH + "开头");
		if (!this.zkClient.exists(path))
			throw new Exception("path:" + this.path + ",此路径不存在，请去配置管理中心配一下");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() {
		zkClient.close();
	}

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

	@Override
	public String toString() {
		return "Config [" + (parameters != null ? "parameters=" + parameters + ", " : "") + (path != null ? "path=" + path + ", " : "") + (zookeeperServer != null ? "zookeeperServer=" + zookeeperServer : "") + "]";
	}

	public static Config build(final String zookeeperServer, final String path) {
		return build(zookeeperServer, path, "/configuration", Charset.forName("UTF-8"));
	}

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
