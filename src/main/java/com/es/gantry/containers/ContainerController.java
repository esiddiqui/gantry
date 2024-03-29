package com.es.gantry.containers;


import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.base.ObjectListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ContainerController extends ObjectListController<Container,ContainerService> {

    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    public ContainerController(ContainerService service, ThreadLocalAuthService authService) {
        super(service,authService);
    }

    @RequestMapping("/api/v1/containers")
    public ResponseEntity<List<Container>> getContainers(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @RequestParam(value= "filter", defaultValue="")String filter) {
        return super.getObjects(authHeader, filter);

    }

    @RequestMapping("/api/v1/containers/{id}")
    public ResponseEntity<List<HashMap<String,Object>>> inspectContainer(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @PathVariable(value= "id")String id) {
       return this.inspectObjects(authHeader,id);
    }

}
