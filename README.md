config
======

一个基于zookeeper的配置管理服务。

引用客户端后,调用相应的api来获取参数,当参数发生改变时,客户端中的配置会自动刷新.
当成功获取配置后,会像zookeeper注册一个临时的znode,在配置管理中心能查看当前配置的订阅者都有谁.

配置管理中心可以通过可视化界面配置zookeeper中的参数.通过子节点继承父节点的方式来进行配置的抽象.

config-client的API文档http://d-z.cc/oos/config-client


