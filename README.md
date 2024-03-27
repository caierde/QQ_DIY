# QQ_DIY
JAVA实现的命令行交互版的简单通讯软件  
**QQClieNew：**		   
1 显示在线用户列表  
		         2 群发消息  
		         3 私聊消息  
		         4 发送文件  
		         6 游戏对战（还未完全实现，功能目前不可用）  
		         9 退出系统  

    
**QQServerNew：**  
服务器端，负责确定监听的端口以及QQClieNew用户一开始需要确定的服务器ip等  

目前已预定义的用户名和密码：  
```
validUsers.put("猴子",new User("猴子","123"));  
validUsers.put("紫霞仙子",new User("紫霞仙子","123"));  
validUsers.put("牛魔王",new User("牛魔王","123"));  
validUsers.put("至尊宝",new User("至尊宝","123"));  
validUsers.put("铁扇公主",new User("铁扇公主","123"));  
validUsers.put("001",new User("001","123"));  
validUsers.put("002",new User("002","123"));  
validUsers.put("003",new User("003","123"));  
validUsers.put("a",new User("a","123"));  
validUsers.put("b",new User("b","123"));  
validUsers.put("c",new User("c","123"));
```
如：用户名：猴子；密码：123
