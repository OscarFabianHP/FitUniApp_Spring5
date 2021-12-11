package co.edu.unab.fituni;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImcViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imgIconReg;
    private final TextView txtImcReg;
    private final TextView txtFechaReg;
    private final TextView txtUser;

    public ImcViewHolder(@NonNull View itemView) {
        super(itemView);
        imgIconReg = (ImageView) itemView.findViewById(R.id.imageIconReg);
        txtUser = (TextView) itemView.findViewById(R.id.textViewUser);
        txtImcReg = (TextView) itemView.findViewById(R.id.textImcReg);
        txtFechaReg = (TextView) itemView.findViewById(R.id.textFechaReg);
    }

    public ImageView getImgIconReg() {
        return imgIconReg;
    }

    public TextView getTxtImcReg() {
        return txtImcReg;
    }

    public TextView getTxtFechaReg() {
        return txtFechaReg;
    }

    public TextView getTxtUser() {
        return txtUser;
    }
}
