package cc.d_z.config.api.test.constant;

import java.nio.charset.Charset;

/**
 * 测试时用的一些常量.
 * 
 * @author davy <br>
 *         2014年4月8日 下午11:25:40 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public interface TestConstant {

	/** The zookeeper server. */
	String ZOOKEEPER_SERVER = "me:2181,me:2182,me:2183";

	/** The path. */
	String PATH = "/configuration/dubboservice/idmapping";

	/** The root path. */
	String ROOT_PATH = "/configuration";

	/** The tmp path. */
	String TMP_PATH = PATH + "/~tmp";

	/** The charset. */
	Charset CHARSET = Charset.forName("UTF-8");
}
