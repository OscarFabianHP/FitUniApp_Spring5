package co.edu.unab.fituni.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Persona implements Parcelable{

    @SerializedName("id")
    private Long id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido")
    private String apellido;
    @SerializedName("email")
    private String email;
    @SerializedName("esAdmin")
    private Boolean esAdmin;
    @SerializedName("fechaRegistro")
    private String fechaRegistro;

    public Persona(){}

    public Persona(Parcel in){
        nombre=in.readString();
        apellido=in.readString();
        email=in.readString();
        fechaRegistro=in.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(email);
        dest.writeString(fechaRegistro);
    }

    public static final Parcelable.Creator<Persona> CREATOR =
            new Parcelable.Creator<Persona>() {
                public Persona createFromParcel(Parcel in){
                    return new Persona(in);
                }

                @Override
                public Persona[] newArray(int size) {
                    return new Persona[size];
                }
            };
}
