package cc.d_z.config.api.test;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import cc.d_z.config.api.test.constant.TestConstant;

import com.github.zkclient.ZkClient;

/**
 * 辅助测试时所用,用来对zk做操作.
 * 
 * @author davy <br>
 *         2014年4月8日 下午11:24:45 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ZookeeperOperator implements TestConstant {

	/** The client. */
	private ZkClient client = new ZkClient(ZOOKEEPER_SERVER);

	/**
	 * Read data.
	 */
	@Test
	public void readData() {
		byte[] data = client.readData(PATH);
		System.out.println(new String(data, CHARSET));
	}

	/**
	 * Write data.
	 */
	@Test
	public void writeData() {
		Stat stat = client.writeData(PATH, "{\"service_port\":\"9103\"}".getBytes(CHARSET));
		System.out.println(ToStringBuilder.reflectionToString(stat));
	}

	/**
	 * Delete data.
	 */
	public void deleteData() {
	}

	/**
	 * Creates the new path.
	 */
	@Test
	public void createNewPath() {
		client.createPersistent(TMP_PATH);
	}

	/**
	 * Delete path.
	 */
	@Test
	public void deletePath() {
		client.deleteRecursive(TMP_PATH);
	}

	/**
	 * Close.
	 */
	public void close() {
		client.close();
	}

	/**
	 * Stat.
	 */
	@Test
	public void stat() {
		Stat stat = new Stat();
		client.readData(PATH, stat);
		System.out.println(stat);
		close();
	}

	/**
	 * Update.
	 */
	public void update() {
	}
}
