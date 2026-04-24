package com.modulersx;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@MapperScan("com.modulersx.repository")
public class ModuleRsxServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ModuleRsxServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ModuleRsxServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("module-rsx-server started successfully on http://localhost:8082");
    }
}
