package org.example.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@ConfigurationProperties("cinema")
@Getter@Setter
@Component
public class CinemaProperties {
    private int totalRows;
    private int totalColumns;

    @PostConstruct
    void logLoaded() {
        log.info("== totalRows = {}", totalRows);
        log.info("== totalColumns = {}", totalColumns);
    }
}
