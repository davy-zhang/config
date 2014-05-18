/**
 *
 */
package controllers

import scala.collection.mutable.ListBuffer
import models.TreeNode
import play.api.libs.json.Writes
import play.api.libs.json.Json
import models.Config
import models.ConfigDAOZKImpl
import models.ConfigDAO
import scala.collection.mutable.Map
import play.api.Play
import utils.MarshallableImplicits._

/**
 * @author davy
 *
 */
class ConfigService {
  private val configDao: ConfigDAO = new ConfigDAOZKImpl

  def getTreeNodes(): String = configDao.getTreeNodes.toJson

  def getConfig(path: Option[String]) = if (path.isDefined) configDao.getConfig(path.get).toJson else "null"

  def createNode(path: Option[String]) = if (path.isDefined) configDao createNode path.get else false

  def deleteNode(path: Option[String]) = if (path.isDefined) configDao deleteNode path.get else false

  def updateNode(path: Option[String], newName: Option[String]) = if (path.isDefined && newName.isDefined) configDao.updateNode(path.get, newName.get) else false

  def saveConfig(path: String, kvs: Map[String, String]) = configDao.saveConfig(path, kvs)
}