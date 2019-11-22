//작성자: 박재효, 이원구
// for database (3 of 3)
// Entity class

package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "scripts")
public class Script {
    @PrimaryKey(autoGenerate = true)
    private int id; // default value
    @ColumnInfo(name = "userName")
    private String userId = "mobileId";   //default
    @ColumnInfo(name = "title")
    private String scriptTitle;
    @ColumnInfo(name = "contents")
    private String scriptContents;
//    @ColumnInfo(name = "path")
//    private String scriptPath;

    @Ignore // we don't want to store this value on database so ignore it
    private boolean checked = false;

    public Script(){

    }
    public Script(String userId, String scriptTitle, String scriptContents) {
        this.userId = userId;
        this.scriptTitle = scriptTitle;
        this.scriptContents = scriptContents;
    }

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    //    public String getScriptPath() {
//        return scriptPath;
//    }
//
//    public void setScriptPath(String scriptPath) {
//        this.scriptPath = scriptPath;
//    }


    // 작성자: 이원구
    @Override
    public String toString() {
        return "Script{" +
                "id=" + id+
                ", title=" + scriptTitle+
                '}';
    }
}