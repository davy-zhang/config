/**
 *
 */
package models

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

/**
 * @author davy
 *
 */
trait ConfigDAO {
  def getTreeNodes(): ListBuffer[TreeNode]
  def getConfig(path: String): Config
  def createNode(path: String): Boolean
  def deleteNode(path: String): Boolean
  def updateNode(path: String, newName: String): Boolean
  def saveConfig(path:String,kvs:Map[String,String]):Boolean
}