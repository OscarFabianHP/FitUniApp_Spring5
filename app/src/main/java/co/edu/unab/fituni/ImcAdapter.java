package co.edu.unab.fituni;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unab.fituni.modelo.IndiceMasaMuscular;

public class ImcAdapter extends RecyclerView.Adapter<ImcViewHolder> {
    //private Context context;
    //private int layoutID;
    private List<IndiceMasaMuscular> list;
    //private LayoutInflater inflater;

    public ImcAdapter(List<IndiceMasaMuscular> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ImcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ImcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImcViewHolder holder, int position) {
        IndiceMasaMuscular imcReg = list.get(position); //obtiene elemnto de la lista de registros
        if(MainActivity.getCorreoPersona()!=null) //solo pone el correo de persona si se ha iniciado sesion
            holder.getTxtUser().setText(String.format("USER:%n%s", imcReg.getPersona().getEmail()));
        holder.getTxtImcReg().setText(String.format("IMC:%n%.1f", imcReg.getImc()));
        holder.getTxtFechaReg().setText(String.format("FECHA:%n%s", imcReg.getFecha()));
        double imc = imcReg.getImc(); //obtinen el IMC de la lista

        if(imc<18.5)
            holder.getImgIconReg().setImageResource(R.drawable.delgadoicon);
        else if(imc>=18.5 && imc<=24.9)
            holder.getImgIconReg().setImageResource(R.drawable.normalicon2);
        else if(imc>=25.0 && imc<=29.9)
            holder.getImgIconReg().setImageResource(R.drawable.sobrepesoicon);
        else if(imc>29.9)
            holder.getImgIconReg().setImageResource(R.drawable.gordoicon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
