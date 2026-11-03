package com.bridge.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Checker {

    public static String validateInstituteHeader(Map<String, String> headers) {
        String institute = headers.get("institute");
        checkNull(institute, "Institute missing in header");
        return institute;
    }

    public static void checkNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkBlank(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkBlankJson(String json, String message) {
        if (StringUtils.isBlank(json) || "{}".equals(json.trim())) {
            throw new IllegalArgumentException(message);
        }
    }
}
