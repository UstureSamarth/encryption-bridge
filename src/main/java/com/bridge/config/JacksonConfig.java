package com.bridge.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class JacksonConfig {

    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL))
                .build();
    }

    static void main() {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.objectMapper();
        Map<Integer, String> map = new HashMap<>();
        map.put(100, "A");
        map.put(1, "B");
        map.put(50, "C");
        map.put(2, "D");
        System.out.println(mapper.writeValueAsString(map));
        ObjectMapper mapper1 = new ObjectMapper();
        System.out.println(mapper1.writeValueAsString(map));
    }
}
