package co.edu.unab.fituni.repository;

import java.util.List;
import java.util.Optional;

import co.edu.unab.fituni.modelo.Persona;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PersonaRepository {

    @GET("personas")
    Call<List<Persona>> readAllPersonas(@Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("personas/{id}")
    Call<Persona> readPersona(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("personas/email/{email}")
    Call<Persona> readPersona(@Path("email") String email, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @POST("personas")
    Call<Persona> createPersona(@Body Persona persona, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @PUT("personas/{id}")
    Call<Persona> updatePersona(@Path("id") Long id, @Body Persona persona, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @DELETE("personas/{id}")
    Call<String> deletePersona(@Path("id") Long id, @Header("Authorization") String token);

}
