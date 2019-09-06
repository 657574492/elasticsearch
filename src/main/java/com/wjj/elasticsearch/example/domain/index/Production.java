package com.wjj.elasticsearch.example.domain.index;

/**
 * @Author: wangjunjie 2019/8/23 16:01
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/23 16:01
 */
public class Production {

    private String type;

    private String name;

    private int grade;

    private long createDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Production{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", createDate=" + createDate +
                '}';
    }
}
