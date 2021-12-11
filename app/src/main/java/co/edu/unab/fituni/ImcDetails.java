package co.edu.unab.fituni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import co.edu.unab.fituni.modelo.IndiceMasaMuscular;
import co.edu.unab.fituni.modelo.Persona;

public class ImcDetails extends AppCompatActivity {

    private TextView txtFecha;
    private TextView txtImc;
    private TextView txtPeso;
    private TextView txtEstatura;
    private ImageView imagen;
    private Button btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_details);

        //obtiene Extra "regItem" enviado en Intent desde RegistroActivity.java
        IndiceMasaMuscular registro = getIntent().getParcelableExtra("regItem");

        txtFecha = (TextView) findViewById(R.id.textFechaDetail);
        txtImc = (TextView) findViewById(R.id.text_imc_details);
        txtPeso = (TextView) findViewById(R.id.textPesoDetails);
        txtEstatura = (TextView) findViewById(R.id.textEstaturaDetails);
        imagen = (ImageView) findViewById(R.id.imageDetails);
        btnCerrar = (Button) findViewById(R.id.buttonCerrarDetails);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        double imc = registro.getImc();
        if(imc<18.5)
            imagen.setImageResource(R.drawable.delgadoicon);
        else if(imc>=18.5 && imc<=24.9)
            imagen.setImageResource(R.drawable.normalicon2);
        else if(imc>=25.0 && imc<=29.9)
            imagen.setImageResource(R.drawable.sobrepesoicon);
        else if(imc>=30.0)
            imagen.setImageResource(R.drawable.gordoicon);

        if(MainActivity.getCorreoPersona()!=null) //solo muestra correo de usuario si se ha iniciado session
            txtFecha.setText(String.format("FECHA:%n%s%nUsuario: %s", registro.getFecha(), registro.getPersona().getEmail()));
        else
            txtFecha.setText(String.format("FECHA:%n%s%nUsuario: %s", registro.getFecha(), "inicie sesión para agregarlos a su registro"));
        txtImc.setText(String.format("IMC:%n%.1f", registro.getImc()));
        txtPeso.setText(String.format("PESO:%n%.1f Kg", registro.getPeso()));
        txtEstatura.setText(String.format("ESTATURA:%n%s Cm", registro.getEstatura()*100 ));

    }
}