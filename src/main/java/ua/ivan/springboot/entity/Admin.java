package ua.ivan.springboot.entity;

public class Admin {
    String login;
    int password;


    public Admin() {
        this.login = "admin";
        this.password = "admin".hashCode();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
