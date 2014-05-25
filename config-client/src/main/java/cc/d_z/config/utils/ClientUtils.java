package cc.d_z.config.utils;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.zkclient.IZkClient;

/**
 * Client工具类.
 */
public class ClientUtils {

	/**
	 * 获得当前目录下所有的子目录.
	 * 
	 * @param client
	 *            zookeeperClient.
	 * @param dirName
	 *            当前目录.
	 * @return 子目录集合.
	 */
	public static List<String> getChildPath(IZkClient client, String dirName) {
		List<String> paths = new ArrayList<String>();
		List<String> dirNames = client.getChildren(dirName);
		String splite = "/".equalsIgnoreCase(dirName) ? "" : "/";
		if (dirNames != null) {
			for (String dir : dirNames) {
				paths.add(dirName + splite + dir);
			}
		}
		return paths;
	}

	/**
	 * 获得当前目录的所有子目录,子目录下如果还有子目录,依旧迭代.
	 * 
	 * @param client
	 *            zookeeperClient.
	 * @param dirName
	 *            当前目录.
	 * @return 子目录集合.
	 */
	public static List<String> getEachOneChildPath(IZkClient client, String dirName) {
		List<String> paths = new ArrayList<String>();
		for (String path : getChildPath(client, dirName)) {
			if (isDir(client, path)) {
				paths.addAll(getEachOneChildPath(client, path));
			} else {
				paths.add(path);
			}
		}
		return paths;
	}

	/**
	 * 看此路径是否包含子节点.
	 * 
	 * @param client
	 *            zookeeperClient
	 * @param path
	 *            要查看的路径
	 * @return true, 如果含有子节点
	 */
	public static boolean isDir(IZkClient client, String path) {
		return getChildPath(client, path).size() != 0;
	}

	/**
	 * 获取当前路径的每一个路径,如当前路径是/a/b/c<br>
	 * 那么它的每一个路径为/a/b/c, /a/b, /a, /
	 * 
	 * @param rootPath
	 *            the root path
	 * @param path
	 *            the path
	 * @return the each one path
	 */
	public static List<String> getEachOnePath(String rootPath, String path) {
		ArrayList<String> paths = new ArrayList<String>();
		if (startsWith(path, rootPath)) {
			if (StringUtils.equals(path, rootPath)) {
				paths.add(path);
			} else {
				paths.add(path);
				paths.addAll(getEachOnePath(rootPath, countMatches(path, "/") == 1 ? "/" : substringBeforeLast(path, "/")));
			}
		}
		return paths;
	}

}
