package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/12/15:05
 * @Description: Created with IntelliJ IDEA
 */
public class ServerConnectClientThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;

    private String userId; //连接到服务器的用户id
    //可以获取一个Socket对象


    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        System.out.println("服务器端线程与客户端 "+userId+" 保持通信，等待传输消息...");
        //因为Thread可以在后台和客户端通信,因此做成循环
        while (true){
            try {
                //如果没有拿到传输的消息，会一直阻塞到这，因此不需要sleep()方法
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms = (Message) ois.readObject();
                //客户端要求返回在线用户列表
                if (ms.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(ms.getSender()+" 请求在线用户列表");
                    Message message = new Message();
                    message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message.setContent(ManageServerConnectClientThread.getOnlineUsers());
                    message.setGetter(ms.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                //客户端请求退出
                }else if (ms.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(ms.getSender()+" 请求退出");
                    Message message = new Message();
                    message.setGetter(ms.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    message.setMesType(MessageType.MESSAGE_SERVER_PER_CLIENT_EXIT);

                    ManageServerConnectClientThread.removeThread(ms.getSender());
                    socket.close();
                    //退出线程
                    break;
                //客户端请求私聊消息
                }else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println(ms.getSendTime()+" "+ ms.getSender()+" 对 "+ms.getGetter()+"说："+ms.getContent());
                    //根据getterId得到对应的线程
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                    oos.writeObject(ms);
                //客户端请求群发消息
                }else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    System.out.println(ms.getSendTime() + " " + ms.getSender() + " 对 " + "所有人 说：" + ms.getContent());
                    HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线用户id
                        String onlineUserId = iterator.next().toString();
                        if (!onlineUserId.equals(ms.getSender())) {
                            ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(onlineUserId);
                            ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                            oos.writeObject(ms);
                        }
                    }
                //客户端请求开始游戏
                }else if(ms.getMesType().equals(MessageType.MESSAGE_GAME_Start)) {
                    System.out.println(ms.getSender()+" 和 "+ms.getGetter()+"准备开始游戏");
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                    oos.writeObject(ms);
                //客户端请求同步游戏数据
                }else if(ms.getMesType().equals(MessageType.MESSAGE_GAME_MES)) {
                    System.out.println(ms.getSender()+" 和 "+ms.getGetter()+"正在同步游戏数据");
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                    oos.writeObject(ms);

                //客户端发送文件
                }else if (ms.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println(ms.getSendTime()+" "+ ms.getSender()+" 对 "+ms.getGetter()+" 发送文件："+
                            ms.getSrc()+" 到对方的电脑目录 "+ms.getDest());
                    //根据getterId得到对应的线程
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(ms.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.socket.getOutputStream());
                    oos.writeObject(ms);

                }

            } catch (Exception e) {
                System.out.println("警告！"+userId+"进程异常退出");
                ManageServerConnectClientThread.removeThread(userId);
                try {
                    socket.close();
                } catch (IOException ex) {
                }
                return;
            }
        }

    }
}
