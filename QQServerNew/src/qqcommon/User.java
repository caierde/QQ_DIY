package qqcommon;

import java.io.Serializable;

/**
 * 表示一个用户或客户信息
 */
public class User implements Serializable {

    private static final long SerialVersionUID = 1L;
    private String userId;      //用户Id
    private String passwd;      //用户密码

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
