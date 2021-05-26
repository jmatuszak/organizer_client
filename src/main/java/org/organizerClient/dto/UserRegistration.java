package org.organizerClient.dto;

import java.util.List;

public class UserRegistration {

    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;
    private List<String> roles;

    public UserRegistration(String firstName, String lastName, String email, String login, String password, List<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}


/*
{
        "id": "10",
        "firstName": "pawe2l1",
        "lastName": "Gaweł1",
        "email": "pawel12@wp.pl",
        "login": "pawel1223",
        "password": "qwertyuio",
        "roles": [
        "USER"
        ]
        }*/
