package cc.d_z.config.utils.test;

import static cc.d_z.config.utils.ClientUtils.getChildPath;
import static cc.d_z.config.utils.ClientUtils.getEachOneChildPath;
import static cc.d_z.config.utils.ClientUtils.getEachOnePath;

import java.util.List;

import org.junit.Test;

import cc.d_z.config.api.test.constant.TestConstant;

import com.github.zkclient.IZkClient;
import com.github.zkclient.ZkClient;

/**
 * @author davy <br>
 *         2014年5月25日 下午5:44:16 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ClientUtilsTest implements TestConstant {
	private static final IZkClient client = new ZkClient(ZOOKEEPER_SERVER);

	/**
	 * Test method for
	 * {@link cc.d_z.config.utils.ClientUtils#getChildPath(com.github.zkclient.IZkClient, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetChildPath() {
		List<String> paths = getChildPath(client, "/");
		System.out.println(paths);
	}

	/**
	 * Test method for
	 * {@link cc.d_z.config.utils.ClientUtils#getEachOneChildPath(com.github.zkclient.IZkClient, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetEachOneChildPath() {
		List<String> paths = getEachOneChildPath(client, "/");
		System.out.println(paths);
	}

	/**
	 * Test method for
	 * {@link cc.d_z.config.utils.ClientUtils#getEachOnePath(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetEachOnePath() {
		List<String> paths = getEachOnePath("/", "/a/b/c");
		System.out.println(paths);
	}

}
