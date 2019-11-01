package com.es.gantry.auth;

import com.es.gantry.ssh.SshConnectionService;
import com.es.shell.ExecuteRequest;
import com.es.shell.ExecuteResult;
import com.es.shell.JSchShell;
import com.es.shell.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

@RestController
public class AuthController {

    private static final long SIX_HOURS = 6 * 60 * 60 * 1000;

    private static final String TEST_SSH_CMD = "hostname -f";

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private Shell shell = new JSchShell();

    @Autowired
    private SshConnectionService sshConnectionService;

    @Autowired
    private AuthService authService;

    @RequestMapping(method = RequestMethod.POST, path="/api/v1/auth/tokens",
            produces = "application/json", consumes = "application/json")
    public ResponseEntity<Auth> getToken(@RequestBody Auth authRequest) {
        Auth auth = new Auth();
        try {
            String host = authRequest.getHost();
            String user = authRequest.getUser();
            String key = authRequest.getKey();
            ExecuteRequest request =
                    this.sshConnectionService.getConnectionForHost(host,user,key);
            request.setCommand(Arrays.asList(TEST_SSH_CMD));
            ExecuteResult rs = shell.execute(request);
            if (rs.getReturnStatus()== ExecuteResult.CommandStatus.COMMAND_RAN_SUCCESSFUL) {
                auth.setHost(host);
                auth.setKey(key);
                auth.setUser(user);
                auth.setToken(UUID.randomUUID().toString());
                auth.setExpiry(new Timestamp(System.currentTimeMillis()+SIX_HOURS));
                this.authService.saveAuthRecord(auth);
                return ResponseEntity.status(HttpStatus.OK).body(auth);
            } else {
                String message = rs.getOut().get(0);
                throw new Exception(rs.getReturnStatus() + " - " + message);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            auth.setMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(auth);
        }
    }

}
