package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/15/20:23
 * @Description: Created with IntelliJ IDEA
 * 该类完成文件传输功能
 */
public class FileClientService {
    /**
     *
     * @param src         文件源地址
     * @param dest        文件目的地址
     * @param senderId    发送者
     * @param getterId    接收者
     */
    public void sendFileToOne(String src, String dest, String senderId, String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowTime = dateTimeFormatter.format(now);
        message.setSendTime(nowTime);

        //读取文件
        byte[] fileBytes = new byte[(int) new File(src).length()];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println(message.getSendTime()+" "+ message.getSender()+"（我）对 "+message.getGetter()+" 发送文件："+
                message.getSrc()+" 到对方的电脑目录 "+message.getDest());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
