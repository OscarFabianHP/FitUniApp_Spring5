package co.edu.unab.fituni.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

//Clase parceable para pasar datos facilmente entre distintas activitys
public class IndiceMasaMuscular implements Parcelable {
    @SerializedName("estatura")
    private Double estatura;
    @SerializedName("peso")
    private Double peso;
    @SerializedName("imc")
    private Double imc;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("idPersona")
    private Persona persona;

    public IndiceMasaMuscular(Double estatura, Double peso, Double imc, String fecha, Persona persona) {
        this.estatura = estatura;
        this.peso = peso;
        this.imc = imc;
        this.fecha = fecha;
        this.persona = persona;
    }

    public IndiceMasaMuscular(Parcel in){
        //String[] data = new String[4];
        //SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

        //in.readStringArray(data);
        estatura = in.readDouble(); //Double.parseDouble(data[0]);
        peso = in.readDouble();//Double.parseDouble(data[1]);
        imc = in.readDouble();//Double.parseDouble(data[2]);
        //try {
        fecha = in.readString();//dateFormat.parse(data[3]);
        persona = (Persona) in.readValue(Persona.class.getClassLoader());
       // } catch (ParseException e) {
         //   e.printStackTrace();
        //}

    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getEstatura() {
        return estatura;
    }

    public Double getPeso() {
        return peso;
    }

    public Double getImc() {
        return imc;
    }

    public String getFecha() {
        return fecha;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(estatura);
        dest.writeDouble(peso);
        dest.writeDouble(imc);
        dest.writeString(fecha);
        dest.writeValue(persona);
    }

    public static final Parcelable.Creator<IndiceMasaMuscular> CREATOR =
            new Parcelable.Creator<IndiceMasaMuscular>() {
        public IndiceMasaMuscular createFromParcel(Parcel in){
            return new IndiceMasaMuscular(in);
        }

                @Override
                public IndiceMasaMuscular[] newArray(int size) {
                    return new IndiceMasaMuscular[size];
                }
            };
}
