package service;

import java.util.HashMap;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/12/11:16
 * @Description: Created with IntelliJ IDEA
 */
public class ManageClientConnectServerThread {
    //把多个线程放入到HashMap集合中,key就是用户id,value就是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    //将某个线程加入到集合中
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread){
        hm.put(userId, clientConnectServerThread);
    }
    //通过userId,可以得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
         return hm.get(userId);
    }

    //删除线程
    public static void removeThread(String userId){
        hm.remove(userId);
    }

}
