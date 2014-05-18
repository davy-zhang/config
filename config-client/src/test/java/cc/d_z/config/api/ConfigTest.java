package cc.d_z.config.api;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import cc.d_z.config.api.constant.TestConstant;

/**
 * @author davy <br>
 *         2014年4月8日 下午11:20:10 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class ConfigTest implements TestConstant {

	private Config config;
	private static final AtomicBoolean stop = new AtomicBoolean();

	@Before
	public void init() {
		config = Config.build(ZOOKEEPER_SERVER, PATH);

	}

	@Test
	public void stop() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Scanner(System.in).nextLine();
				stop.set(true);
			}
		}).start();
	}

	@Test
	public void getParameter() {
		for (;!stop.get();) {
			System.out.println(config.getParameters().get("service_port"));
			sleep();
		}
		close();
	}

	public void close() {
		config.close();
	}

	private void sleep() {
		try {
			TimeUnit.SECONDS.sleep(1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
