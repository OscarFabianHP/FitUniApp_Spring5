package co.edu.unab.fituni.repository;

import java.util.List;

import co.edu.unab.fituni.modelo.Usuario;
import co.edu.unab.fituni.pojo.Authorization;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetAuthorization {

    @Headers("Cache-Control: no-cache")
    @POST("auth")
    Call<Authorization> getAuthorization(@Body Usuario usuario);

    @Headers("Cache-Control: no-cache")
    @POST("auth/create")
    Call<Authorization> createAdmin(@Body Usuario usuario);

    @Headers("Cache-Control: no-cache")
    @GET("auth/{username}/{password}")
    Call<List<Authorization>> login(@Path("username") String user, @Path("password") String pass);
}
