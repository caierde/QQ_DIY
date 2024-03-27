package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2023/01/23/18:12
 * @Description: Created with IntelliJ IDEA
 * 此类专门用来接受和发送游戏数据包
 */

public class GameDataConnectService {
    //该线程需要持有Socket
    private Socket socket;
    public String content;
    //可以获取一个Socket对象
    public GameDataConnectService(Socket socket) {
        this.socket = socket;
    }

    //发送游戏数据包
    public void SendGameDataMessage(String content, String senderId, String getterId) {
        //构建message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GAME_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //接受游戏数据包
    public void SetGameDataMessage(Message ms) {
        content = ms.getContent();
    }

}
