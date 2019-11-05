package com.es.gantry.base;

import com.es.gantry.auth.Auth;
import com.es.gantry.auth.ThreadLocalAuthService;
import com.es.gantry.ssh.SshConnectionService;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public abstract class AbstractObjectListService<T> implements ObjectListService<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractObjectListService.class);

    protected static final String FILTER_ARG = " --filter ";

    protected static final String CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    @Autowired
    protected SshConnectionService sshConnectionService;

    @Autowired
    protected ThreadLocalAuthService authService;

    protected Shell shell = new JSchShell();

    @Override
    public List<T> findAll(String filter) {
        List<T> containers = new ArrayList<>();
        try {
            String command = this.getFindAllCliCommand();
            if (!StringUtils.isEmpty(filter)) {
                command += FILTER_ARG + filter;
            }
            ExecuteResult rs = this.exec(command);
            if (rs.getReturnStatus() == ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL &&
                    rs.getExitStatus() == 0) {
                List<T> list = this.getObjectListExtractor().extract(rs.getOut());
                return list;
            }
            return containers;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return containers;
    }

    @Override
    public List<T> findAll() {
        return findAll(null);
    }

    @Override
    public Optional<T> findById(String id) {
        List<T> objects = this.findAll(this.getFindByIdFilterString(id));
        if (objects.size()==0)
            return Optional.empty();
        else
            return Optional.of(objects.get(0));
    }

    @Override
    public List<HashMap<String,Object>> inspect(String id) {
        try {
            ExecuteResult rs = this.exec(String.format(this.getInspectCliCommand(id)));
            if (rs.getReturnStatus()== ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL) {
                StringBuilder output = new StringBuilder();
                rs.getOut().stream().forEach(output::append);
                logger.info(output.toString());
                String json = output.toString();
                List<HashMap<String,Object>> containers = new ObjectMapper().readValue(
                        json,new TypeReference<List<HashMap<String,Object>>>(){});
                return containers;
            } else
                return null;
        } catch (Exception ex) {
            return null;
        }
    }


    protected ExecuteResult exec(String command) throws Exception {
        Auth auth = this.authService.getAuth();
        if (auth == null)
            throw new Exception("Error in authentication");

        String host = auth.getHost();
        String user = auth.getUser();
        String key = auth.getKey();

        ExecuteRequest request =
                this.sshConnectionService
                        .getConnectionForHost(host, user, key);

        request.setCommand(Arrays.asList(command));
        return shell.execute(request);
    }

    /** implementing classes to return a string to pass to findAll(String) to filer by id */
    protected abstract String getFindByIdFilterString(String id);

    protected abstract String getFindAllCliCommand();

    protected abstract String getInspectCliCommand(String id);

    protected abstract ObjectListExtractor<T> getObjectListExtractor();
}
