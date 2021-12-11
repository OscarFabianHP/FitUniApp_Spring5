package co.edu.unab.fituni.repository;

import co.edu.unab.fituni.Callbacks;
import co.edu.unab.fituni.modelo.UsuarioBienestarU;

public interface UsuarioBienestarURepo {
    public void crear(UsuarioBienestarU usuario, Callbacks  callbacks);
    public void actualizar(UsuarioBienestarU usuario, Callbacks  callbacks);
    public void eliminar(String usuarioRefDoc, Callbacks callbacks);
    public void consultarDocumento(String usuarioRefDoc, Callbacks callbacks);
    public void comprobarCodigoAdmin(String codigo, Callbacks callbacks);
}
