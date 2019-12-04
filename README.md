# 工业互联网大数据安全监测：工业会话蠕虫检测挖掘模块 
~~~
工业互联网大数据安全监测平台 - Worm Detect插件

该插件可以检测工业互联网大数据中的蠕虫行为

该插件提供3种模式：学习模式 / 检测模式 / 清理模式

当前版本：0.0.1 (build 8)
~~~

自动编译信息：[![Build Status](https://www.travis-ci.org/SugarGuan/WormDetect.svg?branch=master)](https://www.travis-ci.org/SugarGuan/WormDetect)     


## 学习模式

学习模式下，机器获取elasticsearch中保存的工业互联网会话。

通过对不同会话间的跳跃关系、聚集关系分析确定风险行为，并将具体的操作队列记录在本地（白名单）。


## 检测模式

检测模式中，根据会话开展检测。如果检测到异常且不在白名单中，则发出报警，向redis发送警报。


## 清理模式

清理模式将清空已有的学习文件（模式）。清理模式结束后返回前一线程模式。

## Future Versions:

1. 改进模型构建算法，降低时间复杂度
2. 适应Java和Scala语法（不同的数据格式转换问题）


&copy;  2019 Harbin Institute and Technology.
