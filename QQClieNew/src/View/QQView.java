package View;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2023/01/19/21:38
 * @Description: Created with IntelliJ IDEA
 */

import service.FileClientService;
import service.GameService;
import service.MessageClientService;
import service.UserClientService;
import utils.Utility;

public class QQView {
    private boolean loop = true; //判断是否显示菜单
    private String key;         //接受用户的键盘输入
    private UserClientService userClientService = new UserClientService();     //用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService(); //用于私聊消息
    private FileClientService fileClientService = new FileClientService();
    private GameService gameService = null;   //用于开始游戏
    public static void main(String[] args) {
        new QQView().mainMenu();
    }

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("===========欢迎登录网络通信系统============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入您的选择：");

            key = Utility.readString(1);           //接受用户选择（限制为1个字符）

            switch (key) {
                case "1":
                    System.out.println("\n===登录系统");
                    System.out.print("请输入服务器端ip(例如：192.168.1.105)：");
                    String ip = Utility.readString(30);
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(30);
                    System.out.print("请输入密码：");
                    String passWd = Utility.readString(30);
                    //需要去服务器验证该用户是否合法
                    //这里还需要许多代码

                    if (userClientService.checkUser(userId, passWd, ip)) {
                        System.out.println("===========欢迎（用户：" + userId + "）============");
                        //进入到二级菜单
                        while (loop) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            System.out.println("\n===========网络通信系统二级菜单（用户：" + userId + "）============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 6 游戏对战");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入您的选择：");

                            key = Utility.readString(1);           //接受用户选择（限制为1个字符）
                            switch (key) {
                                case "1":
                                    System.out.println("\n===显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "2":
                                    System.out.println("\n===群发消息");
                                    System.out.print("请输入想要群发消息内容：");
                                    String content1 = Utility.readString(100);
                                    System.out.println("确认是否群发消息：");
                                    char choose1 = Utility.readConfirmSelection();
                                    if (choose1 == 'Y') {
                                        messageClientService.sendMessageAll(content1, userId);
                                    } else {
                                        System.out.println("===========放弃群发消息============");
                                    }
                                    break;
                                case "3":
                                    System.out.println("\n===私聊消息");
                                    System.out.print("请输入想聊天的用户号(在线)：");
                                    String getterId = Utility.readString(30);
                                    System.out.print("请输入聊天内容：");
                                    String content2 = Utility.readString(100);
                                    System.out.println("确认是否群发消息：");
                                    char choose2 = Utility.readConfirmSelection();
                                    if (choose2 == 'Y') {
                                        messageClientService.sendMessageTo(content2, userId, getterId);
                                    } else {
                                        System.out.println("===========放弃群发消息============");
                                    }
                                    break;
                                case "4":
                                    System.out.println("\n===发送文件");
                                    System.out.print("请输入发送文件的用户号(在线)：");
                                    String getterId2 = Utility.readString(30);
                                    System.out.print("请输入发送文件的路径（形式：d:\\\\xx.jpg）：");
                                    String src = Utility.readString(50);
                                    System.out.print("请输入文件发送到的对应的路径（形式：e:\\\\xx.jpg）：");
                                    String dest = Utility.readString(50);
                                    System.out.println("确认是否群发消息：");
                                    char choose3 = Utility.readConfirmSelection();
                                    if (choose3 == 'Y') {
                                        fileClientService.sendFileToOne(src, dest, userId, getterId2);
                                    } else {
                                        System.out.println("===========放弃发送文件============");
                                    }
                                    break;
                                case "6":
                                    System.out.println("\n===游戏对战");
                                    System.out.println("游戏列表：");
                                    System.out.println("1.巴什博弈(点兵点将小游戏)");
                                    System.out.println("...敬请期待");
                                    System.out.print("\n请输入您选择的游戏序号：");
                                    key = Utility.readString(1);           //接受用户选择（限制为1个字符）
                                    System.out.print("请输入邀请对战的用户姓名(在线)：");
                                    String getterId3 = Utility.readString(30);
                                    System.out.println("确认是否开始游戏：");
                                    char choose4 = Utility.readConfirmSelection();
                                    if (choose4 == 'Y') {
                                        switch (key) {
                                            case "1":
                                                userClientService.gameStart(key, userId, getterId3);
                                                break;
                                            default:
                                                System.out.println("无此游戏");
                                        }
                                    } else {
                                        System.out.println("===========放弃游戏对战============");
                                    }

                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;

                            }

                        }
                    } else {  //登录服务器验证失败
                        System.out.println("\n======登录失败=======");
                    }

                    break;
                case "9":
                    loop = false;
                    break;

            }

        }

    }

}

