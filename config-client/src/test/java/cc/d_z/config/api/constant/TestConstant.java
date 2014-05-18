package cc.d_z.config.api.constant;

import java.nio.charset.Charset;

/**
 * @author davy <br>
 *         2014年4月8日 下午11:25:40 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public interface TestConstant {
	String ZOOKEEPER_SERVER = "me:2181,me:2182,me:2183";
	String PATH = "/configuration/dubboservice/idmapping";
	String ROOT_PATH = "/configuration";
	String TMP_PATH = PATH+"/~tmp";
	Charset CHARSET = Charset.forName("UTF-8");
}
