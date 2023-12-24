package com.yejh.jcode.sboot.restful.service.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CanalClient {

    private static final String HOSTNAME = "vm5.yejh.cn";
    private static final int PORT = 11111;
    private static final String DESTINATION = "example";
    private static final String FILTER = "sync_vm.person";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EsService esService;

    @PostConstruct
    public void init() {
        try {
            run();
        } catch (Exception e) {
            log.error("exception: {}", e.getMessage(), e);
        }
    }

    /**
     * 实时数据同步
     */
    public void run() throws Exception {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(HOSTNAME, PORT), DESTINATION, StringUtils.EMPTY, StringUtils.EMPTY);
        // 连接
        connector.connect();
        // 订阅数据库
        connector.subscribe(FILTER);

        while (true) {
            // 获取数据
            Message message = connector.get(100);

            List<CanalEntry.Entry> entryList = message.getEntries();
            if (CollectionUtils.isEmpty(entryList)) {
                long sleepTime = 5L;
                log.info("没有数据，休眠: {} sec(s)", sleepTime);
                TimeUnit.SECONDS.sleep(sleepTime);
                continue;
            }
            for (CanalEntry.Entry entry : entryList) {
                // 判断类型是否为 ROWDATA
                if (!Objects.equals(entry.getEntryType(), CanalEntry.EntryType.ROWDATA)) continue;

                // 获取序列化后的数据
                ByteString storeValue = entry.getStoreValue();
                // 反序列化数据
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                // 获取当前事件操作类型
                CanalEntry.EventType eventType = rowChange.getEventType();
                log.info("------ {} 操作 ------", eventType);

                switch (eventType) {
                    case INSERT:
                        List<Person> personList = new ArrayList<>();
                        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                            personList.add(buildPersonByCanalEntry(rowData.getAfterColumnsList()));
                        }
                        // es 批量新增文档
                        esService.index(personList);
                        log.info("新增: {}", personList);
                        break;
                    case UPDATE:
                        List<Person> beforeList = new ArrayList<>();
                        List<Person> afterList = new ArrayList<>();
                        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                            // 更新前
                            beforeList.add(buildPersonByCanalEntry(rowData.getBeforeColumnsList()));
                            // 更新后
                            afterList.add(buildPersonByCanalEntry(rowData.getAfterColumnsList()));
                        }
                        // es 批量更新文档
                        esService.index(afterList);
                        log.info("更新前: {}", beforeList);
                        log.info("更新后: {}", afterList);
                        break;
                    case DELETE:
                        List<String> idList = new ArrayList<>();
                        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                            for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                                if (Objects.equals(column.getName(), "id")) {
                                    idList.add(column.getValue());
                                    break;
                                }
                            }
                        }
                        // es 批量删除文档
                        esService.delete(idList);
                        // 打印删除 id 集合
                        log.info(Arrays.toString(idList.toArray()));
                        break;
                    case CREATE:
                        esService.createIndex();
                        break;
                    case ERASE:
                        esService.deleteIndex();
                        break;
                    default:
                }
            }
        }
    }

    /**
     * 反射构造 {@link Person}
     */
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private Person buildPersonByCanalEntry(List<CanalEntry.Column> columnList) throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        if (CollectionUtils.isEmpty(columnList)) return Person.builder().build();

        Person person = Person.class.newInstance();
        for (CanalEntry.Column col : columnList) {
            String colName = col.getName();
            String reflectName = col.getName();
            if (colName.contains("_")) {
                String[] colArr = colName.split("_");
                // e.g. create_time -> createTime
                reflectName = colArr[0] + colArr[1].substring(0, 1).toUpperCase() + colArr[1].substring(1);
            }
            Field field = Person.class.getDeclaredField(reflectName);
            field.setAccessible(true);
            if (Arrays.asList("timestamp", "date").contains(col.getMysqlType())) {
                field.set(person, Date.from(LocalDateTime.parse(col.getValue(), FORMATTER).atZone(ZoneId.systemDefault()).toInstant()));
            } else if (Arrays.asList("bigint").contains(col.getMysqlType())) {
                field.set(person, Long.valueOf(col.getValue()));
            } else if (Arrays.asList("int").contains(col.getMysqlType())) {
                field.set(person, Integer.valueOf(col.getValue()));
            } else if (Arrays.asList("tinyint").contains(col.getMysqlType())) {
                field.set(person, Short.valueOf(col.getValue()));
            } else if (Arrays.asList("bool").contains(col.getMysqlType())) {
                field.set(person, Objects.equals(col.getValue(), "1"));
            } else {
                field.set(person, col.getValue());
            }
        }

        return person;
    }

}