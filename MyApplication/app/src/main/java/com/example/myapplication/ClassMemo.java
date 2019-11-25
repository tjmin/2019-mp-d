package com.example.myapplication;

public class ClassMemo {
    private String member_userid;
    private String member_title;
    private String member_contents;
    private String member_sharecode;


    public String getMember_userid() {
        return member_userid;
    }

    public String getMember_title() {
        return member_title;
    }

    public String getMember_contents() {
        return member_contents;
    }
    public String getMember_sharecode() {
        return member_sharecode;
    }

    public void setMember_userid(String member_userid) {
        this.member_userid = member_userid;
    }

    public void setMember_title(String member_title) {
        this.member_title = member_title;
    }

    public void setMember_contents(String member_contents) {
        this.member_contents = member_contents;
    }

    public void setMember_sharecode(String member_sharecode) {
        this.member_sharecode = member_sharecode;
    }
}
