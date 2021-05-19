package org.organizerClient.dto;

import java.util.Base64;

public class UserAuth {

    private String userName;
    private String password;

    public UserAuth(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String baseUserCredentials(){
        String encodedCredentials = Base64.getEncoder().encodeToString(String.format("%s:%s",userName,password).getBytes());
        return String.format("Basic %s",encodedCredentials);
    }
}
