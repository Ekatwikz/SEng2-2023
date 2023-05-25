package pw.react.backend.controller;

import static java.util.stream.Collectors.joining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

public abstract class BaseController {
    protected static Logger log;

    public BaseController() {
       log = LoggerFactory.getLogger(getClass());
    }

    protected void logHeaders(@RequestHeader HttpHeaders headers) {
        log.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(",")));
    }
}
