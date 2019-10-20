package com.es.gantry.containers;

import com.es.gantry.images.Image;
import com.es.gantry.ssh.SshConnectionManager;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShellDockerClientContainerService implements ContainerService {

    Shell shell = new JSchShell();

    private static final String CONTAINER_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private static final String IMAGE_LS_WITH_FORMAT =
            "docker ps --format=\"{{.ID}},{{.Image}},{{.Command}},{{.CreatedAt}},{{.Status}},{{.Ports}},{{.Names}},{{.Networks}}\"";


    public List<Container> findAll() {
        return findAll(null);
    }

    public List<Container> findAll(String filter) {
        List<Container> containers = new ArrayList<>();
        ExecuteRequest request =
                SshConnectionManager.getConnectionForHost("nerf001.core.int.bf1.corp.pvt");

        request.setCommand(Arrays.asList(IMAGE_LS_WITH_FORMAT));
        ExecuteResult rs = shell.execute(request);
        if (rs.getReturnStatus()== ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL &&
                rs.getExitStatus()== 0)  {
            for (String line:rs.getOut()) {

                Container container = new Container();
                String[] fields = line.split(",");
                container.setId(fields[0]);
                container.setImage(fields[1]);
                container.setCommand(fields[2]);

                String ts = fields[3];
                try {
                    Date createdAtDate =
                            new SimpleDateFormat(CONTAINER_CREATED_DATE_FORMAT)
                                    .parse(ts.substring(0, ts.lastIndexOf(' ')));
                    container.setCreated(new Timestamp(createdAtDate.getTime()));
                } catch (Exception ex) {
                    System.err.println("Error parsing container created at");
                }
                container.setStatus(fields[4]);
                container.setPorts(fields[5]);
                container.setNames(fields[6]);
                container.setNetworks(fields[7]);
                containers.add(container);
            }
        }
        return containers;
    }


    public Optional<Container> findById(String imageId) {
        return this.findAll().stream()
                .filter(c->c.getId().trim().equalsIgnoreCase(imageId.trim()))
                .findFirst();
    }

}
