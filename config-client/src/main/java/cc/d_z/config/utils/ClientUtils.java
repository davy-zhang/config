package cc.d_z.config.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.zookeeper.data.Stat;

import com.github.zkclient.IZkClient;

public class ClientUtils {

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

	public static boolean isDir(IZkClient client, String path) {
		return getChildPath(client, path).size() != 0;
	}

	public static String getMinPath(IZkClient client, String path) {
		String tp = "/".equals(path) ? "/" : substringBeforeLast(path, "/");
		String minPath = null;
		for (String p : getChildPath(client, tp)) {
			minPath = minPath == null ? p : NumberUtils.toLong(p) > NumberUtils.toLong(minPath) ? minPath : p;
		}
		return minPath;
	}

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

	public static String statToString(Stat stat) {
		StringBuffer buffer = new StringBuffer();
		Field[] fields = Stat.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				buffer.append(field.getName()).append("=").append(FieldUtils.readField(field, stat, true)).append(",");
			}
		} catch (IllegalAccessException e) {
			buffer.append("statToString发生异常");
		}
		return substringBeforeLast(buffer.toString(), ",");
	}
}
