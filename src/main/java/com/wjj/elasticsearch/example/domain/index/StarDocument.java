package com.wjj.elasticsearch.example.domain.index;

import java.util.List;

/**
 * @Author: wangjunjie 2019/8/23 15:52
 * @Description: 存储文档 star_document
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/23 15:52
 */
public class StarDocument {

    private String uid;

    private String username;

    private String type;

    private String introduce;

    private String content;

    private int age;

    private int sex;

    private long createDate;

    private long updateDate;

    private List<Production> productions;

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "StarDocument{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", introduce='" + introduce + '\'' +
                ", content='" + content + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", productions=" + productions +
                '}';
    }
}
