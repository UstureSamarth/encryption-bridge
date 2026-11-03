package com.bridge.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bridge.constants.BridgeConstants.STAN_REQUIRED_TXN;
import static com.bridge.error.Checker.checkNull;

@Slf4j
public final class TxnNameHelper {

    private static final AtomicInteger sequence = new AtomicInteger();

    public static String getTxnName(Map<Integer, String> msg) {
        if (msg != null) {
            String txnName = getMtiPart(msg) + "." + getProcessingCodePart(msg);
            String txnType = getField125Part(msg);
            if (txnType != null) {
                txnName = txnName + "." + txnType;
            }
            handleStanIfRequired(msg, txnName);
            return txnName;
        }
        return null;
    }

    private static String getMtiPart(Map<Integer, String> msg) {
        String mti = msg.get(0);
        return (mti != null && mti.length() > 1) ? mti.substring(1) : "";
    }

    private static String getProcessingCodePart(Map<Integer, String> msg) {
        String procCode = msg.get(3);
        return (procCode != null && procCode.length() >= 2) ? procCode.substring(0, 2) : "";
    }

    private static String getField125Part(Map<Integer, String> msg) {
        String fld125 = msg.get(125);
        return (fld125 != null && !fld125.isBlank()) ? fld125.trim() : null;
    }

    private static void handleStanIfRequired(Map<Integer, String> request, String txnName) {
        String stan = request.get(11);
        if (!STAN_REQUIRED_TXN.contains(txnName)) {
            if (stan == null) {
                stan = nextCode();
                request.put(11, stan);
                log.info("STAN ={}", stan);
            }
            return;
        }
        checkNull(stan, "STAN (field 11) is absent");
    }

    private static String nextCode() {
        long time = System.currentTimeMillis() % 1_000_000;
        int seq = sequence.getAndIncrement() % 1000; // 3 digits
        long combined = time * 1000 + seq; // 6 digits time + sequence
        return String.format("%06d", combined % 1_000_000);
    }
}
