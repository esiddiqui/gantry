package com.es.gantry.auth;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public static Map<String,Auth> authMap = new HashMap<>();

    public Auth getAuthRecordForToken(String token) throws Exception {
        if (AuthService.authMap.containsKey(token)) {
            Auth auth = AuthService.authMap.get(token);
            if (auth.getExpiry().before(new Timestamp(System.currentTimeMillis()))) {
                AuthService.authMap.remove(token);
                throw new Exception("Authentication token has expired");
            } else
                return auth;
        } else
           return null;
    }


    public Auth saveAuthRecord(Auth auth) throws Exception {
        if (auth!=null && auth.getToken()!=null) {
            AuthService.authMap.put(auth.getToken(), auth);
            return getAuthRecordForToken(auth.getToken());
        } else
            throw new Exception("Null auth object or token provided");
    }

}
