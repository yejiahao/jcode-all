package com.yejh.jcode.sboot.restful.service.canal;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class EsService {

    private static final String INDEX = "person";

    private static final String TYPE = "personType";

//    @Autowired
//    private ElasticsearchClient client;

    @Autowired
    private RestHighLevelClient client;

    /**
     * es 批量新增、更新文档（不存在：新增， 存在：更新）
     */
    public void index(List<Person> personList) throws IOException {
        /// es v8.x
//        BulkRequest.Builder br = new BulkRequest.Builder();
//        personList.forEach(p -> br.operations(op -> op.index(
//                idx -> idx.index(INDEX)
//                        .id(String.valueOf(p.getId()))
//                        .document(p)
//        )));
//        client.bulk(br.build());
        for (Person person : personList) {
            IndexRequest idxReq = new IndexRequest(INDEX, TYPE, String.valueOf(person.getId()));
            IndexResponse idxResp = client.index(idxReq.source(person, XContentType.JSON), RequestOptions.DEFAULT);
            log.info("idxResp: {}", idxResp);
        }
    }

    /**
     * es 批量删除文档
     */
    public void delete(List<String> idList) throws IOException {
        /// es v8.x
//        BulkRequest.Builder br = new BulkRequest.Builder();
//        idList.forEach(id -> br.operations(op -> op.delete(idx -> idx.index("person").id(id))));
//        client.bulk(br.build());
        for (String id : idList) {
            DeleteResponse delResp = client.delete(new DeleteRequest(INDEX, TYPE, id), RequestOptions.DEFAULT);
            log.info("delResp: {}", delResp);
        }
    }

    /**
     * 创建 index
     */
    public void createIndex() throws IOException {
        CreateIndexResponse createIdxResp = client.indices().create(new CreateIndexRequest(INDEX).settings(
                Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 0)
        ), RequestOptions.DEFAULT);
        log.info("createIdxResp: {}", createIdxResp.isAcknowledged());
    }

    /**
     * 删除 index
     */
    public void deleteIndex() throws IOException {
        AcknowledgedResponse ackResp = client.indices().delete(new DeleteIndexRequest(INDEX), RequestOptions.DEFAULT);
        log.info("ackResp: {}", ackResp.isAcknowledged());
    }
}
