package co.edu.unab.fituni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ResultadoImc extends AppCompatActivity {

    private static final int[] FLACO_DRAWABLE = {R.drawable.flaca, R.drawable.flaco, R.drawable.flacoagudelo};
    private static final int[] NORMAL_DRAWABLE = {R.drawable.normal_imc_mujer, R.drawable.normal_imc_mujer2, R.drawable.normal_imc_mujer3,
            R.drawable.normal_imc_hombre, R.drawable.normal_imc_hombre2};
    private static final int[] SOBREPESO_DRAWABLE = {R.drawable.sobrepeso_mujer, R.drawable.sobrepeso_mujer2, R.drawable.sobrepeso_mujer3,
            R.drawable.sobrepeso_hombre, R.drawable.sobrepeso_hombre2};
    private static final int[] OBESO_DRAWABLE = {R.drawable.obeso, R.drawable.obeso2, R.drawable.obesa, R.drawable.obesa2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_imc);

        final TextView textViewResultado = (TextView) findViewById(R.id.textViewResultado);
        final ImageView imageViewResultado = (ImageView) findViewById(R.id.imageViewResultado);
        final Button buttonVolverCalcular = (Button) findViewById(R.id.buttonVolverCalcular);

        Double imc = MainActivity.getImc(); //obtiene imc calculado en MainActivity.java

        setResultadosImc(imc, textViewResultado, imageViewResultado); //llamada a metodo que establece imagen y texto según imc calculado

        buttonVolverCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //metodo que establece el text e imagen segun Imc calculado
    private void setResultadosImc(double imc, TextView text, ImageView image){
        Random random = new Random();

        if(imc<18.5) {
            int imgBajop = random.nextInt(3); //elige numero aleatorio de 0 a 4
            text.setText(String.format("IMC: %.1f tienes Bajo peso%nInfo: IMC Por debajo de 18.5 Bajo peso", imc ));
            image.setImageResource(FLACO_DRAWABLE[imgBajop]);
        }
        else if(imc>=18.5 && imc<=24.9) {
            int imgNormal = random.nextInt(5); //elige numero aleatorio de 0 a 4
            text.setText(String.format("IMC: %.1f tienes Peso Normal%nInfo: IMC 18.5 – 24.9 Normal", imc ));
            image.setImageResource(NORMAL_DRAWABLE[imgNormal]); //establece la imagen imc normal
        }
        else if(imc>=25.0 && imc<=29.9) {
            int imgSobrep = random.nextInt(5); //elige numero aleatorio de 0 a 4
            text.setText(String.format("IMC: %.1f tienes Sobrepeso%nInfo: IMC 25.0 – 29.9 Sobrepeso", imc ));
            image.setImageResource(SOBREPESO_DRAWABLE[imgSobrep]); //establece la imagen imc sobrepeso
        }
        else if(imc>=30.0) {
            int imgObeso = random.nextInt(4); //elige numero aleatorio de 0 a 4
            text.setText(String.format("IMC: %.1f tienes Obesidad%nInfo: IMC 30.0 o más Obesidad", imc ));
            image.setImageResource(OBESO_DRAWABLE[imgObeso]); //establece la imagen imc Obesidad
        }
    }
}