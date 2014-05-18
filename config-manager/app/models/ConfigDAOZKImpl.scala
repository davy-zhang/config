/**
 *
 */
package models

import scala.collection.mutable.ListBuffer
import com.github.zkclient.ZkClient
import play.api.Play
import org.apache.commons.lang3.StringUtils._
import sun.reflect.UTF8
import play.Logger
import java.nio.charset.Charset
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import org.apache.zookeeper.data.Stat
import java.util.concurrent.atomic.AtomicBoolean
import scala.util.parsing.json.JSONObject
import play.api.libs.json.Json
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import utils.ZKUtils
import utils.MarshallableImplicits._
import utils.JsonUtil

/**
 * @author davy
 *
 */
class ConfigDAOZKImpl extends ConfigDAO {
  val ROOT_PATH = Play.current.configuration.getString("rootPath").getOrElse("/configuration")
  val zkClient = new ZkClient(Play.current.configuration.getString("zkServer").get)
  val charset = Play.current.configuration.getString("charset").getOrElse("UTF-8")

  override def getTreeNodes(): ListBuffer[TreeNode] = {
    val paths = ListBuffer(ROOT_PATH) ++= ZKUtils.getEachOneChildPath(zkClient, ROOT_PATH)
    paths.map(path => {
      path match {
        case ROOT_PATH => new TreeNode(ROOT_PATH, "0", ROOT_PATH, false, true)
        case path: String => new TreeNode(path, substringBeforeLast(path, "/"), substringAfterLast(path, "/"))
      }
    });
  }

  override def getConfig(path: String): Config = {
    var parentParameters = Map[String, String]()
    val stat = new Stat
    val jsons = ZKUtils.getEachOnePath(ROOT_PATH, path).reverse.map(_path => {
      if (_path == path) Option(zkClient.readData(_path, stat)) else Option((zkClient.readData(_path)))
    }).map(data => {
      if (data.isDefined) Option(new String(data.get, Charset.forName(charset))) else None
    });
    val version: Int = stat.getVersion()
    Logger.debug("path:{},取出继承的配置字符串:{}", path, jsons)
    val selfJson = jsons.remove(jsons.length - 1)
    Logger.debug("path:{},取出自己的配置字符串:{}", path, jsons)
    jsons.foreach(json => {
      val map = jsonToMap(json)
      if (map.isDefined) {
        parentParameters ++= map.get
      }
    })
    Logger.debug("继承的配置变成Map:{}", parentParameters)
    val selfParameters = jsonToMap(selfJson)
    Logger.debug("自己的配置变成Map:{},版本是:{}", selfParameters, "" + version)
    new Config(path, Option(parentParameters), selfParameters, Option(version))
  }

  override def createNode(path: String) = createOrdeleteNode(path, zkClient.createPersistent)

  override def deleteNode(path: String) = createOrdeleteNode(path, zkClient.delete)

  override def updateNode(path: String, newName: String) = ZKUtils.updatePath(zkClient, path, ZKUtils.getParentPath(ROOT_PATH, path) + "/" + newName)

  def createOrdeleteNode(path: String, operation: (String) => Any): Boolean = {
    Logger.debug("要操作节点路径:{}", path);
    val isOk = new AtomicBoolean(true)
    try {
      operation(path)
    } catch {
      case t: Throwable => Logger.error("操作节点路径:" + path + "时发生异常", t); isOk set false
    }
    isOk get
  }

  def jsonToMap(json: Option[String]): Option[Map[String, String]] = if (json.isDefined) Option(JsonUtil.toMap[String](json.get)) else None

  def saveConfig(path: String, kvs: collection.mutable.Map[String, String]) = {
    Logger.debug("保存配置:path={},kvs={}", path, kvs)
    val kvsJson = kvs.toJson
    try {
      zkClient.writeData(path, kvsJson.getBytes(Charset.forName(charset)))
      true
    } catch {
      case t: Throwable =>
        Logger.error("保存配置失败,path:" + path + ",kvs:" + kvs, t)
        false
    }
  }
}