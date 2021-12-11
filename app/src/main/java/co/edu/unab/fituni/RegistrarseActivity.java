package co.edu.unab.fituni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import co.edu.unab.fituni.modelo.Persona;
import co.edu.unab.fituni.modelo.UsuarioBienestarU;
import co.edu.unab.fituni.network.MyBackendAPIClient;
import co.edu.unab.fituni.repository.PersonaRepository;
import co.edu.unab.fituni.repository.UsuarioBienestarURepo;
import co.edu.unab.fituni.repository.UsuarioBienestarURepoImple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarseActivity extends AppCompatActivity {
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy KK:mm:ss a", Locale.US);
    UsuarioBienestarURepo usuarioBienestarURepo = new UsuarioBienestarURepoImple();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        EditText txtNombre = findViewById(R.id.textNombreReg);
        EditText txtApellido = findViewById(R.id.textApellidoReg);
        EditText editEmail = findViewById(R.id.editTxtEmailReg);
        Switch swAdmin = findViewById(R.id.switchAdmin);
        EditText txtCodeAdmin = findViewById(R.id.editTextCodeAdmin);
        EditText txtPassAdmin = findViewById(R.id.editTextPassAdmin);
        ImageButton imageButtonCode = findViewById(R.id.imageButtonCode);

        Button botonEnviar = findViewById(R.id.buttonEnviarReg);

        swAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swAdmin.isChecked()){
                    txtCodeAdmin.setVisibility(View.VISIBLE);
                    imageButtonCode.setVisibility(View.VISIBLE);
                }
                else{
                   txtCodeAdmin.setVisibility(View.GONE);
                   imageButtonCode.setVisibility(View.GONE);
                }
            }
        });

        imageButtonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeUser = txtCodeAdmin.getText().toString().trim();
                Callbacks callbacksCodigoAdmin = new Callbacks() {
                    @Override
                    public void onSuccess(Object object) {
                        String codeDB = (String) object;
                        if (Objects.equals(codeUser, codeDB) && codeUser!=null) { //evita exception nullpointexception
                            txtCodeAdmin.setEnabled(false);
                            txtPassAdmin.setVisibility(View.VISIBLE);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "¡El codigo no coincide!, pruebe otro!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.d("Fallo", "No se pudo obtener el codigo de la Firestore", exception);
                    }
                };
                usuarioBienestarURepo.comprobarCodigoAdmin(codeUser, callbacksCodigoAdmin);
               /* db = FirebaseFirestore.getInstance();
                db.collection("codigo_bienestaru")
                        .document("codigo")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    ;
                                    if (Objects.equals(codeUser, codeDB)) { //evita exception nullpointeception
                                        txtCodeAdmin.setEnabled(false);
                                        txtPassAdmin.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                    Log.d("Fallo", "No se pudo realizar la comprobación del codigo");
                            }
                        });*/
            }
        });

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha =  dateFormat.format(Calendar.getInstance().getTime()); //obtiene fecha y hora actual
                String nombre = txtNombre.getText().toString().trim();
                String apellido = txtApellido.getText().toString().trim();
                String email = editEmail.getText().toString().toLowerCase().trim();
                String pass = txtPassAdmin.getText().toString().trim();

                Persona personaDatos = new Persona();
                personaDatos.setNombre(nombre);
                personaDatos.setApellido(apellido);
                personaDatos.setEmail(email);
                personaDatos.setFechaRegistro(fecha);

                if(swAdmin.isChecked() && !txtCodeAdmin.isEnabled()) { //si se ha ingresado codigo de registro Usuario Bienestar correctamente
                    personaDatos.setEsAdmin(true);

                    Callbacks consultarUsuarioAdmin = new Callbacks() {
                        @Override
                        public void onSuccess(Object object) {
                            DocumentSnapshot resultadoConsulta = (DocumentSnapshot) object;

                            if(((DocumentSnapshot) object).exists()){ //si existe un registro en administradores con ese email
                                Toast.makeText(RegistrarseActivity.this, "El correo ya esta registrado, prueba con otro", Toast.LENGTH_LONG).show();
                                Log.d("Fallo", "Correo ya esta en uso por alguien más "+resultadoConsulta.getId());
                            }
                            else {
                                crearUsuario(personaDatos); //agrega los datos del admin a tabla persona
                                crearAdmin(email, pass); //agrega los datos en Firestore bienestar universitario
                            }
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Log.d("Fallo", "Fallo consulta para verificar si correo ya existe", exception);
                        }
                    };
                    usuarioBienestarURepo.consultarDocumento(email, consultarUsuarioAdmin); //consulta si existe documento con email en administradores
                }
                else {
                    personaDatos.setEsAdmin(false);
                    crearUsuario(personaDatos);
                }
/*
                //guarda datos de la persona en la base de datos MySQL sea administrador o no;
                PersonaRepository personaRepo = MyBackendAPIClient.getRetrofit().create(PersonaRepository.class);

                //obtinen token de la cache android
                SharedPreferences sharedPrefe = getPreferences(MODE_PRIVATE);
                String token = MainActivity.getBearerToken();//sharedPrefe.getString("token", "");

                Call<Persona> persona = personaRepo.createPersona(personaDatos, token);
                persona.enqueue(new Callback<Persona>() {
                    @Override
                    public void onResponse(Call<Persona> call, Response<Persona> response) {
                        Log.d("Exito", "Se registro Persona Correctamente");
                        Toast.makeText(RegistrarseActivity.this, "Registro éxitoso!, ahora inicie sesion con el correo", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Persona> call, Throwable t) {
                        Log.d("Fallo", "Fallo al agregar registro de Persona", t);
                        txtNombre.setText("");
                        txtApellido.setText("");
                        editEmail.setText("");
                        Toast.makeText(RegistrarseActivity.this, "Ocurrio error al registrarse, por favor vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

    }

    private void crearUsuario(Persona personaDatos){
        //guarda datos de la persona en la base de datos MySQL sea administrador o no;
        PersonaRepository personaRepo = MyBackendAPIClient.getRetrofit().create(PersonaRepository.class);

        //obtinen token de la cache android
        SharedPreferences sharedPrefe = getPreferences(MODE_PRIVATE);
        String token = MainActivity.getBearerToken();//sharedPrefe.getString("token", "");

        Call<Persona> persona = personaRepo.createPersona(personaDatos, token);
        persona.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, Response<Persona> response) {
                Log.d("Exito", "Se registro Persona Correctamente");
                Toast.makeText(RegistrarseActivity.this, "Registro éxitoso!, ahora inicie sesion con el correo", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Log.d("Fallo", "Fallo al agregar registro de Persona", t);
                Toast.makeText(RegistrarseActivity.this, "Ocurrio error al registrarse, por favor vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //agrega registro de usuario y contrasenia de usuario administrador
    private void crearAdmin(String email, String password){
        Callbacks crearAdmin = new Callbacks() {
            @Override
            public void onSuccess(Object object) {
                UsuarioBienestarU admin = (UsuarioBienestarU) object;
                Log.d("Exito", "Se ha creado exitosamente el usuario Admin "+admin.getUsuario());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d("Fallo", "No se ha creado usuario admin", exception);
            }
        };
        if(password!=null){
            UsuarioBienestarU usuario = new UsuarioBienestarU(email, password);
            usuarioBienestarURepo.crear(usuario, crearAdmin);
        }
        else
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

    }
}