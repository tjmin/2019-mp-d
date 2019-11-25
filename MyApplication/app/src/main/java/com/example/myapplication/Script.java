//작성자: 박재효, 이원구
// for database (3 of 3)
// Entity class

package com.example.myapplication;



public class Script {
    private int id;
    private String userId;
    private String scriptTitle;
    private String scriptContents;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userName) {
        this.userId = userId;
    }

    public String getScriptTitle() {
        return scriptTitle;
    }

    public void setScriptTitle(String scriptTitle) {
        this.scriptTitle = scriptTitle;
    }

    public String getScriptContents() {
        return scriptContents;
    }

    public void setScriptContents(String scriptContents) {
        this.scriptContents = scriptContents;
    }

}
