package co.edu.unab.fituni.repository;

import java.util.List;
import java.util.Optional;

import co.edu.unab.fituni.modelo.IndiceMasaMuscular;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IndiceMasaMuscularRepository {

    @GET("registros")
    Call<List<IndiceMasaMuscular>> readAllRegistros(@Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("registros/{id}")
    Call<IndiceMasaMuscular> readRegistro(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @GET("registros/idpersona/{id}")
    Call<List<IndiceMasaMuscular>> readAllRegistrosPersona(@Path("id") Long id, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @POST("registros")
    Call<IndiceMasaMuscular> createRegistro(@Body IndiceMasaMuscular registro, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @POST("registros/list")
    Call<List<IndiceMasaMuscular>> createListaRegistro(@Body List<IndiceMasaMuscular> registros, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @PUT("registros/{id}")
    Call<IndiceMasaMuscular> updateRegistro(@Path("id") Long id, @Body IndiceMasaMuscular registro, @Header("Authorization") String token);

    @Headers("Cache-Control: no-cache")
    @DELETE("registros/{id}")
    Call<String> deleteRegistro(@Path("id")Long id, @Header("Authorization") String token);
}

