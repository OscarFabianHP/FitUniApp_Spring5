package co.edu.unab.fituni.pojo;

import com.google.gson.annotations.SerializedName;

public class Authorization {
    @SerializedName("access") //se mapea nombre segun se llame en el backend de springboot
    private String token;

    @SerializedName("username")
    private String user;

    @SerializedName("password")
    private String pass;

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
