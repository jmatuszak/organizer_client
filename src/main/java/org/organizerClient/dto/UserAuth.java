package org.organizerClient.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class UserAuth {

    private String userName;
    private String password;
    private String userCredentials;


    public String baseUserCredentials(){
        String auth = userName + ":" + password;
        byte[] encodedAuth = org.apache.tomcat.util.codec.binary.Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        return "Basic " + new String( encodedAuth );
    }

    public String getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(String userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
