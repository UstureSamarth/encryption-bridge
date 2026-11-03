package com.bridge.helper;

import com.bridge.pojo.RequestInfo;

import java.util.concurrent.atomic.AtomicInteger;

public final class RequestHelper {

    private static final AtomicInteger REQUEST_ID = new AtomicInteger(10000);

    public static String getRequestReceivedInfo(String id, String institute, String bc, String api, String txnName) {
        return id + " Received request | " + getPrintInfo(institute, bc, api, txnName);
    }

    public static String getPrintInfo(String institute, String bc, String api, String txnName) {
        return "institute=" + institute +
                " | bc=" + bc +
                " | api=" + api +
                " | txn=" + txnName;
    }

    public static String getTimeTaken(String id, long start) {
        long timeTaken = (System.nanoTime() - start) / 1_000_000;
        return id + " Request completed | timeTaken= " + timeTaken + " ms";
    }

    public static RequestInfo getRequestInfo(String request, String institute, String bc, String api, String txnName) {
        return RequestInfo.builder()
                .request(request)
                .institute(institute)
                .bc(bc)
                .txnName(txnName)
                .api(api)
                .build();
    }

    public static String getRequestId() {
        int requestId = REQUEST_ID.incrementAndGet();
        if (requestId > 99999) {
            REQUEST_ID.set(10000);
        }
        return "[ " + requestId + " ]";
    }
}
