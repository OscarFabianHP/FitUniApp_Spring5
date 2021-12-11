package co.edu.unab.fituni.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import co.edu.unab.fituni.Callbacks;
import co.edu.unab.fituni.modelo.UsuarioBienestarU;

public class UsuarioBienestarURepoImple implements UsuarioBienestarURepo{
    final static String COLLECTION = "usuarios_bienestaru";
    final static String COLLECTION_CODE = "codigo_bienestaru";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void crear(UsuarioBienestarU usuario, Callbacks callbacks) {
        db.collection(COLLECTION)
                .document(usuario.getRefDoc())//establesco q el nombre del documento sera el usuario q es lo q devuelve getRefDoc()
                .set(usuario.getMapUser())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callbacks.onSuccess(usuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbacks.onFailure(e);
                    }
                });
    }

    @Override
    public void actualizar(UsuarioBienestarU usuario, Callbacks callbacks) {
        db.collection(COLLECTION)
                .document(usuario.getRefDoc())
                .update(usuario.getMapUser())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callbacks.onSuccess(usuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbacks.onFailure(e);
                    }
                });
    }

    @Override
    public void eliminar(String usuarioRefDoc, Callbacks callbacks) {
        db.collection(COLLECTION)
                .document(usuarioRefDoc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callbacks.onSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbacks.onFailure(e);
                    }
                });
    }

    @Override
    public void consultarDocumento(String usuarioRefDoc, Callbacks callbacks) {
        db.collection(COLLECTION)
                .document(usuarioRefDoc)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.isComplete())
                            callbacks.onSuccess(task.getResult());
                        else
                            callbacks.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void comprobarCodigoAdmin(String codigo, Callbacks callbacks){
        db.collection(COLLECTION_CODE)
                .document("codigo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                            callbacks.onSuccess(task.getResult().get("codigo")); //envia el contenido en el campo "codigo" del documento "codigo"
                        else
                            callbacks.onFailure(task.getException());
                    }
                });
    }
}
