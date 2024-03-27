package service;

import qqcommon.User;
import utils.Utility;

import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: Child
 * @Version: 1.0
 * @Date: 2023/01/23/11:03
 * @Description: Created with IntelliJ IDEA
 */
public class GameService {
    private Socket socket;
    private String SenderId;
    private String GetterId;
    //可以获取一个Socket对象
    public GameService(Socket socket, String SenderId, String GetterId) {
        this.socket = socket;
        this.SenderId = SenderId;
        this.GetterId = GetterId;
    }

    private GameDataConnectService gameDataConnectService = new GameDataConnectService(socket);

    //巴什博弈1号
    public void bashGame1() {
        System.out.println("欢迎进入巴什博弈游戏，也可以称之为点兵点将游戏");
        System.out.print("请输入初始的兵个数：");
        int num = Utility.readInt();
        gameDataConnectService.SendGameDataMessage(""+ num, SenderId, GetterId);
        System.out.print("请输入每名玩家每次最多可以拿的兵数量：");
        int soldierNum = Utility.readInt();
        gameDataConnectService.SendGameDataMessage(""+ soldierNum, SenderId, GetterId);
        System.out.println("每名玩家每次可以拿1~" + soldierNum + "个兵");
        System.out.println("=====游戏开始(1号玩家)======");
        int[][] arr = CreateArray(num); //创建数组
        printArray(arr);
        String result_str = "";  //结果字符串
        for (int i = 0; num > 0; i++) {
            for (int j = 1; j < 3; j++) {
                System.out.println("=====" + "第" + (i + 1) + "局======");
                int subtractedNum = 0;
                if (j == 1) {
                    System.out.println("第" + (i + 1) + "局: " + j + "号玩家请输入您想要拿取的兵数量");
                    subtractedNum = Utility.readInt();
                    while (!Judge(subtractedNum, num, soldierNum)) {
                        System.out.println("=====" + "第" + (i + 1) + "局======");
                        System.out.println("第" + (i + 1) + "局: " + j + "号玩家请重新输入您想要拿取的兵数量");
                        subtractedNum = Utility.readInt();
                        gameDataConnectService.SendGameDataMessage("" + subtractedNum, SenderId, GetterId);
                    }
                }else {
                    System.out.println("=====" + "第" + (i + 1) + "局======");
                    num = Integer.parseInt(gameDataConnectService.content);
                    System.out.println("第" + (i + 1) + "局: " + j + "号玩家拿取的兵数量为："+ num);
                }
                num -= subtractedNum;
                minusArray(arr, subtractedNum);
                printArray(arr);
                System.out.println("场上剩余" + num + "个兵");
                if (j == 1) {
                    if (subtractedNum < 10)
                        result_str += "  " + subtractedNum;    //让结果排列更加好看一点
                    else {
                        result_str += " " + subtractedNum;
                    }
                } else {
                    result_str += "       " + subtractedNum + "\n";
                }
                if (num == 0) {
                    System.out.println("第" + (i + 1) + "局结束: " + j + "号玩家拿到最后一个兵，游戏结束");
                    System.out.println("结果:" + (3 - j) + "号玩家胜利，" + j + "号玩家失败");
                    System.out.println("对局结果统计：");
                    System.out.println("玩家1" + "    " + "玩家2");

                    System.out.println(result_str);  //通过字符串解决游戏记录问题
                    return;
                }
            }

        }

    }

    //巴什博弈2号
    public void bashGame2() {
        System.out.println("欢迎进入巴什博弈游戏，也可以称之为点兵点将游戏");
        int num = Integer.parseInt(gameDataConnectService.content);
        System.out.println("初始的兵个数：" + num);
        int soldierNum = Integer.parseInt(gameDataConnectService.content);
        System.out.println("每名玩家每次可以拿1~" + soldierNum + "个兵");
        System.out.println("=====游戏开始(2号玩家)======");
        int[][] arr = CreateArray(num); //创建数组
        printArray(arr);
        String result_str = "";  //结果字符串
        for (int i = 0; num > 0; i++) {
            for (int j = 1; j < 3; j++) {
                System.out.println("=====" + "第" + (i + 1) + "局======");
                int subtractedNum = 0;
                if (j == 2) {
                    System.out.println("第" + (i + 1) + "局: " + j + "号玩家请输入您想要拿取的兵数量");
                    subtractedNum = Utility.readInt();
                    while (!Judge(subtractedNum, num, soldierNum)) {
                        System.out.println("=====" + "第" + (i + 1) + "局======");
                        System.out.println("第" + (i + 1) + "局: " + j + "号玩家请重新输入您想要拿取的兵数量");
                        subtractedNum = Utility.readInt();
                        gameDataConnectService.SendGameDataMessage("" + subtractedNum, SenderId, GetterId);
                    }
                }else {
                    System.out.println("=====" + "第" + (i + 1) + "局======");
                    num = Integer.parseInt(gameDataConnectService.content);
                    System.out.println("第" + (i + 1) + "局: " + j + "号玩家拿取的兵数量为："+ num);
                }
                num -= subtractedNum;
                minusArray(arr, subtractedNum);
                printArray(arr);
                System.out.println("场上剩余" + num + "个兵");
                if (j == 1) {
                    if (subtractedNum < 10)
                        result_str += "  " + subtractedNum;    //让结果排列更加好看一点
                    else {
                        result_str += " " + subtractedNum;
                    }
                } else {
                    result_str += "       " + subtractedNum + "\n";
                }
                if (num == 0) {
                    System.out.println("第" + (i + 1) + "局结束: " + j + "号玩家拿到最后一个兵，游戏结束");
                    System.out.println("结果:" + (3 - j) + "号玩家胜利，" + j + "号玩家失败");
                    System.out.println("对局结果统计：");
                    System.out.println("玩家1" + "    " + "玩家2");

                    System.out.println(result_str);  //通过字符串解决游戏记录问题
                    return;
                }

            }
        }
    }

    public static int[][] CreateArray(int num) {
        int ynum = 15;                       //每行体现十五个是很有美感的
        int row = num / ynum;
        int column = num % ynum;
        int[][] arr = new int[row + 1][];   //创建数组
        for (int i = 0; i < row; i++) {
            arr[i] = new int[ynum];
        }
        arr[row] = new int[column];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 1;
            }
        }
        for (int i = 0; i < arr[row].length; i++) {
            arr[row][i] = 1;
        }
        return arr;
    }

    public static boolean Judge(int subtractedNum, int num, int soldierNum) {
        if (subtractedNum < 1 || subtractedNum > soldierNum) {
            System.out.println("拿去数量错误，应该在1~" + soldierNum + "之间");
            return false;
        } else if (subtractedNum > num) {
            System.out.println("拿去数量错误，系统只剩下" + num + "个兵");
            return false;
        }
        return true;

    }

    public static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void minusArray(int[][] arr, int subtractedNum) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] == 1 && subtractedNum > 0) {
                    arr[i][j] = 0;
                    subtractedNum--;
                }
            }
        }
    }
}
