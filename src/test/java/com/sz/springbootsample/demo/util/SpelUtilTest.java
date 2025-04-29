package com.sz.springbootsample.demo.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Yanghj
 * @date 2025/4/29 11:40
 */
public class SpelUtilTest {

    @Test
    public void testSpel() {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("cloud_type", "hybrid_cloud");
        contextMap.put("source", "private_cloud");
        contextMap.put("type", "event");
        contextMap.put("severity", "p1");
        contextMap.put("event", "host_offline");

        String expressionString =
                "(['cloud_type'] == 'private_cloud' || (['cloud_type'] == 'hybrid_cloud' && ['source'] == 'private_cloud')) && {'event', 'risk', 'threshold'}.contains(['type']) && !{'host_offline', 'storage_offline'}.contains(['event']) && {'p1', 'p2'}.contains(['severity'])";
        contextMap.put("event", "vm_paused_io_error");
        Boolean result = SpelUtil.getInstance().getBooleanValue(contextMap, expressionString);
        assertTrue(result);
    }
}
