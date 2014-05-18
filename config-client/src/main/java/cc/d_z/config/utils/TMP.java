package cc.d_z.config.utils;

import com.google.common.base.Optional;

/**
 * @author davy <br>
 *         2014年4月12日 下午3:32:20 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
public class TMP {
	public static void main(String[] args) {
		Optional<String> o=Optional.fromNullable("s");
		System.out.println(o.isPresent());
		System.out.println(o.get());
	}
}
