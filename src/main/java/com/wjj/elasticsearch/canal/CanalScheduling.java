package com.wjj.elasticsearch.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.wjj.elasticsearch.shop.dao.ShopModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
@Slf4j
public class CanalScheduling  {

    @Autowired
    private ShopModelMapper shopModelMapper;
    @Autowired
    private CanalConnector myCanalConnector;
    @Autowired
    private RestHighLevelClient rhlClient;


    @PostConstruct
    public void run() {
        while (true) {
            long batchId = -1;
            try {
                int batchSize = 1000;
                Message message = myCanalConnector.getWithoutAck(batchSize);
                batchId = message.getId();
                List<CanalEntry.Entry> entries = message.getEntries();
                if (batchId != -1 && entries.size() > 0) {
                    for (CanalEntry.Entry entry : entries) {
                        if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                            publishCanalEvent(entry);
                        }
                    }
                }
                myCanalConnector.ack(batchId);
            } catch (Exception e) {
                log.error("发送监听事件失败！batchId回滚,batchId=" + batchId, e);
                myCanalConnector.rollback(batchId);
            }

        }
    }

    private void publishCanalEvent(CanalEntry.Entry entry) throws IOException {
        CanalEntry.RowChange change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        for (CanalEntry.RowData rowData : change.getRowDatasList()) {
            handleEvent(entry,rowData);

        }

    }

    private void handleEvent(CanalEntry.Entry entry, CanalEntry.RowData rowData) throws IOException {
        CanalEntry.EventType eventType = entry.getHeader().getEventType();
        String database = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();
        switch (eventType) {
            case INSERT:
            case UPDATE:
                Map<String, Object> afterColumnsValue = getAfterColumnsValue(rowData);
                saveES(afterColumnsValue, database, table);
                break;
            case DELETE:
                Map<String, Object> beforeColumnsValue = getBeforeColumnsValue(rowData);
                delEs(beforeColumnsValue,database,table);
                break;
            default:
                break;
        }
    }

    private Map<String, Object> getAfterColumnsValue(CanalEntry.RowData rowData) {
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        return parseColumnsToMap(afterColumnsList);
    }

    private Map<String, Object> getBeforeColumnsValue(CanalEntry.RowData rowData) {
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        return parseColumnsToMap(beforeColumnsList);
    }

    private Map<String, Object> parseColumnsToMap(List<CanalEntry.Column> columns) {
        Map<String, Object> jsonMap = new HashMap<>();
        columns.forEach(column -> {
            if (column != null) {
                jsonMap.put(column.getName(), column.getValue());
            }
        });
        return jsonMap;
    }

    private void delEs(Map<String, Object> dataMap, String database, String table) throws IOException {
        if (!StringUtils.equals("imooc_dianping", database)) {
            return;
        }
        if (dataMap == null || dataMap.size() == 0) {
            return;
        }
        if ("shop".equals(table)) {
            DeleteRequest deleteRequest = new DeleteRequest("shop");
            deleteRequest.id(String.valueOf(dataMap.get("id")));
            rhlClient.delete(deleteRequest, RequestOptions.DEFAULT);
        }
    }

    private void saveES(Map<String, Object> dataMap, String database, String table) throws IOException {
        if (!StringUtils.equals("imooc_dianping", database)) {
            return;
        }
        if (dataMap == null || dataMap.size() == 0) {
            return;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        if (StringUtils.equals("seller", table)) {
            result = shopModelMapper.buildESQuery(new Integer((String) dataMap.get("id")), null, null);
        } else if (StringUtils.equals("category", table)) {
            result = shopModelMapper.buildESQuery(null, new Integer((String) dataMap.get("id")), null);
        } else if (StringUtils.equals("shop", table)) {
            result = shopModelMapper.buildESQuery(null, null, new Integer((String) dataMap.get("id")));
        } else {
            return;
        }

        for (Map<String, Object> map : result) {
            IndexRequest indexRequest = new IndexRequest("shop");
            indexRequest.id(String.valueOf(map.get("id")));
            indexRequest.source(map);
            rhlClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
}
