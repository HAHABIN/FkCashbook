package com.example.haha.fkcashbook.model.bean;

public class Kfaccount extends BaseBean {
    private int userid;
    private String username;
    private String password;
    private String Kfmail;
    private String sex;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getKfmail() {
        return Kfmail;
    }

    public void setKfmail(String kfmail) {
        Kfmail = kfmail;
    }


}
