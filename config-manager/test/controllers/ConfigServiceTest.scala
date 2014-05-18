package controllers

import org.junit.runner.RunWith
import org.junit.Test

/**
 * @author davy <br>
 *         2014年5月8日 下午10:50:18 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
class ConfigServiceTest {
  val sevice=new ConfigService

  
  @Test
  def getTreeNodes(){
	  println(sevice.getTreeNodes)
  }
}