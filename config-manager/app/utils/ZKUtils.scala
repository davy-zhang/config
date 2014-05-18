package utils

import scala.collection.mutable.ListBuffer
import com.github.zkclient.IZkClient
import scala.collection.JavaConverters._
import org.apache.commons.lang3.StringUtils
import play.Logger

object ZKUtils {
  def getChildPath(client: IZkClient, dirName: String) = {
    val paths = ListBuffer[String]()
    val dirNames = client.getChildren(dirName).asScala
    val splite = if ("/" == dirName) "" else "/"
    if (dirNames != null) {
      for (dir <- dirNames) {
        paths += (dirName + splite + dir)
      }
    }
    paths
  }

  def getAllChildPathHaveEmpty(client: IZkClient, dirName: String): ListBuffer[String] = {
    val paths = ListBuffer[String]()
    for (path <- getChildPath(client, dirName)) {
      if (!paths.contains(path))
        paths += path
      if (isDir(client, path)) {
        paths ++= getAllChildPathHaveEmpty(client, path)
      }
    }
    paths
  }

  def getEachOneChildPath(client: IZkClient, dirName: String): ListBuffer[String] = {
    val paths = ListBuffer[String]()
    for (path <- getChildPath(client, dirName)) {
      paths += path
      if (isDir(client, path)) {
        paths ++= getEachOneChildPath(client, path)
      }
    }
    paths
  }
  def isDir(client: IZkClient, path: String) = getChildPath(client, path).size != 0

  def getEachOnePath(rootPath: String, path: String): ListBuffer[String] = {
    val paths = ListBuffer[String]()
    if (StringUtils.startsWith(path, rootPath)) {
      if (StringUtils.equals(path, rootPath)) {
        paths += path
      } else {
        paths += path
        paths ++= getEachOnePath(rootPath, if (StringUtils.countMatches(path, "/") == 1) "/" else StringUtils.substringBeforeLast(path, "/"))
      }
    }

    paths
  }

  def updatePath(zkClient: IZkClient, oldPath: String, newPath: String) = {
    if (zkClient.exists(oldPath) && !zkClient.exists(newPath)) {
      try {
        val data = zkClient.readData(oldPath)
        zkClient.createPersistent(newPath, data)
        true
      } catch {
        case t: Throwable =>
          Logger.error("从" + oldPath + "移动到" + newPath + "时出错", t)
          false
      }
    } else {
      Logger.error("请确认{}在zookeeper中存在,{}在zookeeper中不存在", oldPath, newPath)
      false
    }
  }

  def getParentPath(rootPath: String, path: String) = if (rootPath == path) rootPath else StringUtils.substringBeforeLast(path, "/")

}