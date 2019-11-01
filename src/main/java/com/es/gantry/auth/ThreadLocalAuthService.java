package com.es.gantry.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ThreadLocalAuthService {

    private ThreadLocal<Auth> threadLocalAuth = new ThreadLocal<>();

    @Autowired
    AuthService authService;

    public void lookupAuth(String token) throws Exception {
        try {
            if (StringUtils.isEmpty(token))
                throw new Exception("Empty token provided");
             String[] splits = token.split(" ");
             if (splits.length!=2)
                 throw new Exception("Invalid auth string");
             if (!splits[0].trim().equalsIgnoreCase("Bearer"))
                 throw new Exception("Must have a Bearer keyword");

             String authToken = splits[1].trim();
            Auth auth = this.authService.getAuthRecordForToken(authToken);
            threadLocalAuth.set(auth);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Auth getAuth() {
        return threadLocalAuth.get();
    }

}
