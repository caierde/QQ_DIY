package service;



import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/12/11:46
 * @Description: Created with IntelliJ IDEA
 * 这是服务器，在监听9999，等待客户端的连接，并保持通信
 */
public class QQServer {
    private ServerSocket ss;
    //创建一个集合，放置多个用户，如果是这些用户登录就认为是合法的
    private static HashMap<String,User> validUsers = new HashMap<>();
    static {  //在静态代码块中初始化validUsers
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

    }

    //验证用户登录是否合法
    private boolean CheckUser(String userId, String passwd){
        User u = validUsers.get(userId);
        if(u == null){
            return false;
        }
        if(!(u.getPasswd().equals(passwd))){
            return false;
        }
        return true;
    }

    public QQServer() {
        System.out.print("请输入服务器监听端口（例如：9999）：");
        int port = Utility.readInt();
        System.out.println("服务器在"+port+"端口，监听...");
        try {
            ss = new ServerSocket(port);
            while (true){       //当和某个客户端连接后，会继续监听，因此用循环
                Socket socket = ss.accept();
                //读取从客户端发送的验证信息
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                User u = (User) ois.readObject();
                //创建Message对象，准备回复客户端
                Message message = new Message();
                //验证
                if(CheckUser(u.getUserId(), u.getPasswd())){
                    System.out.println("用户 id=" + u.getUserId()+" passwd=" + u.getPasswd()+" 登录成功" );
                    //发回结果消息
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建一个线程，和客户端保持通信
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();
                    //把该线程对象放入到一个集合中，进行管理
                    ManageServerConnectClientThread.addServerConnectClientThread(u.getUserId(),serverConnectClientThread);
                }else {
                    System.out.println("用户 id=" + u.getUserId()+" passwd=" + u.getPasswd()+" 登录失败" );
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAILED);
                    oos.writeObject(message);
                    //登录失败则关闭socket
                    socket.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //服务器端退出了while循环，说明服务器端不再监听，关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
