package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/14/20:10
 * @Description: Created with IntelliJ IDEA
 * 提供和消息相关的服务方法
 */
public class MessageClientService {
    /**
     *
     * @param content       内容
     * @param senderId      发送对象id
     * @param getterId      接受对象id
     */
    public void sendMessageTo(String content, String senderId, String getterId){
        //构建message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateTimeFormatter.format(now);
        message.setSendTime(nowTime);
        System.out.println(message.getSendTime()+" "+ message.getSender()+"（我）对 "+message.getGetter()+" 说："+message.getContent());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageAll(String content, String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateTimeFormatter.format(now);
        message.setSendTime(nowTime);
        System.out.println(message.getSendTime()+" "+ message.getSender()+"（我）对 "+" 所有人 说："+message.getContent());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
