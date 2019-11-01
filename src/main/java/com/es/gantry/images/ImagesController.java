package com.es.gantry.images;

import com.es.gantry.auth.ThreadLocalAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ImagesController {

    @Autowired
    ImageService service;

    @Autowired
    ThreadLocalAuthService threadLocalAuth;

    private static final Logger logger = LoggerFactory.getLogger(ImagesController.class);

    @RequestMapping("/api/v1/images")
    public ResponseEntity<List<Image>> getImages(
            @RequestHeader(value = "Authorization", defaultValue = "") String authHeader,
            @RequestParam(value= "filter", defaultValue="")String filter) {
        try {
            if (StringUtils.isEmpty(authHeader))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            this.threadLocalAuth.lookupAuth(authHeader);

            List<Image> images = this.service.findAll(filter);
            if (images.size() > 0)
                return ResponseEntity.ok(images);
            else
                return ResponseEntity.ok(images);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

}
