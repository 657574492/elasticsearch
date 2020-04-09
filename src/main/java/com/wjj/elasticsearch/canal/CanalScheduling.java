package com.wjj.elasticsearch.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wjj.elasticsearch.shop.dao.ShopModelMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjunjie
 * @date 2020/4/7
 * @description TODO
 */

@Component
@EnableScheduling
public class CanalScheduling implements Runnable {

    @Autowired
    private ShopModelMapper shopModelMapper;
    @Autowired
    private CanalConnector myCanalConnector;
    @Autowired
    private RestHighLevelClient rhlClient;


    @Override
    @Scheduled(fixedDelay = 1000)
    public void run() {
        long batchId = -1;
        try {
            int batchSize = 1000;
            Message message = myCanalConnector.getWithoutAck(batchSize);
            batchId = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            if (batchId != -1 && entries.size() > 0) {
                entries.forEach(entry -> {
                    if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                        publishCanalEvent(entry);
                    }
                });
            }
            myCanalConnector.ack(batchId);
        } catch (Exception e) {
            e.printStackTrace();
            myCanalConnector.rollback(batchId);
        }
    }

    private void publishCanalEvent(CanalEntry.Entry entry) {
        CanalEntry.EventType eventType = entry.getHeader().getEventType();
        String database = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();
        CanalEntry.RowChange change = null;

        try {
            change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return;
        }

        change.getRowDatasList().forEach(rowData -> {
            List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
            String primaryKey = "id";
            CanalEntry.Column idColumn = columns.stream().filter(column -> column.getIsKey()
                    && primaryKey.equals(column.getName())).findFirst().orElse(null);
            Map<String, Object> dataMap = parseColumnsToMap(columns);
            try{
                indexES(dataMap,database,table);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, Object> parseColumnsToMap(List<CanalEntry.Column> columns) {
        Map<String, Object> jsonMap = new HashMap<>();
        columns.forEach(column -> {
            if (column != null) {
                jsonMap.put(column.getName(),column.getValue());
            }
        });
        return jsonMap;
    }

    private void indexES(Map<String,Object> dataMap,String database,String table) throws IOException {
        if(!StringUtils.equals("imooc_dianping",database)){
            return;
        }

        List<Map<String,Object>> result = new ArrayList<>();
        if(StringUtils.equals("seller",table)){
            result = shopModelMapper.buildESQuery(new Integer((String)dataMap.get("id")),null,null);
        }else if(StringUtils.equals("category",table)){
            result = shopModelMapper.buildESQuery(null,new Integer((String)dataMap.get("id")),null);
        }else if(StringUtils.equals("shop",table)){
            result = shopModelMapper.buildESQuery(null,null,new Integer((String)dataMap.get("id")));
        }else{
            return;
        }

        for(Map<String,Object>map : result){
            IndexRequest indexRequest = new IndexRequest("shop");
            indexRequest.id(String.valueOf(map.get("id")));
            indexRequest.source(map);
            rhlClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
}
