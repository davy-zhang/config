package cc.d_z.config.api;

import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import cc.d_z.config.api.constant.TestConstant;
import cc.d_z.config.utils.ClientUtils;

import com.github.zkclient.ZkClient;

/**
 * @author davy <br>
 *         2014年4月8日 下午11:24:45 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ZookeeperOperator implements TestConstant {
	private ZkClient client = new ZkClient(ZOOKEEPER_SERVER);

	@Test
	public void readData() {
		byte[] data = client.readData(PATH);
		System.out.println(new String(data, CHARSET));
	}

	@Test
	public void writeData() {
		Stat stat = client.writeData(PATH, "{\"service_port\":\"9103\"}".getBytes(CHARSET));
		System.out.println(ClientUtils.statToString(stat));
	}

	public void deleteData() {
	}

	@Test
	public void createNewPath() {
		client.createPersistent(TMP_PATH);
	}

	@Test
	public void deletePath() {
		client.deleteRecursive(TMP_PATH);
	}

	public void close() {
		client.close();
	}

	@Test
	public void stat() {
		Stat stat = new Stat();
		client.readData(PATH, stat);
		System.out.println(stat);
		close();
	}

	public void update() {
	}
}
