//작성자: 박재효
package com.example.myapplication;

public class Script {
    private String userId;
    private String title;
    private String path;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Script(String userId, String title, String path) {
        this.userId = userId;
        this.title = title;
        this.path = path;
    }
}
