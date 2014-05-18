package models

case class TreeNode(val id: String, val pid: String, val name: String, val checked: Boolean, val open: Boolean) {
  def this(id: String, pid: String, name: String) {
    this(id, pid, name, false, false)
  }
}