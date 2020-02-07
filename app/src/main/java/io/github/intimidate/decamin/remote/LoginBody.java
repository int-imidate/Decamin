package io.github.intimidate.decamin.remote;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("password")
    String password;
    @SerializedName("email")
    String email;

    public LoginBody(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
