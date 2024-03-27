package service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/11/21:25
 * @Description: Created with IntelliJ IDEA
 * 该类完成用户登录验证和用户注册等功能
 */
public class UserClientService {
    private User u = new User();
    private Socket socket;
    private String ip;
    public boolean checkUser(String userId, String passwd, String ip){
        boolean judge = false;
        u.setUserId(userId);
        u.setPasswd(passwd);
        //连接服务器，发送u对象
        try {
            //这里可以修改服务器ip代码
            socket = new Socket(InetAddress.getByName(ip),9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);
            //读取从服务器回复的信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if(MessageType.MESSAGE_LOGIN_SUCCEED.equals(ms.getMesType())){   //登录成功
                //创建一个和服务器端保持通信的线程
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                ccst.start();
                //这里为了以后的客户端拓展，使用集合进行线程管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,ccst);

                judge = true;

            }else {
                //验证失败则关闭socket
                socket.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return judge;
    }

    //向服务器端请求在线用户列表
    public void onlineFriendList(){
        Message message = new Message();
        message.setSender(u.getUserId());
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //退出线程
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" 退出系统");
            //退出进程
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //运行游戏
    public void gameStart(String gameId, String SenderId, String GetterId){
        switch (gameId){
            case "1":
                new GameService(socket, SenderId, GetterId).bashGame1();

        }
    }

}
