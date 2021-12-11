package co.edu.unab.fituni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import co.edu.unab.fituni.modelo.IndiceMasaMuscular;
import co.edu.unab.fituni.modelo.Persona;
import co.edu.unab.fituni.modelo.Usuario;
import co.edu.unab.fituni.network.MyBackendAPIClient;
import co.edu.unab.fituni.pojo.Authorization;
import co.edu.unab.fituni.repository.GetAuthorization;
import co.edu.unab.fituni.repository.IndiceMasaMuscularRepository;
import co.edu.unab.fituni.repository.PersonaRepository;
import co.edu.unab.fituni.repository.UsuarioBienestarURepo;
import co.edu.unab.fituni.repository.UsuarioBienestarURepoImple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText estaturaText;
    private EditText pesoText;
    private EditText passText;
    private Switch swBoton;
    private static Double imc;
    private ArrayList<IndiceMasaMuscular> listaReg;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy KK:mm:ss a", Locale.US);
    static String correoPersona, bearerToken;
    static Persona userPersona;
    Usuario usuarioAccess = new Usuario();
    Authorization au;
    private UsuarioBienestarURepo usuarioBienestarURepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioBienestarURepo = new UsuarioBienestarURepoImple(); //accede a Firebase

        //Obtinen TOKEN BEARER
        GetAuthorization authRepo = MyBackendAPIClient.getRetrofit().create(GetAuthorization.class);

        //ATENCIÓN: creo las credenciales usuario y contrasenia que deben existir previamente en la tabla usuario
        // de la base de datos para poder obtener token
        //Usuario usuario = new Usuario();
        usuarioAccess.setUsername("admin");
        usuarioAccess.setPassword("admin");
        new Thread(new Runnable(){ //permite ejecutar el execute() si interrupciones de network
            @Override
            public void run() {
                try {
        Call<List<Authorization>> login = authRepo.login(usuarioAccess.getUsername(), usuarioAccess.getPassword()); //verifica para mirar si se requiere crear usuario o existe ya
        Response<List<Authorization>> respuesta = login.execute();
        if(respuesta.body().isEmpty()){ //si no se ha agregado las credenciales en la tabla usuario para obtener token
            Call<Authorization> create = authRepo.createAdmin(usuarioAccess); //crea usuario y lo agrega a la base de datos
            create.enqueue(new Callback<Authorization>() {
                @Override
                public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                    au = response.body();
                    usuarioAccess.setUsername(au.getUser());
                    usuarioAccess.setPassword(au.getPass());
                    Log.d("Exito", "Se creo usuario para obtener token");

                    Call<Authorization> auth = authRepo.getAuthorization(usuarioAccess); //obtiene token
                    auth.enqueue(new Callback<Authorization>() {
                        @Override
                        public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                            bearerToken = response.body().getToken(); //obtiene token Bearer

                            //guardo en cache el token para para solicitarlo desde otras actividades
                            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token", bearerToken); //pares de valores
                            editor.commit(); //aqui lo guarda ya en cache
                            Log.d("Exito", "Se obtuvo corectamente el Token " + bearerToken);
                        }

                        @Override
                        public void onFailure(Call<Authorization> call, Throwable t) {
                            Log.d("Fallo", "Fallo obtencion del Bearer Token", t);
                        }
                    });

                }

                @Override
                public void onFailure(Call<Authorization> call, Throwable t) {
                    Log.d("Fallo", "No se pudo autenticar");
                }
            });
        } else { //si ya existen credenciales es decir ya esta autorizado para obtener Token
            Call<Authorization> auth = authRepo.getAuthorization(usuarioAccess); //obtiene token
            auth.enqueue(new Callback<Authorization>() {
                @Override
                public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                    if (response.isSuccessful()) {
                        bearerToken = response.body().getToken(); //obtiene token Bearer

                        //guardo en cache el token para para solicitarlo desde otras actividades
                        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", bearerToken); //pares de valores
                        editor.commit(); //aqui lo guarda ya en cache
                        Log.d("Exito", "Se obtuvo corectamente el Token " + bearerToken);
                    }
                }

                @Override
                public void onFailure(Call<Authorization> call, Throwable t) {
                    Log.d("Fallo", "Fallo obtencion del Bearer Token", t);
                }
            });
        }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        /*//forma asyncrona
        login.enqueue(new Callback<List<Authorization>>() {
            @Override
            public void onResponse(Call<List<Authorization>> call, Response<List<Authorization>> response) {
                if(response.isSuccessful()) { //si usuario no esta logueado o no existe en base de datos para poder obtener token
                    if (response.body() == null) {
                        Call<Authorization> create = authRepo.createAdmin(usuarioAccess); //crea usuario
                        create.enqueue(new Callback<Authorization>() {
                            @Override
                            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                                au = response.body();
                                usuarioAccess.setUsername(au.getUser());
                                usuarioAccess.setPassword(au.getPass());
                                Log.d("Exito", "Se creo usuario para obtener token");

                                Call<Authorization> auth = authRepo.getAuthorization(usuarioAccess); //obtiene token
                                auth.enqueue(new Callback<Authorization>() {
                                    @Override
                                    public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                                        bearerToken = response.body().getToken(); //obtiene token Bearer

                                        //guardo en cache el token para para solicitarlo desde otras actividades
                                        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("token", bearerToken); //pares de valores
                                        editor.commit(); //aqui lo guarda ya en cache
                                        Log.d("Exito", "Se obtuvo corectamente el Token " + bearerToken);
                                    }

                                    @Override
                                    public void onFailure(Call<Authorization> call, Throwable t) {
                                        Log.d("Fallo", "Fallo obtencion del Bearer Token", t);
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<Authorization> call, Throwable t) {
                                Log.d("Fallo", "No se pudo obtener autenticar");
                            }
                        });
                    } else { //si ya existen credenciales es decir ya esta autorizado para obtener Token
                        Call<Authorization> auth = authRepo.getAuthorization(usuarioAccess); //obtiene token
                        auth.enqueue(new Callback<Authorization>() {
                            @Override
                            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                                if (response.isSuccessful()) {
                                    bearerToken = response.body().getToken(); //obtiene token Bearer

                                    //guardo en cache el token para para solicitarlo desde otras actividades
                                    SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("token", bearerToken); //pares de valores
                                    editor.commit(); //aqui lo guarda ya en cache
                                    Log.d("Exito", "Se obtuvo corectamente el Token " + bearerToken);
                                }
                            }

                            @Override
                            public void onFailure(Call<Authorization> call, Throwable t) {
                                Log.d("Fallo", "Fallo obtencion del Bearer Token", t);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Authorization>> call, Throwable t) {
                Log.d("Fallo", "No se pudo comprobar login usuario");
            }
        });*///fin forma asyncrona

        /*Call<Authorization> create = authRepo.createAdmin(usuario);
        create.enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                au = response.body();
                usuarioAccess.setUsername(au.getUser());
                usuarioAccess.setPassword(au.getPass());


                Call<Authorization> auth = authRepo.getAuthorization(usuarioAccess);
                auth.enqueue(new Callback<Authorization>() {
                    @Override
                    public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                        bearerToken = response.body().getToken(); //obtiene token Bearer

                        //guardo en cache el token para para solicitarlo desde otras actividades
                        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", bearerToken); //pares de valores
                        editor.commit(); //aqui lo guarda ya en cache
                        Log.d("Exito", "Se obtuvo corectamente el Token "+bearerToken);
                    }

                    @Override
                    public void onFailure(Call<Authorization> call, Throwable t) {
                        Log.d("Fallo", "Fallo obtencion del Bearer Token", t);
                    }
                });

            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                Log.d("Fallo", "No se pudo obtener autenticar");
            }
        });*/
        //Call<Authorization> auth = authRepo.getAuthorization(usuario);


        listaReg = new ArrayList<>(); //crea lista donde se añadiran los registros
        estaturaText = (EditText) findViewById(R.id.editTextNumber);
        pesoText = (EditText) findViewById(R.id.editTextNumber2);
        passText = findViewById(R.id.editTextTextPassword);

        Button botonCalcular = (Button) findViewById(R.id.button2);
        Button botonRegistros = (Button) findViewById(R.id.button3);
        Button botonRegistrarse = findViewById(R.id.buttonRegistrarse);
        swBoton = findViewById(R.id.switchBienestar);

        EditText campoEmail = findViewById(R.id.editTextEmailAddress);
        ImageButton imageButtonSesion = findViewById(R.id.imageButton);
        ImageButton imageButtonSalir = findViewById(R.id.imageButtonSalir);
        imageButtonSalir.setVisibility(View.INVISIBLE);

/*      //una forma de que guarde en correo del usuario para iniciar sin tener que reingresar (no implementado del todo)
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        correoPersona = sharedPref.getString("correo", null);
        if(correoPersona!=null){
            PersonaRepository personaRepo = MyBackendAPIClient.getRetrofit().create(PersonaRepository.class);
            Call<Persona> personaSesion = personaRepo.readPersona(correoPersona, bearerToken);
            personaSesion.enqueue(new Callback<Persona>() {
                @Override
                public void onResponse(Call<Persona> call, Response<Persona> response) {
                    userPersona = response.body();
                    Log.d("Exito", "Usuario con sesión activa");
                    campoEmail.setEnabled(false);
                }

                @Override
                public void onFailure(Call<Persona> call, Throwable t) {
                    Log.d("Fallo", "Fallo al obtener datos del usuario", t);
                    campoEmail.setText("");
                    Toast.makeText(MainActivity.this, "Correo no esta registrado!!!, por favor registrese", Toast.LENGTH_SHORT);
                }
            });
            campoEmail.setText(correoPersona);
            campoEmail.setEnabled(false);
        }*/


        botonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imcCalcular();
               Intent intentResultado = new Intent(MainActivity.this, ResultadoImc.class);
               startActivity(intentResultado);
               estaturaText.setText(""); //limpia el campo estatura
               pesoText.setText(""); //limpia el campo peso
               estaturaText.requestFocus(); //posiciona focus del cursor en campo estatura
            }
        });

        botonRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(userPersona!=null) { //verifica si existe un usuario logueado antes de mostrar registros
                    Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                    intent.putParcelableArrayListExtra("datosLista", listaReg);
                    //intent.putExtras(savedInstanceState);
                    startActivity(intent);
                //}
                //else
                //    Log.d("Fallo", "Debe inciar sesion antes para consultar registros");
            }
        });

        swBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swBoton.isChecked()){
                    passText.setVisibility(View.VISIBLE);
                }
                else
                    passText.setVisibility(View.GONE);
            }
        });

        //al hacer click me envia a al formulario de registro de Persona
        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
                startActivity(intent);
            }
        });

        imageButtonSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String correo="";
                PersonaRepository personaRepo = MyBackendAPIClient.getRetrofit().create(PersonaRepository.class);
                correoPersona = campoEmail.getText().toString().toLowerCase().trim();
                //Guarda correo en cache para futuro
                SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("correo", correoPersona); //pares de valores
                editor.commit(); //aqui lo guarda ya en cache

                Call<Persona> personaSesion = personaRepo.readPersona(correoPersona, bearerToken);
                personaSesion.enqueue(new Callback<Persona>() {
                    @Override
                    public void onResponse(Call<Persona> call, Response<Persona> response) {
                        if(response.body()!=null && response.body().getEsAdmin()==false) { //comprueba que si exista la cuenta del usuario ingresada
                            userPersona = response.body();
                            Log.d("Exito", "Usuario con sesión activa");
                            campoEmail.setEnabled(false);
                            imageButtonSesion.setVisibility(View.GONE);
                            imageButtonSalir.setVisibility(View.VISIBLE);

                            agregarLista(); //agrega lista de datos de imc calculados antes de iniciar sesion a la base de datos del usuario
                        }
                        else if(response.body()!=null && response.body().getEsAdmin()==true){ //comprueba si existe cuenta y si es administrador
                            Callbacks iniciarSesionBienestar = new Callbacks() {
                                @Override
                                public void onSuccess(Object object) {
                                    DocumentSnapshot docUsario = (DocumentSnapshot) object;
                                    String passIngresado = passText.getText().toString();
                                    String passUsuario = docUsario.getString("password");
                                    if(Objects.equals(passIngresado, passUsuario)){ //si el password coincide
                                        userPersona = response.body();
                                        Log.d("Exito", "Usuario Admin a iniciado sesión");
                                        campoEmail.setEnabled(false);
                                        passText.setEnabled(false);
                                        swBoton.setEnabled(false);
                                        imageButtonSesion.setVisibility(View.GONE);
                                        imageButtonSalir.setVisibility(View.VISIBLE);
                                        agregarLista(); //agrega lista de datos de imc calculados antes de iniciar sesion a la base de datos del usuario
                                    }
                                    else{ //si el password no coincide
                                        Toast.makeText(MainActivity.this, "Password erroneo", Toast.LENGTH_SHORT).show();
                                        passText.setText("");
                                    }
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    Log.d("Fallo", "No se podo comprobar acceso adminitrador", exception);
                                }
                            };
                            usuarioBienestarURepo.consultarDocumento(correoPersona, iniciarSesionBienestar);  //realiza la consulta en la Firebase de usuarios de bienestar universitario
                        }
                        else if(response.body()==null){ //si no existe el correo ingresa
                            campoEmail.setText("");
                            passText.setText("");
                            Toast.makeText(MainActivity.this, "Correo no esta registrado!!!, por favor registrese", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Persona> call, Throwable t) {
                        Log.d("Fallo", "Fallo al obtener datos del usuario", t);
                        userPersona=null;
                        correoPersona=null;
                        campoEmail.setText("");
                        passText.setText("");
                        Toast.makeText(MainActivity.this, "Fallo la comprobación de las credenciales", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        imageButtonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoPersona=null; //reinicia correo del usuario a vacio
                userPersona=null; //reincia usuario a null
                campoEmail.setEnabled(true);
                passText.setEnabled(true);
                swBoton.setEnabled(true);
                campoEmail.setText("");
                passText.setText("");
                imageButtonSalir.setVisibility(View.GONE);
                imageButtonSesion.setVisibility(View.VISIBLE);
                Log.d("Exito", "sesión terminada por usuario");
                Toast.makeText(MainActivity.this, "Sesión terminada", Toast.LENGTH_LONG).show();
            }
        });
    }


    //metodo calcula IMC y añade los datos a la lista de registro
    private void imcCalcular(){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        Double peso=0.0;
        Double estatura=0.0;
        //Date fecha=null; //inicializa fecha

        estatura = Double.parseDouble(estaturaText.getText().toString().trim())/100;
        peso = Double.parseDouble(pesoText.getText().toString().trim());
        imc = peso/(Math.pow(estatura, 2));
        imc = Math.round(imc*10.0)/10.0; //redondea a un solo decimal
        //String imcFormated = decimalFormat.format(imc)  ;
        //Double imcD = Double.parseDouble(imcFormated);
        //try {
         String fecha =  dateFormat.format(Calendar.getInstance().getTime()); //obtiene fecha y hora actual
        //} catch (ParseException e) {
         //   e.printStackTrace();
        //}
        if (userPersona != null) {
            IndiceMasaMuscular datosImc = new IndiceMasaMuscular(estatura, peso, imc, fecha, userPersona);
            IndiceMasaMuscularRepository imcRepo = MyBackendAPIClient.getRetrofit().create(IndiceMasaMuscularRepository.class);
            Call<IndiceMasaMuscular> registro = imcRepo.createRegistro(datosImc, bearerToken); //agrega registro a la base de datos
            registro.enqueue(new Callback<IndiceMasaMuscular>() {
                @Override
                public void onResponse(Call<IndiceMasaMuscular> call, Response<IndiceMasaMuscular> response) {
                    Log.d("Exito", "se agrego registro de imc a la base de datos");
                }

                @Override
                public void onFailure(Call<IndiceMasaMuscular> call, Throwable t) {
                    Log.d("Fallo", "no se agrego registro de imc a la base de datos", t);
                }
            });
        }
        else if(userPersona==null) {//si no se ha logueado
            listaReg.add(new IndiceMasaMuscular(estatura, peso, imc , fecha, userPersona)); //añade registro nuevo a lista
            Log.d("Exito", "reg imc a lista");}
        }

        private void agregarLista(){
            if(listaReg!=null){ //si usuario registro datos sin haberse logueado primero
                List<IndiceMasaMuscular> listaFull=new ArrayList<>();
                for(IndiceMasaMuscular imc : listaReg) {//a cada registro de la lista
                    imc.setPersona(userPersona); //agrega campo Persona a los registros IMC
                    listaFull.add(imc);
                }
                listaReg=new ArrayList<>(); //limpia registros antes de inicio de sesion
                IndiceMasaMuscularRepository imcRepo = MyBackendAPIClient.getRetrofit().create(IndiceMasaMuscularRepository.class);
                Call<List<IndiceMasaMuscular>> createAllList = imcRepo.createListaRegistro(listaFull, bearerToken); //agrega a registro del usuario la lista de registros de imc que se recopilo antes de iniciar sesion
                createAllList.enqueue(new Callback<List<IndiceMasaMuscular>>() {
                    @Override
                    public void onResponse(Call<List<IndiceMasaMuscular>> call, Response<List<IndiceMasaMuscular>> response) {
                        Log.d("Exito", "Se agrego lista de registros imc de la persona");
                    }

                    @Override
                    public void onFailure(Call<List<IndiceMasaMuscular>> call, Throwable t) {
                        Log.d("Fallo", "No se pudo agregar lista de registros");
                    }
                });
            }
        }

    public static Double getImc() {
        return imc;
    }

    public static String getBearerToken() {
        return bearerToken;
    }

    public static String getCorreoPersona() {
        return correoPersona;
    }

    public static Persona getUserPersona() {
        return userPersona;
    }
}