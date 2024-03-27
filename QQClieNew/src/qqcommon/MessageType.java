package qqcommon;

/**
 * 表示消息类型的接口（比如登录消息，私聊消息等）
 */
public interface MessageType {
    //在接口中定义了一些常量
    //不同的常量值，表示不同的消息类型

    String MESSAGE_LOGIN_SUCCEED = "1";     //表示登录成功
    String MESSAGE_LOGIN_FAILED = "2";      //表示登录失败
    String MESSAGE_COMM_MES = "3";          //普通消息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; //返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";       //客户端请求退出
    String MESSAGE_SERVER_PER_CLIENT_EXIT = "7";       //服务端允许客户端退出
    String MESSAGE_TO_ALL_MES = "8";        //群发消息包
    String MESSAGE_FILE_MES = "9";          //发送文件消息包
    String MESSAGE_GAME_MES = "10";          //发送游戏数据包
    String MESSAGE_GAME_Start = "11";          //发送开始游戏消息包


}
