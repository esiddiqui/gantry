package com.es.gantry.base;

import com.es.gantry.auth.ThreadLocalAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

public abstract class ObjectListController<T,S extends ObjectListService<T>> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectListController.class);

    protected S service;

    protected ThreadLocalAuthService threadLocalAuth;

    public ObjectListController(S service, ThreadLocalAuthService threadLocalAuth) {
        this.service = service;
        this.threadLocalAuth = threadLocalAuth;
    }


    public  ResponseEntity<List<T>> getObjects(String authHeader,String filter) {
        try {
            if (StringUtils.isEmpty(authHeader))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            this.threadLocalAuth.lookupAuth(authHeader);
            List<T> ts = this.service.findAll(filter);
            if (ts.size() > 0)
                return ResponseEntity.ok(ts);
            else
                return ResponseEntity.ok(ts);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    public ResponseEntity<List<HashMap<String,Object>>> inspectObjects(String authHeader,String id) {
        try {
            if (StringUtils.isEmpty(authHeader))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            this.threadLocalAuth.lookupAuth(authHeader);
            List<HashMap<String,Object>> containers = this.service.inspect(id);
            if (containers!=null)
                return ResponseEntity.ok(containers);
            else
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }
}
