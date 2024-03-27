package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/11/22:49
 * @Description: Created with IntelliJ IDEA
 * 客户端和服务器端保持通信的线程
 */
public class ClientConnectServerThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;
    //可以获取一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为Thread可以在后台和服务器通信,因此做成循环
        while (true){
            try {
//                System.out.println("客户端线程，等待读取从服务器端传输的消息");

                //如果没有拿到传输的消息，会一直阻塞到这，因此不需要sleep()方法

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Message ms = (Message) ois.readObject();
                    //如果读取到的是服务器端返回的在线好友列表
                    if (ms.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                        String[] OnlineUsers = ms.getContent().split(" ");
                        for (int i = 0; i < OnlineUsers.length; i++) {
                            System.out.println("用户：" + OnlineUsers[i]);
                        }
                        //退出消息
                    } else if(ms.getMesType().equals(MessageType.MESSAGE_SERVER_PER_CLIENT_EXIT)){
                        socket.close();
                        break;
                        //私聊消息
                    }else if(ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                        System.out.println("\n"+ms.getSendTime()+" "+ ms.getSender()+" 对 "+ms.getGetter()+"（我）说："+ms.getContent());
                        //群发消息
                    } else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                        System.out.println("\n"+ms.getSendTime()+" "+ ms.getSender()+" 对 "+"所有人 说："+ms.getContent());
                        //开始游戏消息
                    }else if(ms.getMesType().equals(MessageType.MESSAGE_GAME_Start)){
                        System.out.println("\n"+ms.getSendTime()+" "+ ms.getSender()+" 和 "+ms.getGetter()+"（我）开始游戏："+ms.getContent()+"\n");
                        new GameService(socket, ms.getGetter(), ms.getSender()).bashGame2();
                        //游戏数据包消息
                    }else if(ms.getMesType().equals(MessageType.MESSAGE_GAME_MES)){
                        new GameDataConnectService(socket).SetGameDataMessage(ms);

                        //文件消息
                    }else if (ms.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                        System.out.println("\n"+ms.getSendTime()+" "+ ms.getSender()+" 对 "+ms.getGetter()+"（我）发送文件："+
                                ms.getSrc()+" 到我的电脑目录 "+ms.getDest());
                        FileOutputStream fos = new FileOutputStream(ms.getDest());
                        fos.write(ms.getFileBytes());
                        fos.close();
                        System.out.println("\n"+"保存文件成功！");
                    }

            } catch (Exception e) {

            }
        }

    }
}
