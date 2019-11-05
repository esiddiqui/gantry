package com.es.gantry.images;

import com.es.gantry.base.AbstractObjectListService;
import com.es.gantry.base.ObjectListExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SshDockerCliImageService extends AbstractObjectListService<Image> implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(SshDockerCliImageService.class);

    private String LS_COMMAND =
            "docker images --format=\"{{.ID}},{{.Repository}},{{.Tag}},{{.CreatedAt}},{{.CreatedSince}},{{.Size}}\"";

    private String INSPECT_COMMAND = "docker inspect %s";

    protected String getFindByIdFilterString(String id) {
        return "id=%s";
    }

    protected String getInspectCliCommand(String id) {
        return String.format(INSPECT_COMMAND,id);
    }

    protected String getFindAllCliCommand() {
        return LS_COMMAND;
    }

    protected ObjectListExtractor<Image> getObjectListExtractor() {
        return out->{
            List<Image> images = new ArrayList<>();
            for (String line : out) {
                Image img = new Image();
                String[] fields = line.split(",");
                img.setId(fields[0]);
                img.setRepository(fields[1]);
                img.setTag(fields[2]);
                String ts = fields[3];
                try {
                    Date createdAtDate =
                            new SimpleDateFormat(CREATED_DATE_FORMAT)
                                    .parse(ts.substring(0, ts.lastIndexOf(' ')));
                    img.setCreated(new Timestamp(createdAtDate.getTime()));
                } catch (Exception ex) {
                    System.err.println("Error parsing image created at");
                }
                img.setCreatedString(fields[4]);
                String sizeStr = fields[5];
                sizeStr = sizeStr.substring(0, sizeStr.indexOf("MB")).trim();
                img.setSize(Double.parseDouble(sizeStr) * 1024 * 1024);
                images.add(img);
            }
            return images;
        };
    }
}
