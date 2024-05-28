package com.example.halalscan.Opening;

public class HelperClass {
    String u_name, u_email, u_password, u_confirmPassword;

    public HelperClass() {
    }

    public HelperClass(String name, String email, String password, String confirmPassword) {
        this.u_name = name;
        this.u_email = email;
        this.u_password = password;
        this.u_confirmPassword = confirmPassword;
    }

    public String getName() {
        return u_name;
    }

    public void setName(String name) {
        this.u_name = name;
    }

    public String getEmail() {
        return u_email;
    }

    public void setEmail(String email) {
        this.u_email = email;
    }

    public String getPassword() {
        return u_password;
    }

    public void setPassword(String password) {
        this.u_password = password;
    }

    public String getConfirmPassword() {
        return u_confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.u_confirmPassword = confirmPassword;
    }
}
