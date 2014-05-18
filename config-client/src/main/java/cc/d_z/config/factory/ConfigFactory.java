package cc.d_z.config.factory;

import java.util.concurrent.Callable;

import cc.d_z.config.api.Config;
import cc.d_z.config.exception.ConfigException;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author davy <br>
 *         2014年4月8日 下午10:02:11 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ConfigFactory {
	private static Cache<String, Config> configCache = CacheBuilder.newBuilder().build();
	private static String zookeeperServer;

	public static Config build(final String path) {
		Config config = null;
		try {
			config = configCache.get(path, new Callable<Config>() {
				@Override
				public Config call() throws Exception {
					return new Config(path, zookeeperServer);
				}
			});
		} catch (Exception e) {
			Optional<String> parameter = Optional.fromNullable(null);
			throw new ConfigException(zookeeperServer, path, parameter, "创建Config对象失败", e);
		}
		return config;
	}

	public static void init(String zookeeperServer) {
		ConfigFactory.zookeeperServer = zookeeperServer;
	}
}
