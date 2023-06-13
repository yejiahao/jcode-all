package com.yejh.jcode.base.flowctrl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * pushagent 流控模型
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-12-25
 * @since x.y.z
 */
@SuppressWarnings({"unused", "StatementWithEmptyBody"})
@Slf4j
public class FlowCtrl {

    private static final String API_PUSH_BY_UID = "push_msg_to_uid";
    private static final String API_PUSH_BY_UID_BATCH = "batch_push_msg_to_uid";
    private static final String API_PUSH_BY_HDID_BATCH = "batch_push_msg_to_hdid";
    private static final String API_PUSH_BY_UID_VAR = "batch_push_msg_to_var";
    private static final String API_PUSH_BY_TOKEN = "push_msg_to_token";
    private static final String API_QUERY_RESULT = "query_result";

    private static final String API_PUSH_BY_HDID = "push_msg_to_hdid";
    private static final String API_PUSH_BY_TOKEN_VAR = "batch_push_msg_to_var_token";
    private static final String API_PUSH_BY_HDID_VAR = "batch_push_msg_to_var_hdid";

    private static final int INTERVAL = 10; // 10s
    private static final int MAX_SAMPLE_COUNT = 60 / INTERVAL; // 1分钟内最大样本数

    private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "checkAppFlow"));

    private Map<Integer, Map<String, LinkedList<Integer>>> appIdApiMinCount = new ConcurrentHashMap<>();

    private Map<Integer, Set<String>> appIdInFlowCtrlApi = new ConcurrentHashMap<>();

    private final FlowCtrlConfig flowCtrlConfig = new FlowCtrlConfig();

    @PostConstruct
    public void init() {
        ses.scheduleAtFixedRate(this::onTimerCheckAppFlowCtrl, INTERVAL, INTERVAL, TimeUnit.SECONDS);
    }

    public void addCount(int appId, String api, int count) {
        Map<String, LinkedList<Integer>> apiMinCnt = appIdApiMinCount.computeIfAbsent(appId, k -> new ConcurrentHashMap<>());
        List<Integer> lstCount = apiMinCnt.computeIfAbsent(api, k -> new LinkedList<>());
        if (lstCount.isEmpty()) {
            lstCount.add(0);
        }

        lstCount.set(lstCount.size() - 1, lstCount.get(lstCount.size() - 1) + count);

        /// TODO 上报指标
//        agent->GetMetricsAll()->AddCounter(api, "api", count); // 汇总统计
//        agent->GetMetrics(appId)->AddCounter(api, "API", count); // 业务方 uid 计划发送量
    }

    public boolean isAppReachFlowCtrl(int appId, String api) {
        Set<String> apiSet = appIdInFlowCtrlApi.get(appId);
        if (Objects.isNull(apiSet)) return false;
        return apiSet.contains(api);
    }

    private void onTimerCheckAppFlowCtrl() {
        appIdApiMinCount.forEach((appId, apiAndCount) -> {
            if (Objects.nonNull(apiAndCount)) {
                Iterator<Map.Entry<String, LinkedList<Integer>>> iterator = apiAndCount.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, LinkedList<Integer>> entry = iterator.next();
                    String api = entry.getKey();
                    LinkedList<Integer> lstMinCount = entry.getValue();

                    // 计算 1 分钟内的 QPS
                    int sum = 0;
                    int qps;
                    for (Integer count : lstMinCount) {
                        sum += count;
                    }
                    if (sum > 0) {
                        qps = sum / (lstMinCount.size() * INTERVAL);
                    } else {
                        iterator.remove();
                        continue;
                    }

                    // 更新 appid 流控列表
                    int threshold = 0;
                    if (flowCtrlConfig.isAppFlowCtrlSwitch()) {
                        if (StringUtils.equalsAny(api, API_PUSH_BY_UID, API_PUSH_BY_UID_BATCH, API_PUSH_BY_UID_VAR)) {
                            threshold = flowCtrlConfig.getApiQpsThresholdUid();
                        } else if (Objects.equals(api, API_PUSH_BY_TOKEN)) {
                            threshold = flowCtrlConfig.getApiQpsThresholdToken();
                        } else if (Objects.equals(api, API_QUERY_RESULT)) {
                            threshold = flowCtrlConfig.getApiQpsThresholdQuery();
                        } else {
                            // 其他暂不做流控
                        }
                    }

                    Set<String> apiInCtrl = appIdInFlowCtrlApi.computeIfAbsent(appId, k -> new HashSet<>());
                    if (threshold > 0 && qps >= threshold) {
                        log.info("appid={}, api={}, QPS={} larger then threshold={}", appId, api, qps, threshold);
                        if (flowCtrlConfig.isAppFlowCtrlSwitch()) {
                            if (!apiInCtrl.contains(api)) {
                                log.info("appid={}, api={}, will be in flow ctrl", appId, api);
                                apiInCtrl.add(api);
                            }
                        }
                    } else {
                        if (apiInCtrl.contains(api)) {
                            log.info("appid={}, api={}, QPS={} smaller then threshold={}, remove from flow ctrl list", appId, api, qps, threshold);
                            apiInCtrl.removeIf(e -> Objects.equals(e, api));
                        }
                    }

                    log.info("appid={}, api={}, average QPS={}, sum={}", appId, api, qps, sum);

                    // 保存 1 分钟内的统计
                    if (lstMinCount.size() >= MAX_SAMPLE_COUNT) {
                        // 删除最早的一个分钟统计项
                        lstMinCount.removeFirst();
                    }

                    // 新增加一个统计项
                    lstMinCount.addLast(0);
                }
            }
        });
    }

    @Data
    static class FlowCtrlConfig {

        private boolean appFlowCtrlSwitch = false;

        private int flowCtrlMethod = 1; // 流控方式。1-提交到独立 mq 队列控制消费速度；2-返回流控错误给业务端，丢弃请求

        private int apiQpsThresholdUid = 3000;

        private int apiQpsThresholdToken = 1;

        private int apiQpsThresholdQuery = 200;
    }

}
