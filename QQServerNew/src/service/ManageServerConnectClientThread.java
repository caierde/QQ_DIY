package service;


import java.util.HashMap;
import java.util.Iterator;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2022/12/13/12:56
 * @Description: Created with IntelliJ IDEA
 */
public class ManageServerConnectClientThread {
    //把多个线程放入到HashMap集合中,key就是用户id,value就是线程
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //将某个线程加入到集合中
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //通过userId,可以得到对应线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //删除线程
    public static void removeThread(String userId){
        hm.remove(userId);
    }

    public static String getOnlineUsers() {
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }
}
