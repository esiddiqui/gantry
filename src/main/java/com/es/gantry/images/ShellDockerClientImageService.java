package com.es.gantry.images;

import com.es.gantry.ssh.SshConnectionService;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ShellDockerClientImageService implements ImageService {

    Shell shell = new JSchShell();

    @Autowired
    private SshConnectionService sshConnectionService;

    private static Logger logger = LoggerFactory.getLogger(ShellDockerClientImageService.class);

    private static final String IMAGE_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private static final String IMAGE_LS_WITH_FORMAT =
            "docker images --format=\"{{.ID}},{{.Repository}},{{.Tag}},{{.CreatedAt}},{{.CreatedSince}},{{.Size}}\"";

    @Override
    public List<Image> findAll() {
        return this.findAll(null);
    }


    @Override
    public List<Image> findAll(String filter) {
        List<Image> images = new ArrayList<>();

        try {
            String host = "nerf001.core.int.bf1.corp.pvt";
            String user = "ansible";
            String key = "integration_ansible.pem";
            ExecuteRequest request =
                    this.sshConnectionService.getConnectionForHost(host,user, key);

            request.setCommand(Arrays.asList(IMAGE_LS_WITH_FORMAT));
            ExecuteResult rs = shell.execute(request);
            if (rs.getReturnStatus() == ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL &&
                    rs.getExitStatus() == 0) {
                for (String line : rs.getOut()) {
                    Image img = new Image();
                    String[] fields = line.split(",");
                    img.setId(fields[0]);
                    img.setRepository(fields[1]);
                    img.setTag(fields[2]);
                    String ts = fields[3];
                    try {
                        Date createdAtDate =
                                new SimpleDateFormat(IMAGE_CREATED_DATE_FORMAT)
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
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return images;
    }




    @Override
    public Optional<Image> findById(String imageId) {
        return this.findAll().stream().filter(img->img.getId().equalsIgnoreCase(imageId))
                .findFirst();
    }

}
