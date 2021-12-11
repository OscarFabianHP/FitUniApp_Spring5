package co.edu.unab.fituni.modelo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//modela los datos q guarda en Firebase de los usuarios de Bienestar Universitario
public class UsuarioBienestarU {
    private String usuario;
    private String pass;

    public UsuarioBienestarU(){}

    public UsuarioBienestarU(String usuario, String pass) {
        this.usuario = usuario;
        this.pass = pass;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRefDoc(){
        return this.usuario;
    }

    public Map<String, Object> getMapUser(){
        Map<String, Object> map =new HashMap<>();
        map.put("user", this.usuario);
        map.put("password", this.pass);
        map.put("fecha ultimo acceso", new Date());

        return map;
    }
}
