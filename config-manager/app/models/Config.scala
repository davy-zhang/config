/**
 *
 */
package models

/**
 * @author davy
 *
 */
case class Config(path: String, selfParameterKeys: List[String], selfParameterValues: List[String]) {
  var parentParameters: Option[Map[String, String]]=None
  var selfParameters: Option[Map[String, String]]=None
  var version:Option[Int]=Option(0)
  
  def this(path:String) {
    this(path,null,null)
  }
  def this(path: String, parentParameters: Option[Map[String, String]], selfParameters: Option[Map[String, String]], version: Option[Int]) {
    this(path)
    this.version=version
    this.parentParameters=parentParameters
    this.selfParameters=selfParameters
  }
}

	
