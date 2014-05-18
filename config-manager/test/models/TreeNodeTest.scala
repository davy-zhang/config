package models

import org.junit.Test

import utils.MarshallableImplicits._
/**
 * @author davy <br>
 *         2014年5月8日 下午11:28:30 <br>
 *         <B>The default encoding is UTF-8 </B><br>
 *         email: davy@d-z.cc<br>
 *         <a href="http://d-z.cc">d-z.cc</a><br>
 */
class TreeNodeTest {
  val treeNode = new TreeNode("/configuration", "0", "/configuration")

  @Test
  def toJson() {
//    val mapper = new ObjectMapper() with ScalaObjectMapper
//    mapper.registerModule(DefaultScalaModule)
//        val json = mapper.writeValueAsString(treeNode)
    val json=treeNode.toJson
    println(json)
  }
}