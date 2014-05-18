package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json.Writes
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import models.TreeNode
import play.api.data._
import play.api.data.Forms._
import models.Config
import views._
import scala.math.Ordering
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.Map

object ConfigController extends Controller {

  def configManager = Action(Ok(views.html.configManager()))

  val configService: ConfigService = new ConfigService

  def getTreeNodes = Action(Ok(configService.getTreeNodes))

  def getConfig = Action(request => Ok(configService.getConfig(request.getQueryString("path"))))

  def createNode = Action(request => Ok(configService.createNode(request.getQueryString("path")).toString))

  def updateNode = Action(request => Ok(configService.updateNode(request.getQueryString("path"), request.getQueryString("newName")).toString))

  def deleteNode = Action(request => Ok(configService.deleteNode(request.getQueryString("path")).toString))

  def saveConfig = Action(request => {
    val param = request.body.asFormUrlEncoded
    if (param.isDefined) {
      val keys = param.get.filterKeys(_.startsWith("k_"))
      val values = param.get.filterKeys(_.startsWith("v_"))
      val path = param.get.get("path").get.head
      val kvs = Map[String, String]()
      for (key <- keys) {
        kvs += ((keys.get(key._1).get.head, values.get("v_" + key._1.substring(2)).get.head))
      }
      Ok(configService.saveConfig(path, kvs).toString)
    } else {
      Ok("请求参数为空")
    }
  })
}