package com.wjj.elasticsearch.shop.model;

/**
 * @author EDZ
 * @date 2020/4/7
 * @description TODO
 */
public class IkWordModel {

    private Integer id;

    private String word;

    private Long time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
