package com.wjj.elasticsearch.example.service;

import com.wjj.elasticsearch.example.domain.index.Production;
import com.wjj.elasticsearch.example.domain.index.StarDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangjunjie 2019/8/23 17:26
 * @Description:
 * @Version: 1.0.0
 * @Modified By: xxx 2019/8/23 17:26
 */

@Service
public class IndexClassService {


    public StarDocument createStarDocument() {
        List<Production> arrayList = new ArrayList<>();
        Production production = new Production();
        production.setCreateDate(System.currentTimeMillis());
        production.setGrade(34);
        production.setName("啥子电视剧哦");
        production.setType("电视剧");
        /*Production production2 = new Production();
        production2.setCreateDate(System.currentTimeMillis());
        production2.setGrade(100);
        production2.setName("十国千娇");
        production2.setType("电视剧");*/
        //arrayList.add(production);

        StarDocument starDocument = new StarDocument();
        starDocument.setAge(40);
        starDocument.setContent("我不知道它和暴力熊谁好，我只知道这篇文章越到后面评价越简洁，明显是我在偷懒");
        starDocument.setIntroduce("买散热都会自带硅脂，比CPU散片送的垃圾硅脂要好。大霜塔及其大霜塔以下的风冷散热器不要换硅脂，用原装就行");
        starDocument.setCreateDate(System.currentTimeMillis());
        starDocument.setUpdateDate(System.currentTimeMillis());
        starDocument.setUsername("路人ABC");
        starDocument.setType("美女");
        starDocument.setUid("16");
        starDocument.setSex(1);
        starDocument.setProductions(arrayList);
        return starDocument;
    }
}
