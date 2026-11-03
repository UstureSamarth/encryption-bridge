package com.bridge;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    @Autowired
    ApplicationContext context;

    @Getter
    String name;

    @PostConstruct
    public void test() {
//        String[] beans = context.getBeanNamesForType(WorkflowParticipant.class);
//        log.info("{}", Arrays.toString(beans));
        name = "Hello World";
    }

    public void reload() {
        name = "Hello World updated";
    }

    static void main() {

    }

}
