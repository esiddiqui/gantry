package com.es.gantry.networks;


import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.base.ObjectListController;
import com.es.gantry.containers.ContainerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class NetworkController extends ObjectListController<Network,NetworkService> {

    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    public NetworkController(NetworkService service, ThreadLocalAuthService threadLocalAuth) {
        super(service,threadLocalAuth);
    }

    @RequestMapping("/api/v1/networks")
    public ResponseEntity<List<Network>> getNetworks(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @RequestParam(value= "filter", defaultValue="")String filter) {
        return super.getObjects(authHeader,filter);
    }

    @RequestMapping("/api/v1/networks/{id}")
    public ResponseEntity<List<HashMap<String,Object>>> inspectContainer(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @PathVariable(value= "id")String id) {
       return super.inspectObjects(authHeader,id);
    }

}
