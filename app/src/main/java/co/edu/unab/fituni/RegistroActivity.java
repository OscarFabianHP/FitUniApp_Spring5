package co.edu.unab.fituni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import co.edu.unab.fituni.modelo.IndiceMasaMuscular;
import co.edu.unab.fituni.network.MyBackendAPIClient;
import co.edu.unab.fituni.repository.IndiceMasaMuscularRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    static List<IndiceMasaMuscular> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        IndiceMasaMuscularRepository imcRepo = MyBackendAPIClient.getRetrofit().create(IndiceMasaMuscularRepository.class);

        if(MainActivity.getCorreoPersona()==null){ //si aun no se ha inicia sesion
            List<IndiceMasaMuscular> registros = getIntent().getParcelableArrayListExtra("datosLista");
            list = registros; //copia la lista de registros actuales a list
            mostrarRegistros(); //muestra la lista de registros del momento
        }
        else { //si ya ha iniciado sesion

            if (MainActivity.getUserPersona().getEsAdmin() == true) { //si el usuario es administrador
                Call<List<IndiceMasaMuscular>> registrosPersonas = imcRepo.readAllRegistros(MainActivity.getBearerToken()); //obtiene todos los registros de todos los usuarios
                registrosPersonas.enqueue(new Callback<List<IndiceMasaMuscular>>() {
                    @Override
                    public void onResponse(Call<List<IndiceMasaMuscular>> call, Response<List<IndiceMasaMuscular>> response) {
                        list = response.body();
                        Log.d("Exito", "GET All IMC Personas se ejecuto exitosamente");
                        if (registrosPersonas.isExecuted())
                            mostrarRegistros(); //muestra todos los registros
                    }

                    @Override
                    public void onFailure(Call<List<IndiceMasaMuscular>> call, Throwable t) {
                        Log.d("Fallo", "GET All IMC Personas falló", t);
                    }
                });
            } else { //si no es administrador
                Call<List<IndiceMasaMuscular>> registrosPersona = imcRepo.readAllRegistrosPersona(MainActivity.getUserPersona().getId(), MainActivity.getBearerToken());//obtiene registro del usuario
                registrosPersona.enqueue(new Callback<List<IndiceMasaMuscular>>() {
                    @Override
                    public void onResponse(Call<List<IndiceMasaMuscular>> call, Response<List<IndiceMasaMuscular>> response) {
                        list = response.body();
                        Log.d("Exito", "GET All IMC de Persona se ejecuto exitosamente");
                        if (registrosPersona.isExecuted())
                            mostrarRegistros();
                    }

                    @Override
                    public void onFailure(Call<List<IndiceMasaMuscular>> call, Throwable t) {
                        Log.d("Fallo", "GET All IMC de Persona falló", t);
                    }
                });
            }
        }
        //ArrayList<IndiceMasaMuscular> list = getIntent().getParcelableArrayListExtra("datosLista"); //obtiene lista de registro desde MainActivity

/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IndiceMasaMuscular regItem = list.get(position); //obtendo elemento que corrsponde al item clickeado;
                Intent intent = new Intent(RegistroActivity.this, ImcDetails.class );
                intent.putExtra("regItem", regItem);
                //intent.putExtras(savedInstanceState);
                startActivity(intent);
            }
        });*/
    }

    private void mostrarRegistros(){
        RecyclerView listaRecycler = (RecyclerView) findViewById(R.id.lista_recycler);
        ImcAdapter adapter = new ImcAdapter(list);
        listaRecycler.setAdapter(adapter);
        listaRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        listaRecycler.setHasFixedSize(true);

        //instaciar y sobreescritura del metodo es esencial para que funcione el listener del RecyclerView de más abajo
        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        listaRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if(child != null && gestureDetector.onTouchEvent(e)){
                    int position = listaRecycler.getChildAdapterPosition(child); //obtinen posicion del click
                    IndiceMasaMuscular regItem = list.get(position); //obtiene elemento de la lista que corresponde al item clickeado;
                    Intent intent = new Intent(RegistroActivity.this, ImcDetails.class );
                    intent.putExtra("regItem", regItem);
                    //intent.putExtras(savedInstanceState);
                    startActivity(intent);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}