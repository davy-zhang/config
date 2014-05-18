package cc.d_z.config.api;

import static org.junit.Assert.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import cc.d_z.config.api.constant.TestConstant;
import cc.d_z.config.factory.ConfigFactory;

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
		ConfigFactory.init(ZOOKEEPER_SERVER);
		config = ConfigFactory.build(PATH);

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
