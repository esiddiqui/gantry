package com.es.gantry.containers;

import com.es.gantry.auth.Auth;
import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.ssh.SshConnectionService;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShellDockerClientContainerService implements ContainerService {

    private static final Logger logger = LoggerFactory.getLogger(ShellDockerClientContainerService.class);

    private static final String CONTAINER_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private static final String CONTAINER_LS_WITH_FORMAT =
            "docker ps --format=\"{{.ID}},{{.Image}},{{.Command}},{{.CreatedAt}},{{.Status}},{{.Ports}},{{.Names}},{{.Networks}}\"";

    private static final String FILTER_ARG = " --filter ";

    private Shell shell = new JSchShell();

    @Autowired
    SshConnectionService sshConnectionService;

    @Autowired
    ThreadLocalAuthService authService;

    public List<Container> findAll() {
        return findAll(null);
    }

    public List<Container> findAll(String filter) {
        List<Container> containers = new ArrayList<>();

        try {

            Auth auth = this.authService.getAuth();
            if (auth==null)
                throw new Exception("Error in authentication");

            String host = auth.getHost();
            String user = auth.getUser();
            String key = auth.getKey();

            ExecuteRequest request =
                    this.sshConnectionService
                            .getConnectionForHost(host, user, key);

            String command = CONTAINER_LS_WITH_FORMAT;
            if (!StringUtils.isEmpty(filter)) {
                command += FILTER_ARG + filter;
            }
            request.setCommand(Arrays.asList(command));
            ExecuteResult rs = shell.execute(request);
            if (rs.getReturnStatus() == ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL &&
                    rs.getExitStatus() == 0) {
                for (String line : rs.getOut()) {

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
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return containers;
    }


    public Optional<Container> findById(String imageId) {
        return this.findAll().stream()
                .filter(c->c.getId().trim().equalsIgnoreCase(imageId.trim()))
                .findFirst();
    }

}
