package com.es.gantry.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class KeyController {

    @Value("${gantry.certs.path}")
    private String certsPath;

    private Logger logger = LoggerFactory.getLogger(KeyController.class);

    @RequestMapping("/api/v1/keys")
    public ResponseEntity<List<String>> getKeys() {
        List<String> keys = new ArrayList<>();
        for (String key: new File(certsPath).list()) {
            keys.add(key);
        }
        return ResponseEntity.ok(keys);
    }
}
